package com.ebay.park.notification.factory.conversation;

import com.ebay.park.db.entity.User;
import com.ebay.park.event.conversation.ConversationRejectedEvent;
import com.ebay.park.notification.factory.AbstractNotificationFactory;
import com.ebay.park.notification.factory.NotificationContext;

import java.util.ArrayList;
import java.util.List;

public class ConversationRejectedNotificationFactory extends AbstractNotificationFactory {

	public ConversationRejectedNotificationFactory(NotificationContext context) {
		super(context);
	}

	@Override
	public List<User> getRecipients() {
		List<User> recipients = new ArrayList<User>();
		ConversationRejectedEvent event = (ConversationRejectedEvent) context.getNotifiableResult();
		recipients.add(context.findUserById(event.getUserIdToNotify()));
		return recipients;
	}
}
