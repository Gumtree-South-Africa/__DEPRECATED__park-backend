package com.ebay.park.service.moderationMode.command;

import static com.ebay.park.service.ServiceException.createServiceException;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.StatusDescription;
import com.ebay.park.elasticsearch.document.ItemDocument;
import com.ebay.park.elasticsearch.document.converter.DocumentConverter;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.moderationMode.ModerationCacheHelper;
import com.ebay.park.service.moderationMode.dto.ApplyFiltersForModerationModeRequest;
import com.ebay.park.service.moderationMode.dto.ApplyFiltersForModerationModeResponse;

/**
 * Command for applying filters and search items of pending moderation.
 * 
 * @author Julieta Salvadó
 *
 */
@Component
public class ApplyFiltersForModerationModeCmdImpl implements ApplyFiltersForModerationModeCmd {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApplyFiltersForModerationModeCmdImpl.class);
	private static final int FIRST_PAGE = 0;

	/**
	 * Default page size for the returned list of items.
	 */
	@Value("${moderationMode.itemFilterList.pageSize}")
	private int defaultPageSize;

	@Autowired
	@Qualifier("elasticsearchTemplate")
	private ElasticsearchOperations elasticsearchOperations;

	@Autowired
	private DocumentConverter documentConverter;

	/**
	 * Helper to use the locked user memcached.
	 */
	@Autowired
	private ModerationCacheHelper moderationCacheHelper;

	public ApplyFiltersForModerationModeResponse execute(ApplyFiltersForModerationModeRequest request)
			throws ServiceException {
		try {

			// Unlocks user from this moderator
			moderationCacheHelper.unlockUser(request.getToken());

			// Searches
			return filterItems(request);
		} catch (Exception e) {
			LOGGER.error("Searching items for moderation. Request: " + request.toString(), e);
			throw createServiceException(ServiceExceptionCode.MODERATION_SEARCH_ITEM_ERROR);
		}

	}

	/**
	 * Filters and searches items the fulfill the request.
	 *
	 * @param request
	 *            request parameters
	 * @return search results
	 */
	private ApplyFiltersForModerationModeResponse filterItems(ApplyFiltersForModerationModeRequest request) {
		SearchQuery searchQuery = createRequestQuery(request);
		List<ItemDocument> itemsDocumentList = new ArrayList<ItemDocument>();
		List<Item> itemsList = new ArrayList<Item>();

		itemsDocumentList = elasticsearchOperations.queryForList(searchQuery, ItemDocument.class);
		itemsList = documentConverter.fromItemDocument(itemsDocumentList);

		ArrayList<Long> itemsId = new ArrayList<Long>(itemsList.size());

		for (Item item : itemsList) {
			itemsId.add(item.getId());
		}
		ApplyFiltersForModerationModeResponse response = new ApplyFiltersForModerationModeResponse(itemsId,
				(int) elasticsearchOperations.count(searchQuery));

		return response;

	}

	/**
	 * Creates the query.
	 * @param request
	 *            request parameters
	 * @return build query
	 */
	SearchQuery createRequestQuery(ApplyFiltersForModerationModeRequest request) {
		BoolQueryBuilder mainBuilder = boolQuery();

		NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
		RangeQueryBuilder dateQueryBuilder = rangeQuery(ItemDocument.FIELD_LAST_MODIFICATION);

		// only active items
		mainBuilder.must(matchQuery(ItemDocument.FIELD_STATUS, StatusDescription.ACTIVE.toString().toLowerCase()));

		// only item from users that are not locked by a moderator
		ArrayList<Long> lockedUsersList = moderationCacheHelper.getLockedUsers();
		if (!CollectionUtils.isEmpty(lockedUsersList)) {
			lockedUsersList.stream()
					.forEach(user -> mainBuilder.mustNot(matchQuery(ItemDocument.FIELD_PUBLISHER_ID, user)));
		}

		// only pending moderation items
		mainBuilder.must(matchQuery(ItemDocument.FIELD_PENDING_MODERATION, true));

		// only not skipped items
		List<Long> skippedItems = request.getSkippedItems();
		if (!CollectionUtils.isEmpty(skippedItems)) {
			skippedItems.stream().forEach(item -> mainBuilder.mustNot(matchQuery(ItemDocument.FIELD_ID, item)));
		}

		// only items in the selected category
		if (request.getCategoryId() != null) {
			mainBuilder.must(matchQuery(ItemDocument.FIELD_CATEGORY_ID, request.getCategoryId()));
		}

		//only items with incoming description
		if(request.getDescription() != null){
			mainBuilder.must(matchQuery(ItemDocument.FIELD_DESCRIPTION, request.getDescription()));
		}

		// only items from username
		if (request.getUserName() != null) {
			mainBuilder.must(matchQuery(ItemDocument.FIELD_PUBLISHER_NAME, request.getUserName()));
		}

		//only items updated after the selected date
		if (request.getItemLastUpdatedFrom() != null || request.getItemLastUpdatedTo() != null) {
		    if (request.getItemLastUpdatedFrom() != null) {
		        dateQueryBuilder.from(request.getItemLastUpdatedFrom());
		    }
		    if (request.getItemLastUpdatedTo() != null) {
                dateQueryBuilder.to(request.getItemLastUpdatedTo());
            }
            mainBuilder.must(dateQueryBuilder);
		}

		//only items from the selected zipcodes
		if (!CollectionUtils.isEmpty(request.getZipCodes())) {
//		    request.getZipCodes().stream()
//		        .forEach(zipcode -> filters.add(QueryBuilders.termFilter(
//		                ItemDocument.FIELD_ZIPCODE, zipcode.toString())));
			request.getZipCodes().stream()
					.forEach(zipcode -> addZipCodeFilter(mainBuilder, zipcode.toString()));
		}
		
		//only items with incoming title
		if(request.getName() != null){
			mainBuilder.must(matchQuery(ItemDocument.FIELD_NAME, request.getName()));
		}
		
		if (mainBuilder.hasClauses()) {
//		    nativeSearchQueryBuilder.withQuery(QueryBuilders.filteredQuery(mainBuilder,
//		            FilterBuilders.orFilter(filters.toArray(new FilterBuilder[filters.size()]))));
			nativeSearchQueryBuilder.withQuery(mainBuilder);
		}

		nativeSearchQueryBuilder = createSort(nativeSearchQueryBuilder);

		SearchQuery searchQuery = nativeSearchQueryBuilder.withPageable(new PageRequest(FIRST_PAGE, defaultPageSize))
				.build();

		LOGGER.debug("Item Search Query: {}", searchQuery.getQuery());
		LOGGER.debug("Item Search Sort: {}", searchQuery.getSort());

		return searchQuery;
	}

	private BoolQueryBuilder addZipCodeFilter(BoolQueryBuilder mainBuilder, String zipcode) {
		return mainBuilder.filter(QueryBuilders.matchQuery(ItemDocument.FIELD_ZIPCODE, zipcode));
	}

	/**
	 * Creates the search sort.
	 *
	 * @param nativeSearchQueryBuilder
	 *            search query
	 * @return search query with sort definition
	 */
	private NativeSearchQueryBuilder createSort(NativeSearchQueryBuilder nativeSearchQueryBuilder) {
		return nativeSearchQueryBuilder
				.withSort(SortBuilders.fieldSort(ItemDocument.FIELD_LAST_MODIFICATION).order(SortOrder.DESC));
	}
}
