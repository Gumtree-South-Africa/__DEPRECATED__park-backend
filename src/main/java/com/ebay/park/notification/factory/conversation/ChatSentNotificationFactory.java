package com.ebay.park.notification.factory.conversation;

import com.ebay.park.db.entity.User;
import com.ebay.park.event.conversation.ChatSentEvent;
import com.ebay.park.notification.factory.AbstractNotificationFactory;
import com.ebay.park.notification.factory.NotificationContext;

import java.util.ArrayList;
import java.util.List;

public class ChatSentNotificationFactory extends AbstractNotificationFactory {

	public ChatSentNotificationFactory(NotificationContext context) {
		super(context);
	}

	@Override
	public List<User> getRecipients() {
		List<User> recipients = new ArrayList<User>();
		ChatSentEvent event = (ChatSentEvent) context.getNotifiableResult();
		recipients.add(context.findUserById(event.getReceiverId()));
		return recipients;
	}
}
