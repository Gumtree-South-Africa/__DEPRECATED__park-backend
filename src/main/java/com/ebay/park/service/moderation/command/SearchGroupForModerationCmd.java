package com.ebay.park.service.moderation.command;

import com.ebay.park.db.entity.Group;
import com.ebay.park.elasticsearch.document.GroupDocument;
import com.ebay.park.elasticsearch.document.converter.DocumentConverter;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.moderation.dto.GroupForModerationDTO;
import com.ebay.park.service.moderation.dto.SearchGroupForModerationRequest;
import com.ebay.park.service.moderation.dto.SearchGroupForModerationResponse;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.ebay.park.service.ServiceException.createServiceException;
import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Command for searching groups according to a set of criteria.
 * @author diana.gazquez | Julieta Salvadó
 */
@Component
public class SearchGroupForModerationCmd implements
		ServiceCommand<SearchGroupForModerationRequest, SearchGroupForModerationResponse> {

	private static Logger logger = LoggerFactory.getLogger(SearchGroupForModerationRequest.class);

	@Value("${search.pageSize}")
	private int defaultPageSize;

	@Autowired
	@Qualifier("elasticsearchTemplate")
	private ElasticsearchOperations elasticsearchOperations;

	@Autowired
	private DocumentConverter documentConverter;

	@Override
	public SearchGroupForModerationResponse execute(
			SearchGroupForModerationRequest request) throws ServiceException {
		try {
			SearchQuery searchQuery = createRequestQuery(request);

			List<GroupDocument> documents = elasticsearchOperations.queryForList(
					searchQuery,
					GroupDocument.class);
			List<Group> groups = documentConverter.fromGroupDocument(documents);

			List<GroupForModerationDTO> listDTO = new ArrayList<GroupForModerationDTO>(
					groups.size());

			for (Group group : groups) {
				GroupForModerationDTO smallGroup = GroupForModerationDTO.fromGroup(group);
				listDTO.add(smallGroup);
			}

			return new SearchGroupForModerationResponse(
					listDTO,
					(int) elasticsearchOperations.count(searchQuery));

		} catch (Exception e) {
			throw createServiceException(ServiceExceptionCode.SEARCH_TERM_IS_NOT_SPECIFIC_ENOUGH);
		}
	}

	protected SearchQuery createRequestQuery(SearchGroupForModerationRequest request) {
		BoolQueryBuilder mainBuilder = boolQuery();
		NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();

		if (StringUtils.isNotEmpty(request.getName())) {
			mainBuilder.must(addNameFilter(request.getName()));
		}
		if (StringUtils.isNotEmpty(request.getCreatorName())) {
			mainBuilder.must(addCreatorFilter(request.getCreatorName()));
		}
		if (mainBuilder.hasClauses()) {
			nativeSearchQueryBuilder.withQuery(mainBuilder);
		}

		SearchQuery searchQuery = nativeSearchQueryBuilder
				.withPageable(addPageable(request))
				.build();

		logger.debug("Group Search Sort: {}", searchQuery.getSort());
		logger.debug("Group Search Query: {}", searchQuery.getQuery());
		return searchQuery;
	}

	protected TermQueryBuilder addCreatorFilter(String creatorName) {
		return termQuery("creator.username", creatorName.toLowerCase());
	}

	protected MatchQueryBuilder addNameFilter(String name) {
		return matchQuery("groupName", name);
	}

	protected Pageable addPageable(SearchGroupForModerationRequest request) {
		int pageSize = calculatePageSize(request.getPageSize());
		int pageIndex = calculatePageIndex(request.getPage());
		return new PageRequest(pageIndex, pageSize);
	}

	private int calculatePageIndex(Integer page) {
		return page == null || page < 0 ? 0 : page;
	}

	private int calculatePageSize(Integer pageSize) {
		return pageSize == null || pageSize <= 0 ? defaultPageSize : pageSize;
	}
}