package com.ebay.park.notification.factory.user;

import com.ebay.park.db.entity.User;
import com.ebay.park.event.user.UsersEvent;
import com.ebay.park.notification.factory.AbstractNotificationFactory;
import com.ebay.park.notification.factory.NotificationContext;

import java.util.ArrayList;
import java.util.List;


public class UsersNotificationFactory extends AbstractNotificationFactory {

	public UsersNotificationFactory(NotificationContext context) {
		super(context); 
	}

	@Override
	public List<User> getRecipients() {
		List<User> recipients = new ArrayList<User>();
		UsersEvent event = (UsersEvent) context.getNotifiableResult();
		recipients.add(event.getBasedUser());
		return recipients;
	}
}
