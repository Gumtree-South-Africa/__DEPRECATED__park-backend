package com.ebay.park.notification.factory.item;

import java.util.List;

import com.ebay.park.db.entity.User;
import com.ebay.park.event.user.InterestedUserItemToFollowersEvent;
import com.ebay.park.notification.factory.AbstractNotificationFactory;
import com.ebay.park.notification.factory.NotificationContext;

public class InterestedItemFollowersNotificationFactory extends AbstractNotificationFactory {

	public InterestedItemFollowersNotificationFactory(NotificationContext context) {
		super(context);
	}

	@Override
	public List<User> getRecipients() {
		InterestedUserItemToFollowersEvent event = (InterestedUserItemToFollowersEvent) context.getNotifiableResult();
		return event.getRecipients();
	}
}
