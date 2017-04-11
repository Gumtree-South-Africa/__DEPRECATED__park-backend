package com.ebay.park.notification.factory.conversation;

import com.ebay.park.db.entity.User;
import com.ebay.park.event.conversation.ConversationAcceptedEvent;
import com.ebay.park.notification.factory.AbstractNotificationFactory;
import com.ebay.park.notification.factory.NotificationContext;

import java.util.ArrayList;
import java.util.List;

public class ConversationAcceptedNotificationFactory extends AbstractNotificationFactory {

	public ConversationAcceptedNotificationFactory(NotificationContext context) {
		super(context);
	}

	@Override
	public List<User> getRecipients() {
		List<User> recipients = new ArrayList<User>();
		ConversationAcceptedEvent event = (ConversationAcceptedEvent) context.getNotifiableResult();
		recipients.add(context.findUserById(event.getUserIdToNotify()));
		return recipients;
	}
}