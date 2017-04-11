package com.ebay.park.service.moderation.command;

import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserStatusDescription;
import com.ebay.park.elasticsearch.document.UserDocument;
import com.ebay.park.elasticsearch.document.converter.DocumentConverter;
import com.ebay.park.elasticsearch.repository.UserRepository;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.moderation.dto.ModerationUserSummary;
import com.ebay.park.service.moderation.dto.SearchUserForModerationRequest;
import com.ebay.park.service.moderation.dto.SearchUserForModerationResponse;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.ebay.park.service.ServiceException.createServiceException;
import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * This class is intended to return the list of items filtering by
 * {@link FilterType}
 */
@Component
public class SearchUserForModerationCmdImpl implements
		SearchUserForModerationCmd {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SearchUserForModerationCmdImpl.class);

	@Value("${search.pageSize}")
	private int defaultPageSize;

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

	@Value("${elasticsearch.fuzzy.minimum_should_match}")
	String fuzzyMinimumMatch;

	@Value("${elasticsearch.shingles.booster}")
	Float shinglesBooster;

	@Value("${elasticsearch.ngram.minimum_should_match}")
	String ngramMinimumMatch;

	@Value("${elasticsearch.ngram.booster}")
	Float ngramBooster;

	@Autowired
	UserRepository userRepository;

	@Autowired
	private DocumentConverter documentConverter;

	@Override
	public SearchUserForModerationResponse execute(
			SearchUserForModerationRequest request) {
		SearchUserForModerationResponse searchUsersResponse = new SearchUserForModerationResponse();

		try {

			SearchQuery searchQuery = createRequestQuery(request);
			Page<UserDocument> page = userRepository.search(searchQuery);
			List<UserDocument> userDocumentList = page.getContent();
			List<User> listUsers = documentConverter
					.fromUserDocument(userDocumentList);

			searchUsersResponse.setAmountUsersFound((int) page.getTotalElements());
			searchUsersResponse.setUsers(new ArrayList<ModerationUserSummary>(
					(int) page.getTotalElements()));

			for (User user : listUsers) {
				searchUsersResponse.getUsers().add(
						ModerationUserSummary.fromUser(user));
			}

		} catch (Exception e) {
            LOGGER.error("Searching users for moderation. Request: {}", request.toString(), e);
			throw createServiceException(ServiceExceptionCode.MODERATION_SEARCH_USER_ERROR);
		} finally {
		}

		return searchUsersResponse;
	}

	SearchQuery createRequestQuery(
			SearchUserForModerationRequest request) {

		BoolQueryBuilder mainBuilder = boolQuery();
		NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();

		int pageSize = calculatePageSize(request.getPageSize());
		int pageIndex = calculatePageIndex(request.getPage(), pageSize);
		try {
			if (!StringUtils.isEmpty(request.getUsername())) {
				if (request.isExactMatch()) {
					mainBuilder.must(buildCriteriaQueryForUsername(request.getUsername()));
				} else {
					mainBuilder.must(QueryBuilders.matchAllQuery());
				}
			}
			if (!StringUtils.isEmpty(request.getEmail())) {
				if (request.isExactMatch()) {
					mainBuilder.must(buildCriteriaQueryForEmail(request.getEmail()));
				} else {
					mainBuilder.must(QueryBuilders.matchAllQuery());
				}
			}
			if (request.getUserVerified() != null) {
				mainBuilder.must(termQuery("emailVerified",
						request.getUserVerified()));
			}
			if (!StringUtils.isEmpty(request.getStatus())) {
				try {
					UserStatusDescription userStatus = UserStatusDescription
							.valueOf(request.getStatus());
					mainBuilder.must(termQuery(UserDocument.FIELD_STATUS, userStatus.toString().toLowerCase()));
				} catch (Exception e) {
                    LOGGER.error("Invalid User Status: {}", request.getStatus());
				}
			}

			if (mainBuilder.hasClauses()) {
				nativeSearchQueryBuilder.withQuery(mainBuilder);
			}
			nativeSearchQueryBuilder = createSort(nativeSearchQueryBuilder,
					request);

			SearchQuery searchQuery = nativeSearchQueryBuilder.withPageable(
					new PageRequest(pageIndex, pageSize)).build();

			LOGGER.debug("User Search Query: {}", searchQuery.getQuery());
			LOGGER.debug("User Search Sort: {}", searchQuery.getSort());

			return searchQuery;
		} catch (Exception e) {
			throw e;
		}
	}

	private NativeSearchQueryBuilder createSort(
			NativeSearchQueryBuilder nativeSearchQueryBuilder,
			SearchUserForModerationRequest request) {
		String order = request.getOrder();
		SortOrder sortOrder = SortOrder.ASC;

		if (order != null && order.startsWith("-")) {
			order = order.replaceFirst("-", StringUtils.EMPTY);
			sortOrder = SortOrder.DESC;
		}

		SortUserField sortType = SortUserField.getEnum(order);

		if (sortType != null) {
			nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(
					sortType.getFieldName()).order(sortOrder));
		} else {
			nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(
					UserDocument.FIELD_USERNAME).order(sortOrder));
		}

		return nativeSearchQueryBuilder;
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

	private enum SortUserField {
		// @formatter:off
		USERNAME("username", UserDocument.FIELD_USERNAME), EMAIL("email",
				UserDocument.FIELD_EMAIL), REGISTRATION_DATE("registrationDate",
				UserDocument.FIELD_CREATION_DATE), STATUS("status", UserDocument.FIELD_STATUS);
		// @formatter:on

		private final String fieldName;
		private final String name;

		private SortUserField(String name, String fieldName) {
			this.fieldName = fieldName;
			this.name = name;
		}

		public static SortUserField getEnum(String value) {
			if (value != null) {
				for (SortUserField type : values()) {
					if (type.getName().equals(value)) {
						return type;
					}
				}
			}
			return null;
		}

		public String getFieldName() {
			return fieldName;
		}

		public String getName() {
			return name;
		}
	}

	protected BoolQueryBuilder buildCriteriaQueryForUsername(String criteria) {
		return boolQuery().must(matchQuery(UserDocument.FIELD_USERNAME, criteria));
	}

	protected BoolQueryBuilder buildCriteriaQueryForEmail(String criteria) {
		return boolQuery()
				.should(matchPhraseQuery(UserDocument.FIELD_EMAIL, criteria));
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