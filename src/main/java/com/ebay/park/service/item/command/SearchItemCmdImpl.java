package com.ebay.park.service.item.command;

import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.*;
import com.ebay.park.elasticsearch.document.ItemDocument;
import com.ebay.park.elasticsearch.document.converter.DocumentConverter;
import com.ebay.park.elasticsearch.repository.ItemRepository;
import com.ebay.park.service.item.ItemUtils;
import com.ebay.park.service.item.dto.GetNewItemInformationRequest;
import com.ebay.park.service.item.dto.ItemSummary;
import com.ebay.park.service.item.dto.SearchItemRequest;
import com.ebay.park.service.item.dto.SearchItemResponse;
import com.ebay.park.service.user.UserServiceHelper;
import com.ebay.park.util.DataCommonUtil;
import com.ebay.park.util.InternationalizationUtil;
import com.ebay.park.util.LanguageUtil;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Cmd that searches for items.
 * @author Mauricio Mariño Pinilla & Julieta Salvadó
 */
@Component
public class SearchItemCmdImpl implements SearchItemCmd {

	private static final String NO_RESULTS_MESSAGE_GENERAL 				= "emptylist.global_items";
	private static final String NO_RESULTS_MESSAGE_CATEGORY 			= "emptylist.category_items";
	private static final String NO_RESULTS_MESSAGE_FROMWISHLIST 		= "emptylist.wishlist_items";
	private static final String NO_RESULTS_MESSAGE_USERITEMS			= "emptylist.profile_items";
	private static final String NO_RESULTS_MESSAGE_FROMFOLLOWGROUPS 	= "emptylist.groups_items";
	private static final String NO_RESULTS_MESSAGE_FROMFOLLOWUSERS 		= "emptylist.following_items";
	private static final String NO_RESULTS_MESSAGE_ITEMSINGROUP 		= "emptylist.group_items";

	private static final Logger LOGGER = LoggerFactory.getLogger(SearchItemCmdImpl.class);

	//sort options
	private static final String ORDER_PUBLISHED = "published";
	private static final String ORDER_PRICE = "price";
	private static final String ORDER_NEAREST = "nearest";
	private static final String ORDER_CATEGORY = "category";

	//fields
	private static final String FIELD_SHINGLE = ItemDocument.FIELD_NAME + "." + ItemDocument.FIELD_SUFFIX_SHINGLES;
	private static final String NGRAM_FIELD = ItemDocument.FIELD_NAME + "." + ItemDocument.FIELD_SUFFIX_NGRAMS;


	@Value("${search.pageSize.mobile}")
	private int defaultPageSizeMobile;

	@Value("${unlogged.latitude}")
	private Double defaultUnloggedLatitude;

	@Value("${unlogged.longitude}")
	private Double defaultUnloggedLongitude;

	@Value("${elasticsearch.fuzzyquery.fuzziness}")
	String fuzziness;

	@Value("${elasticsearch.fuzzyquery.prefix_length}")
	Integer prefixLength;

	@Value("${elasticsearch.fuzzyquery.max_expansions}")
	Integer maxExpansions;

	@Value("${elasticsearch.fuzzyquery.booster}")
	Float fuzzyBooster;

	@Value("${elasticsearch.fuzzyquery.fuzzy_transpositions}")
	Boolean fuzzyTranspositions;

	@Value("${elasticsearch.shingles.booster}")
	Float shinglesBooster;

	@Value("${elasticsearch.fuzzy.minimum_should_match}")
	String fuzzyMinimumMatch;

	@Value("${elasticsearch.ngram.minimum_should_match}")
	String ngramMinimumMatch;

	@Value("${elasticsearch.ngram.booster}")
	Float ngramBooster;

	@Autowired
	private UserServiceHelper userHelper;

	@Autowired
	private InternationalizationUtil i18nUtil;

	@Autowired
	private ItemUtils itemUtils;

	@Autowired
	private DocumentConverter documentConverter;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private UserDao userDao;

	@Autowired
	private GetNewItemInformationCmd getNewItemInformationCmd;

