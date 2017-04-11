package com.ebay.park.service.item.command;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.entity.Item;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.item.dto.UserItemRequest;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;
import org.springframework.beans.factory.annotation.Autowired;

import static com.ebay.park.service.ServiceException.createServiceException;

public abstract class UserItemCmd<Z extends UserItemRequest, T> implements
		ServiceCommand<Z, T> {

	@Autowired
	private SessionService sessionService;

	@Autowired
	private ItemDao itemDao;

	/**
	 * It returns an item that must be owned by the requester.
	 * @param request
	 * @return
	 *     the item
	 * @exception ITEM_NOT_FOUND when the item is deleted or cannot be found.
	 * @exception ITEM_DOESNT_BELONG_TO_USER when the item does not belong to the requester.
	 */
	protected Item getItemUser(UserItemRequest request) {
		//TODO this code now is part of UserUtils, delete this abstract class and use that method
	    UserSessionCache userSession = sessionService.getUserSession(request
				.getToken());
		Item item = itemDao.findOne(request.getItemId());
		if (item == null || item.isDeleted()) {
			throw createServiceException(ServiceExceptionCode.ITEM_NOT_FOUND);
		}

		if (!item.getPublishedBy().getUserId().equals(userSession.getUserId())) {
			throw createServiceException(ServiceExceptionCode.ITEM_DOESNT_BELONG_TO_USER);
		}
		return item;
	}
}
