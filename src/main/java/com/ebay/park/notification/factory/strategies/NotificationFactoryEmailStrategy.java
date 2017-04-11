package com.ebay.park.notification.factory.strategies;

import com.ebay.park.notification.dto.MailNotificationMessage.MailNotificationBuilder;
import com.ebay.park.notification.dto.NotifiableServiceResult;
import com.ebay.park.notification.dto.NotificationMessage;
import com.ebay.park.notification.factory.NotificationContext;
import com.ebay.park.notification.factory.NotificationFactoryStrategy;

import java.util.Arrays;
import java.util.List;

/**
 * Class to be extended by strategies that aim to create email notification. The email builder is defined with the common fields
 * used in all email notifications.
 * @author lucia.masola
 *
 */
public class NotificationFactoryEmailStrategy implements NotificationFactoryStrategy{

	protected NotificationContext context;

	public NotificationFactoryEmailStrategy(NotificationContext notificationContext) {
		this.context = notificationContext; 
	}
	
	/**
	 * Returns a MailNotificationBuilder with the basic attributes a mail notification must contain.
	 * @return
	 */
	public MailNotificationBuilder getEmailNotBuilder(){
		return new MailNotificationBuilder(
				context.getNotificationAction(),
				context.getNotificationType(),
				context.getUserRecipient().getEmail(),
				context.getEmailSubject(),
				context.getEmailTemplate(),
				context.getUserRecipient().getUsername());
	}
	
	@Override
	public List<NotificationMessage> createNotifications() {
		NotifiableServiceResult eventData = context.getNotifiableResult();
		NotificationMessage emailNotificationMessage = getEmailNotBuilder().append(eventData.toMap()).build();

		return Arrays.asList(emailNotificationMessage);
	}
}
