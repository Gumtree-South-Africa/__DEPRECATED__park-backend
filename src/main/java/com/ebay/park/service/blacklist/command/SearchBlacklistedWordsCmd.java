package com.ebay.park.service.blacklist.command;

import static com.ebay.park.service.ServiceException.createServiceException;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

import java.util.List;

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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;

import com.ebay.park.db.entity.BlackList;
import com.ebay.park.elasticsearch.document.BlackListDocument;
import com.ebay.park.elasticsearch.document.converter.DocumentConverter;
import com.ebay.park.elasticsearch.repository.BlackListRepository;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.blacklist.dto.SearchBlacklistedWordsRequest;
import com.ebay.park.service.blacklist.dto.SearchBlacklistedWordsResponse;

@Component
public class SearchBlacklistedWordsCmd
		implements
		ServiceCommand<SearchBlacklistedWordsRequest, SearchBlacklistedWordsResponse> {

	private static Logger LOGGER = LoggerFactory
			.getLogger(SearchBlacklistedWordsCmd.class);

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

	@Value("${elasticsearch.ngram.minimum_should_match}")
	String ngramMinimumMatch;

	@Value("${elasticsearch.ngram.booster}")
	Float ngramBooster;

	@Value("${elasticsearch.shingles.booster}")
	Float shinglesBooster;

	@Autowired
	private DocumentConverter documentConverter;

	@Autowired
	BlackListRepository blackListRepository;

	@Override
	public SearchBlacklistedWordsResponse execute(
			SearchBlacklistedWordsRequest request) throws ServiceException {

		SearchBlacklistedWordsResponse searchResponse = new SearchBlacklistedWordsResponse();

		try {

			List<BlackList> words;
			List<BlackListDocument> result;
			SearchQuery searchQuery = createRequestQuery(request);

			LOGGER.debug("BlackList Search Query: {}", searchQuery.getQuery());
			result = blackListRepository.search(searchQuery).getContent();
			words = documentConverter.fromBlackListDocument(result);

			searchResponse.setAmountWordsFound(words.size());
			for (BlackList word : words) {
				searchResponse.addWord(word.getId(), word.getDescription());
			}

			return searchResponse;

		} catch (Exception e) {
            LOGGER.error("Searching blacklist. Request: {}", request.toString(), e);
			throw createServiceException(ServiceExceptionCode.BLACKLIST_SEARCH_ERROR);
		}
	}

	protected SearchQuery createRequestQuery(SearchBlacklistedWordsRequest request) {

		int pageSize = calculatePageSize(request.getPageSize());
		int pageIndex = calculatePageIndex(request.getPage(), pageSize);
		BoolQueryBuilder mainBuilder = boolQuery();
		NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();

		if (StringUtils.isEmpty(request.getDescription())) {
			mainBuilder.must(matchAllQuery());
		}else{
			mainBuilder.must(buildCriteriaQuery(request.getDescription()));
		}

		if (mainBuilder.hasClauses()) {
			nativeSearchQueryBuilder.withQuery(mainBuilder);
		}

		nativeSearchQueryBuilder = createSort(nativeSearchQueryBuilder, request);

		SearchQuery searchQuery;
		if (request.getPage() != null) {
			searchQuery = nativeSearchQueryBuilder.withPageable(
                    new PageRequest(pageIndex, pageSize)).build();
		}else {
			searchQuery = nativeSearchQueryBuilder.build();
		}

		LOGGER.debug("BlackList Search Sort: {}", searchQuery.getElasticsearchSorts());
		LOGGER.debug("BlackList Search Query: {}", searchQuery.getQuery());
		LOGGER.debug("BlackList Search Filter: {}", searchQuery.getFilter());

		return searchQuery;
	}

	private NativeSearchQueryBuilder createSort(
			NativeSearchQueryBuilder nativeSearchQueryBuilder,
			SearchBlacklistedWordsRequest request) {
		String order = request.getOrder();
		SortOrder sortOrder = SortOrder.ASC;

		if (order != null && order.startsWith("-")) {
			sortOrder = SortOrder.DESC;
		}
		nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(
				BlackListDocument.FIELD_DESCRIPTION).order(sortOrder));

		return nativeSearchQueryBuilder;
	}

	private int calculatePageIndex(Integer page, int pageSize) {
		return pageSize * (page != null ? page : 0);
	}

	private int calculatePageSize(Integer pageSize) {
		if (pageSize == null || pageSize <= 0) {
			return defaultPageSize;
		}
		return pageSize;
	}

	protected BoolQueryBuilder buildCriteriaQuery(String criteria) {
		return boolQuery()
				.should(boolQuery()
						.must(buildFuzzyQuery(BlackListDocument.FIELD_DESCRIPTION, criteria))
						.should(matchQuery(BlackListDocument.FIELD_DESCRIPTION + "." + BlackListDocument.FIELD_SUFFIX_SHINGLES, criteria).boost(shinglesBooster)))//full-text search
				.should(matchQuery(BlackListDocument.FIELD_DESCRIPTION + "." + BlackListDocument.FIELD_SUFFIX_NGRAMS, criteria)
						.boost(ngramBooster).minimumShouldMatch(ngramMinimumMatch));//increase relevance
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
