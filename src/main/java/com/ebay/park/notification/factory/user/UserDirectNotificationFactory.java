package com.ebay.park.notification.factory.user;

import com.ebay.park.db.entity.User;
import com.ebay.park.event.user.UserEvent;
import com.ebay.park.notification.factory.AbstractNotificationFactory;
import com.ebay.park.notification.factory.NotificationContext;

import java.util.ArrayList;
import java.util.List;

public class UserDirectNotificationFactory extends AbstractNotificationFactory {

	public UserDirectNotificationFactory(NotificationContext context) {
		super(context);
	}

	@Override
	public List<User> getRecipients() {
		List<User> recipients = new ArrayList<User>();
		UserEvent event = (UserEvent) context.getNotifiableResult();
		recipients.add(event.getUser());
		return recipients;
	}
}