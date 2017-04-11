package com.ebay.park.service.item.schedule;

import com.ebay.park.db.entity.Item;
import com.ebay.park.event.item.ItemNotificationEvent;
import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.aop.Notifiable;
import org.springframework.stereotype.Component;

@Component
public class InactiveItemsHelper {

	@Notifiable(action = NotificationAction.ITEM_EXPIRE)
	public ItemNotificationEvent notifyExpired(Item item) {
		return new ItemNotificationEvent(item);
	}
	
	@Notifiable(action = NotificationAction.ITEM_ABOUT_TO_EXPIRE)
	public ItemNotificationEvent notifyItemAboutToExpired(Item item) {
		return new ItemNotificationEvent(item);
	}

}