	@Override
	public SearchItemResponse execute(SearchItemRequest request) {

		User user = null;
		if (!StringUtils.isBlank(request.getToken())) {
			user = userHelper.findAuthorizedUserByToken(request.getToken());
		}

		setDefaultDistance(request, user);

		String language = LanguageUtil.getLanguageForUserRequest(user, request.getLanguage());
		try {
			Page<ItemDocument> page = itemRepository.search(createRequestQuery(request,user));
			List<Item> items = documentConverter.fromItemDocument(page.getContent());

			List<ItemSummary> itemsDTO = itemUtils.convertToItemSummary(items, user, language);

			if (request.getTagNewItem() && request.getGroupId() != null && user != null) {
				getNewItemInformationCmd.execute(new GetNewItemInformationRequest(
						itemsDTO, user.getUserId(), request.getGroupId()));
			}

			SearchItemResponse searchItemResponse = new SearchItemResponse(itemsDTO, page.getTotalElements(),
					DataCommonUtil.getDateTimeAsUnixFormat(DateTime.now().toDate()));

			i18nUtil.internationalizeListedResponse(
					searchItemResponse,
					getNoResultMessageKey(request),
					language);

			return searchItemResponse;

		}
		catch (Exception e){
			e.printStackTrace();
			return buildEmptyQueryResponse(request, language);
		}
	}

	private SearchItemResponse buildEmptyQueryResponse(SearchItemRequest request, String language) {
		SearchItemResponse searchItemResponse = new SearchItemResponse(new ArrayList<>(), 0,
				DataCommonUtil.getDateTimeAsISO(DateTime.now().toDate()));

		i18nUtil.internationalizeListedResponse(searchItemResponse, getNoResultMessageKey(request), language);
		return searchItemResponse;
	}

