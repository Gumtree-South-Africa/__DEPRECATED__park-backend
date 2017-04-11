package com.ebay.park.notification.factory.follow;

import com.ebay.park.db.entity.User;
import com.ebay.park.event.user.UserItemToFollowersEvent;
import com.ebay.park.notification.factory.AbstractNotificationFactory;
import com.ebay.park.notification.factory.NotificationContext;

import java.util.ArrayList;
import java.util.List;

public class ItemUserNotificationFactory extends AbstractNotificationFactory {

	public ItemUserNotificationFactory(NotificationContext context) {
		super(context);
	}

	@Override
	public List<User> getRecipients() {
		List<User> recipients = new ArrayList<User>();
		UserItemToFollowersEvent event = (UserItemToFollowersEvent) context.getNotifiableResult();
		recipients.add(event.getItem().getPublishedBy());
		return recipients;
	}
}