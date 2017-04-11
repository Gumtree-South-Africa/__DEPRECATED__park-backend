package com.ebay.park.notification.factory.item;

import com.ebay.park.db.entity.User;
import com.ebay.park.event.user.NotInterestedUserItemToFollowersEvent;
import com.ebay.park.notification.factory.AbstractNotificationFactory;
import com.ebay.park.notification.factory.NotificationContext;

import java.util.List;

public class NotInterestedItemFollowersNotificationFactory extends AbstractNotificationFactory {

	public NotInterestedItemFollowersNotificationFactory(NotificationContext context) {
		super(context);
	}

	@Override
	public List<User> getRecipients() {
	    NotInterestedUserItemToFollowersEvent event = (NotInterestedUserItemToFollowersEvent) context.getNotifiableResult();
		return event.getRecipients();
	}
}
