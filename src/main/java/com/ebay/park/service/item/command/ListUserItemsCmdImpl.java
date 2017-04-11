package com.ebay.park.service.item.command;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.StatusDescription;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.item.ItemUtils;
import com.ebay.park.service.item.dto.ItemSummary;
import com.ebay.park.service.item.dto.ListUserItemsRequest;
import com.ebay.park.service.item.dto.ListUserItemsResponse;
import com.ebay.park.util.DataCommonUtil;
import com.ebay.park.util.GenericBuilder;
import com.ebay.park.util.InternationalizationUtil;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Command to return user's item, if there are paging information it returns
 * them in a paged way. If there is an item exclusion, the defined items will not
 * be part of the results.
 * 
 * @ListUserItemsResponse.items: list of items
 * @ListUserItemsResponse.totalPages: number of pages with items
 * @ListUserItemsResponse.itemsOnPage: number of items in the current page
 * @ListUserItemsResponse.totalOfItems: number of user's items
 * @author cbirge
 *
 */
@Component
public class ListUserItemsCmdImpl implements ListUserItemsCmd {
	
	@Autowired
	ItemDao itemDao;

	@Value("${search.pageSize}")
	private int defaultPageSize;

	@Autowired
	private InternationalizationUtil i18nUtil;
	
	@Autowired
	private ItemUtils itemUtils;
	
	@Autowired
	private UserDao userDao;

	@Override
	public ListUserItemsResponse execute(ListUserItemsRequest request)
			throws ServiceException {
	    List<ItemSummary> itemList;
	    int totalPages = 0, itemsOnPage;
	    long totalItems;

	    User user = null;
        if (!StringUtils.isBlank(request.getToken())) {
            user = userDao.findByToken(request.getToken());
        }

		StatusDescription[] itemsInStatus;
		if (request.isOwnItems()) {
			itemsInStatus = StatusDescription.visibleStatusForOwner;
		} else {
			itemsInStatus = StatusDescription.visibleStatusForOther;
		}

		if (request.getPage() != null || request.getPageSize() != null) {
		    int page = request.getPage() != null ? request.getPage() : 0;
		    int pageSize = request.getPageSize() != null  ? request.getPageSize() : defaultPageSize;

			Page<Item> itemsPage;

			if(request.getRequestTime() != null && request.getItemIdExcluded() == null) {
				itemsPage = itemDao.listItemsFromUser(
					request.getUsername(),
					DataCommonUtil.parseUnixTime(request.getRequestTime()),
					itemsInStatus,
					constructPageSpecification(page, pageSize));
			} else if (request.getRequestTime() == null && request.getItemIdExcluded() == null) {
				itemsPage = itemDao.listItemsFromUser(
						request.getUsername(),
						itemsInStatus,
						constructPageSpecification(page, pageSize));
			} else if (request.getRequestTime() != null) {
			    itemsPage = itemDao.listItemsFromUser(
	                    request.getUsername(),
	                    DataCommonUtil.parseUnixTime(request.getRequestTime()),
	                    itemsInStatus,
	                    constructPageSpecification(page, pageSize),
	                    request.getItemIdExcluded());
			} else {
			    itemsPage = itemDao.listItemsFromUser(
                        request.getUsername(),
                        itemsInStatus,
                        constructPageSpecification(page, pageSize),
                        request.getItemIdExcluded());
			}

			itemList = getResponseList(user, request, itemsPage.getContent());

			totalPages = itemsPage.getTotalPages();
			itemsOnPage = itemsPage.getNumberOfElements();
			totalItems = itemsPage.getTotalElements();

		} else {
		    //without pagination
		    List<Item> items;

		    if (request.getItemIdExcluded() != null) {
		        //all items but one
		        items = itemDao.listItemsFromUser(
		                request.getUsername(),
	                    itemsInStatus,
	                    request.getItemIdExcluded());
		    } else {
		        //all items
		        items = itemDao.listItemsFromUser(
		                request.getUsername(),
		                itemsInStatus);
		    }
		    itemList = getResponseList(user, request, items);
		    itemsOnPage = items.size();
		    totalItems = Long.valueOf(items.size());
		}

		return GenericBuilder.of(() -> new ListUserItemsResponse(
                DataCommonUtil.getDateTimeAsUnixFormat(DateTime.now().toDate())))
        .with(ListUserItemsResponse::setItems, itemList)
        .with(ListUserItemsResponse::setTotalPages, totalPages)
        .with(ListUserItemsResponse::setItemsOnPage, itemsOnPage)
        .with(ListUserItemsResponse::setTotalOfItems, totalItems)
        .build();
	}

	private List<ItemSummary> getResponseList(User user, ListUserItemsRequest request,
            List<Item> items) {
	    if (user == null) {
    	    return itemUtils.convertToItemSummary(
                    i18nUtil.internationalizeItems(items, request),
                    request.getLanguage());
	    } else {
	        return itemUtils.convertToItemSummary(
                    i18nUtil.internationalizeItems(items, request), user,
                    request.getLanguage());
	    }

    }

    /**
	 * Creates a page setting to return the items in the given page
	 * 
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	private Pageable constructPageSpecification(int startIndex, int pageSize) {
		Pageable pageSpecification = new PageRequest(startIndex, pageSize,
				sortByPublishDateDesc());
		return pageSpecification;
	}

	/**
	 * Returns a Sort object which sorts items in descending order by using the
	 * publish date.
	 * 
	 * @return
	 */
	private Sort sortByPublishDateDesc() {
		return new Sort(Sort.Direction.DESC, "published");
	}
}
