package com.ebay.park.notification.factory.item;

import com.ebay.park.db.entity.User;
import com.ebay.park.event.user.UserItemToFollowersEvent;
import com.ebay.park.notification.factory.AbstractNotificationFactory;
import com.ebay.park.notification.factory.NotificationContext;

import java.util.List;

public class ItemFollowersNotificationFactory extends AbstractNotificationFactory {

	public ItemFollowersNotificationFactory(NotificationContext context) {
		super(context);
	}

	@Override
	public List<User> getRecipients() {
		UserItemToFollowersEvent event = (UserItemToFollowersEvent) context.getNotifiableResult();
		return event.getRecipients();
	}
}