	private NativeSearchQueryBuilder createSort(NativeSearchQueryBuilder nativeSearchQueryBuilder,SearchItemRequest request) {

		if (request.getOrder() != null) {
			String fieldsAndDirections = request.getOrder();

			for (String order : fieldsAndDirections.split(",")) {

				SortOrder sortOrder = SortOrder.ASC;

				//reverse
				if (order.startsWith("-")) {
					order = order.replaceFirst("-", StringUtils.EMPTY);
					sortOrder = SortOrder.DESC;
				}

				if (order.equals(ORDER_PUBLISHED)) {
					//order=published means newest to oldest
					sortOrder = flipOrder(sortOrder);
				}

				if (ORDER_PRICE.equalsIgnoreCase(order)) {
					nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(ItemDocument.FIELD_PRICE).order(sortOrder));
				} else if (ORDER_CATEGORY.equalsIgnoreCase(order)) {
					nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(ItemDocument.FIELD_CAREGORY_ORDER).order(sortOrder));
				} else if (ORDER_NEAREST.equalsIgnoreCase(order)){
					nativeSearchQueryBuilder.withSort(SortBuilders.geoDistanceSort(ItemDocument.FIELD_LOCATION).point(request.getLatitude(), request.getLongitude()).order(sortOrder));
				} else if (ORDER_PUBLISHED.equalsIgnoreCase(order)){
					nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(ItemDocument.FIELD_PUBLISHED).order(sortOrder));
				} else {
					nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(order).order(sortOrder));
				}
			}
		} else {
			nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(ItemDocument.FIELD_PUBLISHED).order(SortOrder.DESC));
			nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(ItemDocument.FIELD_NAME).order(SortOrder.ASC));
			nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(ItemDocument.FIELD_PRICE).order(SortOrder.ASC));
			nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(ItemDocument.FIELD_CAREGORY_ORDER).order(SortOrder.ASC));
		}

		return nativeSearchQueryBuilder;
	}

	private SortOrder flipOrder(SortOrder order) {
		if (order.equals(SortOrder.DESC)) {
			return SortOrder.ASC;
		}
		return SortOrder.DESC;
	}

	private int calculatePageIndex(Integer page) {
		return page != null ? page : 0;
	}

	private int calculatePageSize(Integer pageSize) {
		if (pageSize == null || pageSize <= 0) {
			return defaultPageSizeMobile;
		}

		return pageSize;
	}

	protected SearchQuery createRequestQuery(SearchItemRequest request, User user) {
		BoolQueryBuilder mainBuilder = boolQuery();

		GeoDistanceQueryBuilder distanceFilterBuilder = QueryBuilders.geoDistanceQuery(ItemDocument.FIELD_LOCATION);
		NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();

		if (request.getRadius() != null && request.getRadius() > 0) {
			distanceFilterBuilder.point(request.getLatitude(), request.getLongitude()).distance(request.getRadius(), DistanceUnit.MILES);
			mainBuilder.filter(distanceFilterBuilder);
		}

		if (StringUtils.isNotEmpty(request.getCriteria())) {
			mainBuilder.must(buildCriteriaQuery(request.getCriteria()));
		}

		if (request.getCategoryId() != null) {
			mainBuilder.must(buildCategoryFilter(request.getCategoryId()));
		}

		if (request.getGroupId() != null) {
			mainBuilder.must(nestedQuery(ItemDocument.FIELD_ITEM_GROUPS, termQuery(ItemDocument.FIELD_ITEM_GROUPS_GROUP_ID, request.getGroupId())));
		}

		if (request.getPriceFrom() != null || request.getPriceTo() != null) {
			mainBuilder.filter(buildPriceFilter(request.getPriceFrom(), request.getPriceTo()));
		}

		if (user!= null) {
			if (request.isFromFollowedUsers()) {
				mainBuilder.must(termsQuery(ItemDocument.FIELD_PUBLISHER_ID, getUsersFollowedIds(user)));
			}

			if (request.isFromUserWishlist()) {
				mainBuilder.must(termsQuery(ItemDocument.FIELD_ID, getItemsFollowedIds(user)));
				mainBuilder.must(termQuery(ItemDocument.FIELD_DELETED,false));
			}

			if(request.isFromFollowedGroups()){
				mainBuilder.must(nestedQuery(ItemDocument.FIELD_ITEM_GROUPS, termsQuery(ItemDocument.FIELD_ITEM_GROUPS_GROUP_ID,
						getUserGroupFollowedIds(user))));
			}
		}

		if (request.getPublisherName() != null) {
			//FIXME: userDao.findByUsername(request.getPublisherName()) will be null when the name does not match perfectly
			User userPublisher = userDao.findByUsername(request.getPublisherName());
			mainBuilder.must( termQuery(ItemDocument.FIELD_PUBLISHER_ID, userPublisher.getId()));

			if ((user == null) || (null != user && !request.getPublisherName().equals(user.getUsername()))){
				mainBuilder.must(boolQuery()
						.should(termQuery(ItemDocument.FIELD_STATUS, StatusDescription.ACTIVE.toString().toLowerCase()))
						.should(termQuery(ItemDocument.FIELD_STATUS,StatusDescription.SOLD.toString().toLowerCase())));

			} else {
				//If the logged-in user is searching by publisher name, the search must answer without filtering by status.
				//temporary fix: image_pending items are hidden
				mainBuilder.mustNot(boolQuery()
						.should(termQuery(ItemDocument.FIELD_STATUS, StatusDescription.IMAGE_PENDING.toString().toLowerCase())));
			}
		} else {
			mainBuilder.must(termQuery(ItemDocument.FIELD_STATUS,StatusDescription.ACTIVE.toString().toLowerCase()));
		}

		/*
		 * The result only includes items created after the request of the first
		 * page in order to avoid new items breaking the original pagination
		 */
		try {
			if (request.getRequestTime() != null) {
				mainBuilder.filter(addTimeFilter(request.getRequestTime()));
			}
		} catch (Throwable t) {
			LOGGER.error("t=[{}] request.getRequestTime()=[{}]", t.getMessage(), request.getRequestTime());
		}

		nativeSearchQueryBuilder = createSort(nativeSearchQueryBuilder,request);
		SearchQuery  searchQuery = nativeSearchQueryBuilder
				.withQuery(mainBuilder)
				.withPageable(addPageable(request.getPage(), request.getPageSize()))
				.build();

		LOGGER.debug("Item Search Sort: {}", searchQuery.getElasticsearchSorts());
		LOGGER.debug("Item Search Query: {}", searchQuery.getQuery());
		LOGGER.debug("Item Search Filters: {}", searchQuery.getQuery());
		return searchQuery;
	}

	protected Pageable addPageable(Integer pageIndexFromRequest, Integer pageSizeFromRequest) {
		int pageSize = calculatePageSize(pageSizeFromRequest);
		int pageIndex = calculatePageIndex(pageIndexFromRequest);
		return new PageRequest(pageIndex, pageSize);
	}

	//TODO refactor this. It's almost the same on each cmd
	protected QueryBuilder addTimeFilter(String requestTime) {
		RangeQueryBuilder timeFilter = QueryBuilders.rangeQuery(ItemDocument.FIELD_PUBLISHED);
		timeFilter.to(DataCommonUtil.parseUnixTime(requestTime));
		return timeFilter;
	}

	protected TermQueryBuilder buildCategoryFilter(Long categoryId) {
		return termQuery(ItemDocument.FIELD_CATEGORY_ID, categoryId);
	}

	protected QueryBuilder buildPriceFilter(Double priceFrom, Double priceTo) {
		RangeQueryBuilder priceQueryBuilder = QueryBuilders.rangeQuery(ItemDocument.FIELD_PRICE);
		return priceQueryBuilder.gte(priceFrom).lte(priceTo);
	}

	private void setDefaultDistance(SearchItemRequest request, User user) {

		if (request.getLatitude() == null) {
			request.setLatitude(user != null ? user.getLatitude() : defaultUnloggedLatitude);
		}

		if (request.getLongitude() == null) {
			request.setLongitude(user != null ? user.getLongitude() : defaultUnloggedLongitude);
		}
	}

	private String getNoResultMessageKey(SearchItemRequest request){
		if (request.getCategoryId() != null) {
			return NO_RESULTS_MESSAGE_CATEGORY;
		}
		if (request.isFromUserWishlist()) {
			return NO_RESULTS_MESSAGE_FROMWISHLIST;
		}
		if (request.getPublisherName()!=null) {
			return NO_RESULTS_MESSAGE_USERITEMS;
		}
		if (request.isFromFollowedGroups()) {
			return NO_RESULTS_MESSAGE_FROMFOLLOWGROUPS;
		}
		if (request.isFromFollowedUsers()) {
			return NO_RESULTS_MESSAGE_FROMFOLLOWUSERS;
		}
		if (request.getGroupId()!=null) {
			return NO_RESULTS_MESSAGE_ITEMSINGROUP;
		}
		return NO_RESULTS_MESSAGE_GENERAL;

	}

	private Collection<Long> getItemsFollowedIds(User user) {
		return Collections2.transform(user.getItemLikeds(),
				new Function<UserFollowsItem, Long>() {
					@Override
					public Long apply(UserFollowsItem item) {
						return item.getItem().getId();
					}
				});
	}

	private Collection<Long> getUsersFollowedIds(User user) {
		return Collections2.transform(user.getFollowed(),
				new Function<Follower, Long>() {
					@Override
					public Long apply(Follower follower) {
						return follower.getId().getUserId();
					}
				});
	}

	private Collection<Long> getUserGroupFollowedIds(User user) {
		return Collections2.transform(user.getGroups(),
				new Function<UserFollowsGroup, Long>() {
					@Override
					public Long apply(UserFollowsGroup group) {
						return group.getId().getGroupId();
					}
				});
	}

	protected BoolQueryBuilder buildCriteriaQuery(String criteria) {
		return boolQuery()
				.should(boolQuery()
						.must(buildFuzzyQuery(ItemDocument.FIELD_NAME, criteria))//full-text search
						.should(matchQuery(FIELD_SHINGLE, criteria).boost(shinglesBooster)))//increase relevance
				.should(buildNGramQuery(criteria));
	}

	protected MatchQueryBuilder buildNGramQuery(String criteria) {
		return matchQuery(NGRAM_FIELD, criteria).boost(ngramBooster).minimumShouldMatch(ngramMinimumMatch);
	}

	protected MatchQueryBuilder buildFuzzyQuery(String field, String criteria) {

		return matchQuery(field, criteria)
				.fuzziness(Fuzziness.build(fuzziness))
				.fuzzyTranspositions(fuzzyTranspositions)
				.maxExpansions(maxExpansions)
				.prefixLength(prefixLength)
				.minimumShouldMatch(fuzzyMinimumMatch);
	}


}
