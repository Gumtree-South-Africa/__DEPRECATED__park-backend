package com.ebay.park.service.item.command;

import static com.ebay.park.service.ServiceException.createServiceException;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.nestedQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.index.query.QueryBuilders.termsQuery;

import java.util.Collection;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;

import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Follower;
import com.ebay.park.db.entity.StatusDescription;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserFollowsGroup;
import com.ebay.park.db.entity.UserFollowsItem;
import com.ebay.park.elasticsearch.document.ItemDocument;
import com.ebay.park.elasticsearch.repository.ItemRepository;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.item.dto.SearchItemRequest;
import com.ebay.park.util.DataCommonUtil;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;

@Component
public class SearchItemCmdHelper {
	
	@Value("${unlogged.latitude}")
	private Double defaultUnloggedLatitude;

	@Value("${unlogged.longitude}")
	private Double defaultUnloggedLongitude;
	
	@Value("${search.pageSize.mobile}")
	private int defaultPageSizeMobile;
	
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

	//sort options
	private static final String ORDER_PUBLISHED = "published";
	private static final String ORDER_PRICE = "price";
	private static final String ORDER_NEAREST = "nearest";
	private static final String ORDER_CATEGORY = "category";
	
	//empty query responsse messages
	private static final String NO_RESULTS_MESSAGE_GENERAL 				= "emptylist.global_items";
	private static final String NO_RESULTS_MESSAGE_CATEGORY 			= "emptylist.category_items";
	private static final String NO_RESULTS_MESSAGE_FROMWISHLIST 		= "emptylist.wishlist_items";
	private static final String NO_RESULTS_MESSAGE_USERITEMS			= "emptylist.profile_items";
	private static final String NO_RESULTS_MESSAGE_FROMFOLLOWGROUPS 	= "emptylist.groups_items";
	private static final String NO_RESULTS_MESSAGE_FROMFOLLOWUSERS 		= "emptylist.following_items";
	private static final String NO_RESULTS_MESSAGE_ITEMSINGROUP 		= "emptylist.group_items";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SearchItemCmdHelper.class);
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private ItemRepository itemRepository;
	
	public Page<ItemDocument> search(SearchItemRequest request) {
		User user = null;
		
		if (!StringUtils.isBlank(request.getToken())) {
			user = userDao.findByToken(request.getToken());
			if (user == null) {
				throw createServiceException(ServiceExceptionCode.USER_UNAUTHORIZED);
			}		
		}
		
		setDefaultDistance(request, user);
		
		SearchQuery searchQuery = createRequestQuery(request,user);
		
		return itemRepository.search(searchQuery);
	}

	/**
	 * Set distance. User data is set if it is available; otherwise, default values are set.
	 * @param request
	 * 			request which has to contain the data
	 * @param user
	 * 			user containing the data to be copied if available
	 */
	public void setDefaultDistance(SearchItemRequest request, User user) {
		if (request.getLatitude() == null) {
			request.setLatitude(user != null ? user.getLatitude() : defaultUnloggedLatitude);
		}

		if (request.getLongitude() == null) {
			request.setLongitude(user != null ? user.getLongitude() : defaultUnloggedLongitude);
		}
    }
	
	private int calculatePageIndex(Integer page, int pageSize) {
		return page != null ? page : 0;
	}

	private int calculatePageSize(Integer pageSize) {
		if (pageSize == null || pageSize <= 0) {
			return defaultPageSizeMobile;
		}
		return pageSize;
	}

	public SearchQuery createRequestQuery(SearchItemRequest request, User user) {
		int pageSize = calculatePageSize(request.getPageSize());
		int pageIndex = calculatePageIndex(request.getPage(), pageSize);

		BoolQueryBuilder mainBuilder = boolQuery();
		RangeQueryBuilder priceQueryBuilder =  QueryBuilders.rangeQuery(ItemDocument.FIELD_PRICE);
		RangeQueryBuilder timeQueryBuilder = QueryBuilders.rangeQuery(ItemDocument.FIELD_PUBLISHED);
		GeoDistanceQueryBuilder distanceFilterBuilder = QueryBuilders.geoDistanceQuery(ItemDocument.FIELD_LOCATION);
		NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
		BoolQueryBuilder filters = QueryBuilders.boolQuery();

		if (request.getRadius() != null && request.getRadius() > 0) {
			distanceFilterBuilder.point(request.getLatitude(), request.getLongitude()).distance(request.getRadius(), DistanceUnit.MILES);
			filters.must(distanceFilterBuilder);
		}

		if (StringUtils.isEmpty(request.getCriteria())) {
			mainBuilder.must(QueryBuilders.matchAllQuery());
		} else {
			mainBuilder.must(buildCriteriaQuery(request.getCriteria()));
		}

		if (request.getCategoryId() != null) {
			mainBuilder.must(termQuery(ItemDocument.FIELD_CATEGORY_ID, request.getCategoryId()));
		}

		if (request.getGroupId() != null) {
			mainBuilder.must(nestedQuery(ItemDocument.FIELD_ITEM_GROUPS, termQuery(ItemDocument.FIELD_ITEM_GROUPS_GROUP_ID, request.getGroupId())));
		}

		if (request.getPriceFrom() != null || request.getPriceTo() != null) {
			priceQueryBuilder.gte(request.getPriceFrom()).lte(request.getPriceTo());
			filters. must(priceQueryBuilder);
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
				// temporary fix: image_pending items are hidden
				mainBuilder.mustNot(boolQuery().should(termQuery(ItemDocument.FIELD_STATUS,
						StatusDescription.IMAGE_PENDING.toString().toLowerCase())));
			}
		} else {
			mainBuilder.must(termQuery(ItemDocument.FIELD_STATUS, StatusDescription.ACTIVE.toString().toLowerCase()));
		}

		/*
		 * The result only includes items created after the request of the first
		 * page in order to avoid new items breaking the original pagination
		 */
		try {
			if (request.getRequestTime() != null) {
				Date requestTime = DataCommonUtil.parseUnixTime(request.getRequestTime());
				timeQueryBuilder.to(requestTime);
				filters.must(timeQueryBuilder);
			}
		} catch (Throwable t) {
			LOGGER.error("t=[{}] request.getRequestTime()=[{}]", t.getMessage(), request.getRequestTime());
		}

		nativeSearchQueryBuilder.withQuery(mainBuilder);

		nativeSearchQueryBuilder = createSort(nativeSearchQueryBuilder, request);
		SearchQuery searchQuery = nativeSearchQueryBuilder.withFilter(filters)
				.withPageable(new PageRequest(pageIndex, pageSize)).build();

		LOGGER.debug("Item Search Sort: {}", searchQuery.getElasticsearchSorts());
		LOGGER.debug("Item Search Query: {}", searchQuery.getQuery());
		LOGGER.debug("Item Search Filters: {}", searchQuery.getFilter());
		return searchQuery;
	}
	
	private BoolQueryBuilder buildCriteriaQuery(String criteria) {
		return boolQuery()
				.should(boolQuery()
						.must(buildFuzzyQuery(ItemDocument.FIELD_NAME, criteria))//full-text search
						.should(matchQuery(ItemDocument.FIELD_NAME+"."+ItemDocument.FIELD_SUFFIX_SHINGLES, criteria).boost(shinglesBooster)))//increase relevance
				.should(matchQuery(ItemDocument.FIELD_NAME+"."+ItemDocument.FIELD_SUFFIX_NGRAMS, criteria).boost(ngramBooster).minimumShouldMatch(ngramMinimumMatch));
	}
	
	private MatchQueryBuilder buildFuzzyQuery(String field, String criteria) {
		return matchQuery(field, criteria)
				.fuzziness(Fuzziness.build(fuzziness))
				.fuzzyTranspositions(fuzzyTranspositions)
				.maxExpansions(maxExpansions)
				.prefixLength(prefixLength)
				.minimumShouldMatch(fuzzyMinimumMatch);
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

	private Collection<Long> getItemsFollowedIds(User user) {
		return Collections2.transform(user.getItemLikeds(),
				new Function<UserFollowsItem, Long>() {
					@Override
					public Long apply(UserFollowsItem item) {
						return item.getItem().getId();
					}
				});
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
	
	public String getNoResultMessageKey(SearchItemRequest request) {
		if (request.getCategoryId() != null) {
			return NO_RESULTS_MESSAGE_CATEGORY;
		}
		if (request.isFromUserWishlist()) {
			return NO_RESULTS_MESSAGE_FROMWISHLIST;
		}
		if (request.getPublisherName() != null) {
			return NO_RESULTS_MESSAGE_USERITEMS;
		}
		if (request.isFromFollowedGroups()) {
			return NO_RESULTS_MESSAGE_FROMFOLLOWGROUPS;
		}
		if (request.isFromFollowedUsers()) {
			return NO_RESULTS_MESSAGE_FROMFOLLOWUSERS;
		}
		if (request.getGroupId() != null) {
			return NO_RESULTS_MESSAGE_ITEMSINGROUP;
		}
		return NO_RESULTS_MESSAGE_GENERAL;

	}
	
}
