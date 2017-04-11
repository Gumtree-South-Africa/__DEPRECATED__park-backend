package com.ebay.park.service.item.command;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.dao.UserReportItemDao;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.User;
import com.ebay.park.event.user.UserItemToFollowersEvent;
import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.aop.Notifiable;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.item.dto.UserItemRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.ebay.park.service.ServiceException.createServiceException;

/**
 * 
 * @author marcos.lambolay
 */
@Component("removeItemCmd")
public class RemoveItemCmd extends UserItemCmd<UserItemRequest, UserItemToFollowersEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(RemoveItemCmd.class);

	@Autowired
	private ItemDao itemDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserReportItemDao userReportItemDao; 

	@Override
	@Notifiable(action = NotificationAction.DELETE_AN_ITEM)
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public UserItemToFollowersEvent execute(UserItemRequest request) throws ServiceException {

		User user = userDao.findByToken(request.getToken());
		Item item = getItemUser(request);

		if (item == null) {
			throw createServiceException(ServiceExceptionCode.ITEM_NOT_FOUND);
		}

		if (item.isDeleted()) {
			throw createServiceException(ServiceExceptionCode.ITEM_ALREADY_DELETED);
		}
		if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Attempting to delete item id: {}", request.getItemId());
		}
		try {
			//delete reports on the item
			userReportItemDao.deleteByItem(item.getId());
			
			//delete the item and its relations
			item.delete();
			itemDao.save(item);

			if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Delete item id: {}successful", request.getItemId());
			}
			return new UserItemToFollowersEvent(item, user, null);
		} catch (Exception e) {
            LOGGER.error("Error deleting item [{}]", request.getItemId(), e);
			throw createServiceException(ServiceExceptionCode.ITEM_DELETION_ERROR);
		}
	}
}