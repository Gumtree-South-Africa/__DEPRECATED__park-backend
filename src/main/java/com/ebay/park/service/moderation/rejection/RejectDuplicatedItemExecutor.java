package com.ebay.park.service.moderation.rejection;

import org.springframework.stereotype.Component;

import com.ebay.park.event.item.ItemNotificationEvent;
import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.aop.Notifiable;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.moderation.dto.RejectItemRequest;

/**
 * It manages the item rejection when the reason of rejection is 'duplicated item'.
 * @author Julieta Salvadó
 *
 */
@Component
public class RejectDuplicatedItemExecutor extends RejectItemExecutor {

	@Override
    @Notifiable(action=NotificationAction.ITEM_DELETED_FROM_MODERATION_DUPLICATED)
	public ItemNotificationEvent execute(RejectItemRequest request) throws ServiceException {
		return new ItemNotificationEvent(super.rejectItem(request));
	}
}
