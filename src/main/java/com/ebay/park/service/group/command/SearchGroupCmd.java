package com.ebay.park.service.group.command;

import com.ebay.park.db.entity.Group;
import com.ebay.park.db.entity.User;
import com.ebay.park.elasticsearch.document.GroupDocument;
import com.ebay.park.elasticsearch.document.UserDocument;
import com.ebay.park.elasticsearch.document.converter.DocumentConverter;
import com.ebay.park.elasticsearch.repository.GroupRepository;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.group.dto.GetGroupCounterRequest;
import com.ebay.park.service.group.dto.SearchGroupRequest;
import com.ebay.park.service.group.dto.SearchGroupResponse;
import com.ebay.park.service.item.dto.SmallGroupDTO;
import com.ebay.park.service.user.UserServiceHelper;
import com.ebay.park.util.*;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
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
import java.util.List;

import static com.ebay.park.service.ServiceException.createServiceException;
import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Command for searching groups according to a set of criteria.
 * @author federico.jaite | gabriel.sideri | Julieta Salvadó
 */
@Component
public class SearchGroupCmd implements ServiceCommand<SearchGroupRequest, SearchGroupResponse> {

	private static Logger logger = LoggerFactory.getLogger(SearchGroupCmd.class);

	private static final String NO_RESULTS_MESSAGE_GENERAL = "emptylist.group_search";

	private static final String NO_RESULTS_MESSAGE_SUBSCRIBED = "emptylist.groups_suscribed";

	private static final String NO_RESULTS_MESSAGE_SEARCH_BY_OWNER = "emptylist.groups_search_by_owner";

	private static final String NO_RESULTS_MESSAGE_SEARCH_BY_NEAREST = "emptylist.group_search_by_nearest";

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

	public static final String FIELD_CREATOR_NAME = "creator."
			+ UserDocument.FIELD_USERNAME;
	public static final String FIELD_LOCATION = "groupLocation";

	@Value("${search.pageSize}")
	private int defaultPageSize;

	@Autowired
	GroupRepository groupRepository;

	@Autowired
	private DocumentConverter documentConverter;

	@Autowired
	private InternationalizationUtil i18nUtil;

	@Autowired
	private TextUtils textUtils;

	@Autowired
	private GetGroupCounterCmd getGroupCounterCmd;

	@Autowired
	private UserServiceHelper userHelper;

	@Autowired
	private QueryUtils queryUtils;

	@Override
	public SearchGroupResponse execute(SearchGroupRequest request)
			throws ServiceException {
		User user = null;
		if (request.getToken() != null) {
			user = userHelper.findAuthorizedUserByToken(request.getToken());
		}

		setDefaultDistance(request, user);

		try {
			Page<GroupDocument> page = groupRepository.search(createRequestQuery(request, user));
			List<Group> groups = documentConverter.fromGroupDocument(page.getContent());

			ArrayList<SmallGroupDTO> groupsDTO = new ArrayList<SmallGroupDTO>(
					groups.size());
			if (user != null) {
				for (Group group : groups) {
					SmallGroupDTO smallGroup = SmallGroupDTO.fromGroup(group);
					smallGroup.setSubscribed(user.isSubscribedToGroup(group));
					smallGroup.setURL(textUtils.createGroupSEOURL(
							smallGroup.getName(), smallGroup.getId()));
					groupsDTO.add(smallGroup);
				}
			} else {
				for (Group group : groups) {
					SmallGroupDTO smallGroup = SmallGroupDTO.fromGroup(group);
					smallGroup.setURL(textUtils.createGroupSEOURL(
							smallGroup.getName(), smallGroup.getId()));
					groupsDTO.add(smallGroup);
				}
			}

			if (request.findOnlyUserFollowsGroup() || request.isOnlyOwned()) {
				getGroupCounterCmd.execute(new GetGroupCounterRequest(groupsDTO, user.getUserId()));
			}

			SearchGroupResponse searchGroupResponse = new SearchGroupResponse(
					groupsDTO, (int) page.getTotalElements(),
					DataCommonUtil.getDateTimeAsUnixFormat(DateTime.now().toDate()));

			i18nUtil.internationalizeListedResponse(
					searchGroupResponse,
					getNoResultMessageKey(request),
					LanguageUtil.getLanguageForUserRequest(user, request.getLanguage())
			);

			return searchGroupResponse;
		} catch (Exception e) {
			throw createServiceException(ServiceExceptionCode.SEARCH_TERM_IS_NOT_SPECIFIC_ENOUGH);
		}
	}

	private int calculatePageIndex(Integer page) {
		return page == null || page < 0 ? 0 : page;
	}

	private int calculatePageSize(Integer pageSize) {
		return pageSize == null || pageSize <= 0 ? defaultPageSize : pageSize;
	}

