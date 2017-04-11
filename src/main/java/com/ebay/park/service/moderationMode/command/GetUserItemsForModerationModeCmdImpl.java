package com.ebay.park.service.moderationMode.command;

import static com.ebay.park.service.ServiceException.createServiceException;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.StatusDescription;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.item.ItemUtils;
import com.ebay.park.service.moderationMode.dto.GetUserItemsForModerationModeRequest;
import com.ebay.park.service.moderationMode.dto.GetUserItemsForModerationModeResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Component
public class GetUserItemsForModerationModeCmdImpl implements
		GetUserItemsForModerationModeCmd {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private ItemDao itemDao;

	/**
	 * Default page size for the result list.
	 */
	@Value("${moderationMode.itemList.pageSize}")
	private int defaultPageSize;

	@Autowired
	private ItemUtils itemUtils;

	@Override
	public GetUserItemsForModerationModeResponse execute(
			GetUserItemsForModerationModeRequest request) throws ServiceException {
		GetUserItemsForModerationModeResponse response = new GetUserItemsForModerationModeResponse();
		User user = userDao.findOne(request.getUserId());

		if (user == null) {
			throw createServiceException(ServiceExceptionCode.USER_NOT_FOUND);
		}

		StatusDescription[] itemsInStatus = {StatusDescription.ACTIVE};	

		if (request.getPage() != null || request.getPageSize() != null) {
            int page = request.getPage() != null ? request.getPage() : 0;
            int pageSize = request.getPageSize() != null  ? request.getPageSize() : defaultPageSize;

			Page<Item> itemsPage = itemDao.listItemsFromUserId(
				request.getUserId(), request.getItemIdExcluded(), itemsInStatus, constructPageSpecification(
						page, pageSize));

			response.setItems(itemUtils.convertToItemSummaryForModeration(itemsPage.getContent(),
					request.getLanguage()));
			response.setTotalPages(itemsPage.getTotalPages());
			response.setTotalOfItems(itemsPage.getTotalElements());
			response.setItemsOnPage(itemsPage.getNumberOfElements());
		} else {
			List<Item> items = itemDao.listItemsFromUserId(request.getUserId(),
			        request.getItemIdExcluded(), itemsInStatus);

			response.setItems(itemUtils.convertToItemSummaryForModeration(items, request.getLanguage()));
			response.setTotalPages(0);
			response.setTotalOfItems(items.size());
			response.setItemsOnPage(items.size());
		}

		return response;
	}

	/**
	 * Creates a page setting to return the items in the given page.
	 *
	 * @param startIndex
	 * @param pageSize
	 * @return page settings
	 */
	private Pageable constructPageSpecification(int startIndex, int pageSize) {
		return new PageRequest(startIndex, pageSize,
			new Sort(Sort.Direction.DESC, "lastModificationDate"));
	}
}
