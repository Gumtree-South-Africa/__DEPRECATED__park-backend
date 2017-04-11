package com.ebay.park.service.social.command;

import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Follower;
import com.ebay.park.db.entity.User;
import com.ebay.park.elasticsearch.document.UserDocument;
import com.ebay.park.elasticsearch.document.converter.DocumentConverter;
import com.ebay.park.elasticsearch.repository.UserRepository;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.social.dto.BasicUser;
import com.ebay.park.service.social.dto.SearchUserRequest;
import com.ebay.park.service.social.dto.SearchUserResponse;
import com.ebay.park.util.DataCommonUtil;
import com.ebay.park.util.InternationalizationUtil;
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
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.ebay.park.service.ServiceException.createServiceException;
import static org.elasticsearch.index.query.QueryBuilders.*;

@Component
public class SearchUserCmd implements
		ServiceCommand<SearchUserRequest, SearchUserResponse> {

	private static Logger logger = LoggerFactory.getLogger(SearchUserCmd.class);
	
	private static final String NO_RESULTS_MESSAGE_GENERAL = "emptylist.users_search";

	private static final String NO_RESULTS_MESSAGE_SUBSCRIBERS = "emptylist.group_subscribers";


	@Value("${unlogged.latitude}")
	private Double defaultUnloggedLatitude;

	@Value("${unlogged.longitude}")
	private Double defaultUnloggedLongitude;
	
	@Value("${search.pageSize}")
	private int defaultPageSize;

	@Value("${elasticsearch.fuzzyquery.fuzziness}")
	public String fuzziness;

	@Value("${elasticsearch.fuzzyquery.prefix_length}")
	public Integer prefixLength;

	@Value("${elasticsearch.fuzzyquery.max_expansions}")
	public Integer maxExpansions;

	@Value("${elasticsearch.fuzzyquery.booster}")
	public Float fuzzyBooster;

	@Value("${elasticsearch.fuzzyquery.fuzzy_transpositions}")
	public Boolean fuzzyTranspositions;

	@Value("${elasticsearch.fuzzy.minimum_should_match}")
	public String fuzzyMinimumMatch;

	@Value("${elasticsearch.shingles.booster}")
	public Float shinglesBooster;

	@Value("${elasticsearch.ngram.minimum_should_match}")
	public String ngramMinimumMatch;

	@Value("${elasticsearch.ngram.booster}")
	public Float ngramBooster;

	@Autowired
	UserRepository userRepository;

	@Autowired
	private DocumentConverter documentConverter;

	@Autowired
	private UserDao userDao;

	@Autowired
	private InternationalizationUtil i18nUtil;

	@Override
	public SearchUserResponse execute(SearchUserRequest request)
			throws ServiceException {
		User user = null;
		
		if (request.getUsername() != null) {
			user = userDao.findByUsername(request.getUsername());
			if (user == null) {
				throw createServiceException(ServiceExceptionCode.USER_NOT_FOUND);
			}

			if (request.isFollowingMe() && user.getFollowers().isEmpty()) {
				 List<BasicUser> emptyList = Collections.emptyList();
				return new SearchUserResponse(emptyList, 0l, DataCommonUtil.getDateTimeAsUnixFormat(DateTime.now().toDate()));
			}
		}

		setDefaultDistance(request, user);

		try {

			List<BasicUser> users = new ArrayList<>();
			SearchQuery searchQuery = createRequestQuery(request, user);

			Page<UserDocument> page = userRepository.search(searchQuery);
			List<UserDocument> result = page.getContent();
			List<User> list = documentConverter.fromUserDocument(result);

			if (user != null) {
				for (User u : list) {
					BasicUser basicUser = new BasicUser(u);
					basicUser.setFollowedByUser(u.isFollowedByUser(user));
					users.add(basicUser);
				}
			} else {
				for (User u : list) {
					users.add(new BasicUser(u));
				}
			}

			SearchUserResponse response = new SearchUserResponse(users, page.getTotalElements(),DataCommonUtil.getDateTimeAsUnixFormat(DateTime.now().toDate()));
			response.setAmountUsersFound((int) page.getTotalElements());

			//language definition
			String language = request.getLanguage();
			if ((language == null) && (user != null)) {
				language = user.getIdiom().getCode();
			}

			i18nUtil.internationalizeListedResponse(response, getNoResultMessageKey(request),
					language);

			return response;
		} catch (Exception e) {
            logger.error("Error searching users. Request: {}", request.toString(), e);
			throw createServiceException(ServiceExceptionCode.SEARCH_TERM_IS_NOT_SPECIFIC_ENOUGH);
		}
	}

	private Collection<Long> getFollowersIds(User user) {
		return Collections2.transform(user.getFollowers(),
				new Function<Follower, Long>() {
					@Override
					public Long apply(Follower follower) {
						return follower.getId().getFollowerId();
					}
				});
	}

	protected SearchQuery createRequestQuery(SearchUserRequest request, User user) {

		int pageSize = calculatePageSize(request.getPageSize());
		int pageIndex = calculatePageIndex(request.getPage(), pageSize);
		BoolQueryBuilder mainBuilder = boolQuery();
		GeoDistanceQueryBuilder distanceFilterBuilder = geoDistanceQuery("location");
		RangeQueryBuilder timeQueryBuilder = QueryBuilders.rangeQuery(UserDocument.FIELD_CREATION_DATE);
		NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
		//BoolQueryBuilder filters = new BoolQueryBuilder();

		if (request.getCriteria() == null
				|| StringUtils.isEmpty(request.getCriteria())) {
            //FIXME whaqt about query injection here? please double check no possible issues are here
			mainBuilder.must(QueryBuilders.matchAllQuery());

		} else {
			mainBuilder.must(buildCriteriaQuery(request.getCriteria()));
            //mainBuilder.should(buildFuzzyQuery(UserDocument.FIELD_USERNAME, request.getCriteria()));
		}

		if (request.getRadius() != null && request.getRadius() > 0) {
			distanceFilterBuilder.point(request.getLatitude(), request.getLongitude()).distance(request.getRadius(), DistanceUnit.MILES);
            mainBuilder.must(distanceFilterBuilder);
		}

		if (user != null) {
			if (request.isFollowingMe()) {
				mainBuilder.must(termsQuery(UserDocument.FIELD_USER_ID, getFollowersIds(user)));
			}
			if (request.isFollowed()) {
				mainBuilder.must(nestedQuery(UserDocument.FIELD_FOLLOWERS,
						termQuery(UserDocument.FIELD_FOLLOWER_ID, user.getId())));
			}
		}

		if(request.getGroupId()!= null){
			mainBuilder.must(nestedQuery(UserDocument.FIELD_GROUPS,termQuery(
					UserDocument.FIELD_GROUP_ID,request.getGroupId())));
		}

		/*
         * The result only includes users created after the request of the first
         * page in order to avoid new users breaking the original pagination.
         */
        try {
            if (request.getRequestTime() != null) {
                timeQueryBuilder.to(DataCommonUtil.parseUnixTime(request.getRequestTime()));
                mainBuilder.must(timeQueryBuilder);
            }
        } catch (Throwable t) {
            logger.error("t=[{}] request.getRequestTime()=[{}]", t.getMessage(), request.getRequestTime());
        }

		if(mainBuilder.hasClauses()) {
			nativeSearchQueryBuilder.withQuery(mainBuilder);
		}

		createSort(nativeSearchQueryBuilder, request);

		SearchQuery searchQuery = nativeSearchQueryBuilder
		        //.withFilter(filters)
		        .withPageable(new PageRequest(pageIndex, pageSize))
		        .build();
		logger.debug("User Search Sort: {}", searchQuery.getElasticsearchSorts());
		logger.debug("User Search Query: {}", searchQuery.getQuery());
		logger.debug("User Search Filter: {}", searchQuery.getFilter());

		return searchQuery;
	}

	private int calculatePageIndex(Integer page, int pageSize) {
		return page != null ? page : 0;
	}

	private int calculatePageSize(Integer pageSize) {
		if (pageSize == null || pageSize <= 0) {
			return defaultPageSize;
		}

		return pageSize;
	}

	private NativeSearchQueryBuilder createSort(NativeSearchQueryBuilder nativeSearchQueryBuilder, SearchUserRequest request) {
		if (request.getOrder() != null) {
			if (request.getOrder().equals("nearest")) {
				nativeSearchQueryBuilder.withSort(SortBuilders.geoDistanceSort(
						UserDocument.FIELD_LOCATION).point(request.getLatitude(),
								request.getLongitude()).order(SortOrder.ASC)
						);
			} else {
				nativeSearchQueryBuilder.withSort(SortBuilders
                        .fieldSort("username." + UserDocument.FIELD_USERNAME_SORTABLE)
                        .order(SortOrder.ASC)
                        .unmappedType("String"));
			}
		}

		return nativeSearchQueryBuilder;
	}

	
	/**
	 * Set distance. User data is set if it is available; otherwise, default values are set.
	 * @param request
	 * 			request which has to contain the data
	 * @param user
	 * 			user containing the data to be copied if available
	 */
	private void setDefaultDistance(SearchUserRequest request, User user) {
		
		if (request.getLatitude() == null) {
			request.setLatitude(user != null ? user.getLatitude() : defaultUnloggedLatitude);
		}
		
		if (request.getLongitude() == null) {
			request.setLongitude(user != null ? user.getLongitude() : defaultUnloggedLongitude);
		}
	}
	
	private String getNoResultMessageKey(SearchUserRequest request){
		if(request.getGroupId()!= null){
			return NO_RESULTS_MESSAGE_SUBSCRIBERS;
		}
		return NO_RESULTS_MESSAGE_GENERAL;
		
	}

	protected BoolQueryBuilder buildCriteriaQuery(String criteria) {
		return boolQuery()
				.should(buildFuzzyQuery(UserDocument.FIELD_USERNAME, criteria));
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