	protected SearchQuery createRequestQuery(SearchGroupRequest request, User user) {
		BoolQueryBuilder mainBuilder = boolQuery();
		NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();

		if (request.getRadius() != null && request.getRadius() > 0) {
			mainBuilder.filter(queryUtils.buildDistanceFilter(request));
		}

		if (!StringUtils.isEmpty(request.getCriteria())) {
			mainBuilder.must(buildCriteriaQuery(request.getCriteria()));
		}

		// this logic is only used with the endpoint groups/username/{username}
		if (user != null) {
			// Only groups followed by the user
			if (request.findOnlyUserFollowsGroup()) {
				mainBuilder.must(addOnlyUserFollowsGroup(user.getId()));
			}
			// Only groups created by the user
			if (request.isOnlyOwned()) {
				mainBuilder.must(addOnlyOwnedItemsFilter(user.getUsername()));
			}
		}

		/*
         * The result only includes groups created before the request of the first
         * page in order to avoid new groups breaking the original pagination
         */
		try {
			if (request.getRequestTime() != null) {
				mainBuilder.filter(buildTimeFilter(request.getRequestTime()));
			}
		} catch (Throwable t) {
			logger.error("t=[{}] request.getRequestTime()=[{}]", t.getMessage(), request.getRequestTime());
		}

		if (mainBuilder.hasClauses()) {
			nativeSearchQueryBuilder.withQuery(mainBuilder);
		}

		SearchQuery searchQuery = nativeSearchQueryBuilder
				.withSort(addSort(request))
				.withPageable(addPageable(request.getPage(), request.getPageSize()))
				.build();

		logger.debug("Group Search Sort: {}", searchQuery.getElasticsearchSorts());
		logger.debug("Group Search Query: {}", searchQuery.getQuery());
		logger.debug("Group Search Filter: {}", searchQuery.getFilter());
		return searchQuery;
	}

	/**
	 * Sorting. When strategy by name is not set, sorts by nearest.
	 * @param request
	 * 			sorting information
	 * @return
	 * 		Sort element
	 */
	private SortBuilder addSort(SearchGroupRequest request) {
		if (request.getOrder() != null && request.getOrder().equals("name")) {
			return getOrderByGroupName();
		} else {
			return getOrderByNearest(request.getLatitude(), request.getLongitude());
		}
	}

	protected SortBuilder getOrderByNearest(Double latitude, Double longitude) {
		return SortBuilders
				.geoDistanceSort(Group.FIELD_LOCATION)
				.point(latitude, longitude)
				.order(SortOrder.ASC);
	}

	protected FieldSortBuilder getOrderByGroupName() {
		return SortBuilders
				.fieldSort(GroupDocument.FIELD_GROUPNAME_SORTABLE)
				.order(SortOrder.ASC);
	}

	protected Pageable addPageable(Integer pageIndexFromRequest, Integer pageSizeFromRequest) {
		int pageSize = calculatePageSize(pageSizeFromRequest);
		int pageIndex = calculatePageIndex(pageIndexFromRequest);
		return new PageRequest(pageIndex, pageSize);
	}

	protected TermQueryBuilder addOnlyOwnedItemsFilter(String username) {
		return termQuery(Group.FIELD_CREATOR_NAME, username.toLowerCase());
	}

	protected NestedQueryBuilder addOnlyUserFollowsGroup(Long userId) {
		return nestedQuery(GroupDocument.FIELD_GROUPS,
				termQuery(GroupDocument.FIELD_GROUPS_USER_ID, userId));
	}

	//TODO refactor
	protected QueryBuilder buildTimeFilter(String requestTime) {
		RangeQueryBuilder timeFilter = QueryBuilders.rangeQuery(GroupDocument.FIELD_CREATION);
		timeFilter.to(DataCommonUtil.parseUnixTime(requestTime));
		return timeFilter;
	}

	/**
	 * Set distance. User data is set if it is available; otherwise, default values are set.
	 * @param request
	 * 			request which has to contain the data
	 * @param user
	 * 			user containing the data to be copied if available
	 */
	private void setDefaultDistance(SearchGroupRequest request, User user) {

		if (request.getLatitude() == null) {
			request.setLatitude(user != null ? user.getLatitude() : defaultUnloggedLatitude);
		}

		if (request.getLongitude() == null) {
			request.setLongitude(user != null ? user.getLongitude() : defaultUnloggedLongitude);
		}
	}

	private String getNoResultMessageKey(SearchGroupRequest request) {

		if (request.isOnlyOwned()) {
			return NO_RESULTS_MESSAGE_SEARCH_BY_OWNER;
		}

		if (request.findOnlyUserFollowsGroup()
				& StringUtils.isEmpty(request.getCriteria())) {
			return NO_RESULTS_MESSAGE_SUBSCRIBED;
		}

		if (request.getRadius() != null && request.getRadius() > 0) {
			return NO_RESULTS_MESSAGE_SEARCH_BY_NEAREST;
		}

		return NO_RESULTS_MESSAGE_GENERAL;

	}

	protected BoolQueryBuilder buildCriteriaQuery(String criteria) {
		return boolQuery()
				.should(buildFuzzyQuery(GroupDocument.FIELD_GROUPNAME, criteria))
				.should(buildFuzzyQuery(GroupDocument.FIELD_GROUPNAME_FOLDED, criteria));
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