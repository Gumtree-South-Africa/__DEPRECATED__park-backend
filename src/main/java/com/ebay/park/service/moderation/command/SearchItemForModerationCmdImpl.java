package com.ebay.park.service.moderation.command;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.StatusDescription;
import com.ebay.park.elasticsearch.document.ItemDocument;
import com.ebay.park.elasticsearch.document.converter.DocumentConverter;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.moderation.ModerationItemFilterType;
import com.ebay.park.service.moderation.dto.ModerationItemSummary;
import com.ebay.park.service.moderation.dto.SearchItemForModerationRequest;
import com.ebay.park.service.moderation.dto.SearchItemForModerationResponse;
import com.ebay.park.util.TextUtils;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.ebay.park.service.ServiceException.createServiceException;
import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * This class is intended to return the list of items filtering by {@link FilterType}
 */
@Component
public class SearchItemForModerationCmdImpl implements
		SearchItemForModerationCmd {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SearchItemForModerationCmdImpl.class);
	private static final String FIELD_NGRAM = ItemDocument.FIELD_DESCRIPTION + "."
            + ItemDocument.FIELD_SUFFIX_NGRAMS;
	private static final String FIELD_SHINGLES = ItemDocument.FIELD_DESCRIPTION + "."
            + ItemDocument.FIELD_SUFFIX_SHINGLES;

	@Autowired
	ItemDao itemDao;

	@Value("${search.pageSize}")
	private int defaultPageSize;

	@Autowired
	@Qualifier("elasticsearchTemplate")
	private ElasticsearchOperations elasticsearchOperations;

	@Autowired
	private DocumentConverter documentConverter;

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
	private TextUtils textUtils;

	@Override
	public SearchItemForModerationResponse execute(
			SearchItemForModerationRequest request) {
		try {

			SearchQuery searchQuery = createRequestQuery(request);
			List<ItemDocument> itemsDocumentList = new ArrayList<ItemDocument>();
			List<Item> itemsList = new ArrayList<Item>();

			itemsDocumentList = elasticsearchOperations.queryForList(
					searchQuery, ItemDocument.class);
			itemsList = documentConverter.fromItemDocument(itemsDocumentList);

			ArrayList<ModerationItemSummary> itemsDTO = new ArrayList<ModerationItemSummary>(
					itemsList.size());
			ModerationItemSummary dto;
			for (Item item : itemsList) {
				 dto = ModerationItemSummary.fromItem(item);
				 dto.setURL(textUtils.createItemSEOURL(item.getCategory().getName(),
							item.getName(), item.getId()));
				itemsDTO.add(dto);
			}
			SearchItemForModerationResponse searchItemResponse = new SearchItemForModerationResponse(
					itemsDTO, (int) elasticsearchOperations.count(searchQuery));

			return searchItemResponse;

		} catch (Exception e) {
            LOGGER.error("Searching items for moderation. Request: {}", request.toString(), e);
			throw createServiceException(ServiceExceptionCode.MODERATION_SEARCH_ITEM_ERROR);
		}

	}

	SearchQuery createRequestQuery(SearchItemForModerationRequest request) {

		int pageSize = calculatePageSize(request.getPageSize());
		int pageIndex = calculatePageIndex(request.getPage(), pageSize);

		BoolQueryBuilder mainBuilder = boolQuery();
		NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
		RangeQueryBuilder reportQueryBuilder = rangeQuery(ItemDocument.FIELD_COUNT_OF_REPORTS);

		try {
			ModerationItemFilterType filter = ModerationItemFilterType
					.getEnum(request.getFilterType());

			if (filter != null) {
				if (filter.equals(ModerationItemFilterType.FLAGGED)) {
					reportQueryBuilder.from(1);
					mainBuilder.must(reportQueryBuilder);
				} else {
					mainBuilder
							.must(matchQuery(
									ItemDocument.FIELD_STATUS,
									filter.equals(ModerationItemFilterType.BLACKLISTED) ? StatusDescription.PENDING
											: filter));
				}
			} else {
				mainBuilder.mustNot(matchQuery(ItemDocument.FIELD_STATUS,
						StatusDescription.IMAGE_PENDING.toString()
								.toLowerCase()));
			}
			if (!StringUtils.isEmpty(request.getName())) {
				if (request.isExactMatch()) {
//					String name = request.getName().replaceAll("\\W","");
					mainBuilder.must(buildCriteriaQueryForName(request.getName()));
				} else {
					mainBuilder.must(QueryBuilders.matchAllQuery());
				}
			}
			if (!StringUtils.isEmpty(request.getDescription())) {
//				String description = request.getDescription().replaceAll("\\W","");
				if (request.isExactMatch()) {
					mainBuilder.must(buildCriteriaQueryForDescription(request.getDescription()));
				} else {
					mainBuilder.must(QueryBuilders.matchAllQuery());
				}
			}
			if (!StringUtils.isEmpty(request.getUsername())) {
				if (request.isExactMatch()) {
					mainBuilder.must(buildCriteriaQueryForPublisher(request
							.getUsername()));
				} else {
					mainBuilder.must(QueryBuilders.matchAllQuery());
				}
			}
		} catch (Exception e) {
		}

		if (mainBuilder.hasClauses()) {
			nativeSearchQueryBuilder.withQuery(mainBuilder);
		}
		nativeSearchQueryBuilder = createSort(nativeSearchQueryBuilder, request);

		SearchQuery searchQuery = nativeSearchQueryBuilder.withPageable(
				new PageRequest(pageIndex, pageSize)).build();

		LOGGER.debug("Item Search Query: {}", searchQuery.getQuery());
		LOGGER.debug("Item Search Sort: {}", searchQuery.getSort());

		return searchQuery;
	}

	private NativeSearchQueryBuilder createSort(
			NativeSearchQueryBuilder nativeSearchQueryBuilder,
			SearchItemForModerationRequest request) {

		ModerationItemFilterType filter = ModerationItemFilterType
				.getEnum(request.getFilterType());

		String order = request.getOrder();
		SortOrder sortOrder = SortOrder.DESC;
		if (order != null && order.startsWith("-")) {
			order = order.replaceFirst("-", StringUtils.EMPTY);
		} else if (order != null && !order.startsWith("-")){
			sortOrder = SortOrder.ASC;
		}

		if (filter != null) {
			if (filter.equals(ModerationItemFilterType.FLAGGED)) {
				nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(
						ItemDocument.FIELD_COUNT_OF_REPORTS).order(sortOrder));
			} else {
				nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(
						"published").order(sortOrder));
			}
		}

		SortItemField sortType = SortItemField.getEnum(order);
		if (sortType != null) {
			nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(
					sortType.getFieldName()).order(sortOrder));
		} else {
			nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(
					ItemDocument.FIELD_LAST_MODIFICATION).order(sortOrder));
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

	private enum SortItemField {
		// @formatter:off
		NAME("name", ItemDocument.FIELD_NAME), DESCRIPTION("description",
				ItemDocument.FIELD_DESCRIPTION), USERNAME("username",
				"publishedBy.username"), PUBLISH_DATE("publishDate",
				ItemDocument.FIELD_PUBLISHED), STATUS("status", "status"),
				LAST_MODIFICATION_NAME("lastModificationDate" , ItemDocument.FIELD_LAST_MODIFICATION);
		// @formatter:on

		private final String fieldName;
		private final String name;

		private SortItemField(String name, String fieldName) {
			this.fieldName = fieldName;
			this.name = name;
		}

		public static SortItemField getEnum(String value) {
			if (value != null) {
				for (SortItemField type : values()) {
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

	protected BoolQueryBuilder buildCriteriaQueryForName(String criteria) {
		return boolQuery().should(
				boolQuery().must(
						buildFuzzyQuery(ItemDocument.FIELD_NAME, criteria))// full-text
																			// search
						.should(matchQuery(
								ItemDocument.FIELD_NAME + "."
										+ ItemDocument.FIELD_SUFFIX_SHINGLES,
								criteria).boost(shinglesBooster)))// increase
																	// relevance
				.should(matchQuery(
						ItemDocument.FIELD_NAME + "."
								+ ItemDocument.FIELD_SUFFIX_NGRAMS, criteria)
						.boost(ngramBooster).minimumShouldMatch(
								ngramMinimumMatch));
	}

	protected BoolQueryBuilder buildCriteriaQueryForDescription(String criteria) {
		return boolQuery()
				.should(boolQuery()
						.must(buildFuzzyQuery(ItemDocument.FIELD_DESCRIPTION, criteria))// full-text search
						.should(matchQuery(FIELD_SHINGLES, criteria).boost(shinglesBooster))
				)// increase relevance
				.should(matchQuery(FIELD_NGRAM,
						criteria).boost(ngramBooster).minimumShouldMatch(ngramMinimumMatch)
				);
	}

	protected BoolQueryBuilder buildCriteriaQueryForPublisher(String criteria) {
		return boolQuery().must(
				matchQuery(ItemDocument.FIELD_PUBLISHER_NAME, criteria));
	}

	protected MatchQueryBuilder buildFuzzyQuery(String field, String criteria) {

		return matchQuery(field, criteria)
				.fuzziness(Fuzziness.build(fuzziness))
				.fuzzyTranspositions(fuzzyTranspositions)
				.maxExpansions(maxExpansions).prefixLength(prefixLength)
				.minimumShouldMatch(fuzzyMinimumMatch);
	}

}
