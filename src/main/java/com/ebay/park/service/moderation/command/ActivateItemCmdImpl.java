package com.ebay.park.service.moderation.command;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.StatusDescription;
import com.ebay.park.event.item.ItemNotificationEvent;
import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.aop.Notifiable;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.item.ItemUtils;
import com.ebay.park.service.moderation.dto.ItemRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;

@Component
public class ActivateItemCmdImpl implements ActivateItemCmd {

	private static final Logger LOGGER = LoggerFactory.getLogger(ActivateItemCmdImpl.class);

	@Autowired
	private ItemDao itemDao;
	
	@Autowired
	private ItemUtils itemUtils;


	@Override
	@Notifiable(action = {NotificationAction.ITEM_APROVED })
	public ItemNotificationEvent execute(ItemRequest request)
			throws ServiceException {

		Item item = itemDao.findOne(request.getItemId());

		if (item == null) {
			throw createServiceException(ServiceExceptionCode.ITEM_NOT_FOUND);
		}
		if (item.is(StatusDescription.EXPIRED)) {
			throw createServiceException(ServiceExceptionCode.ITEM_EXPIRED);
		}
		if (!item.getStatus()
				.equals(StatusDescription.PENDING)) {
			throw createServiceException(ServiceExceptionCode.MODERATION_ITEM_NOT_PENDING);
		}

		if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Attempting to Activate item id: {}", request.getItemId());
		}
		try {

			itemUtils.activateItem(item);
			
			if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Activate item id: {}successfull", request.getItemId());
			}
			return new ItemNotificationEvent(item);
		} catch (Exception e) {
            LOGGER.error("Error activating item [{}]", request.getItemId(), e);
			throw createServiceException(ServiceExceptionCode.ITEM_ACTIVATION_ERROR);
		}
	}

}
