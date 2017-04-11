package com.ebay.park.notification.factory.user;

import com.ebay.park.notification.dto.NotificationMessage;
import com.ebay.park.notification.factory.NotificationContext;
import com.ebay.park.notification.factory.strategies.NotificationFactoryEmailStrategy;

import java.util.Arrays;
import java.util.List;

public class UserDirectNotificationFactoryEmailStrategy extends NotificationFactoryEmailStrategy{

	public UserDirectNotificationFactoryEmailStrategy(NotificationContext context) {
		super(context);
	}

	@Override
	public List<NotificationMessage> createNotifications() {

		NotificationMessage mailNotificationMessage = getEmailNotBuilder()
										.build();

		return Arrays.asList(mailNotificationMessage);
	}
}
