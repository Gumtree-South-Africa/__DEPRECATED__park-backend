package com.ebay.park.service.moderation;

import com.ebay.park.db.entity.User;
import com.ebay.park.notification.factory.AbstractNotificationFactory;
import com.ebay.park.notification.factory.NotificationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gabriel.sideri
 */
public class FeedModerationFactory extends AbstractNotificationFactory {

	public FeedModerationFactory(NotificationContext context) {
		super(context);
	}

	@Override
	public List<User> getRecipients() {
		List<User> recipients = new ArrayList<User>();
		FeedModerationEvent event = (FeedModerationEvent) context.getNotifiableResult();
		recipients.addAll(context.findUsersById(event.getRecipients()));
		return recipients;
	}
}