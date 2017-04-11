package com.ebay.park.notification.factory.strategies;

import com.ebay.park.notification.dto.NotifiableServiceResult;
import com.ebay.park.notification.dto.NotificationMessage;
import com.ebay.park.notification.dto.PushNotificationMessage.PushNotificationBuilder;
import com.ebay.park.notification.factory.NotificationContext;
import com.ebay.park.notification.factory.NotificationFactoryStrategy;

import java.util.Arrays;
import java.util.List;

/**
 * Class to be extended by strategies that aim to create push notification. The push builder is defined with the common 
 * fields used in all push notifications.
 * @author lucia.masola
 *
 */
public class NotificationFactoryPushStrategy implements NotificationFactoryStrategy{

	protected NotificationContext context;

	public NotificationFactoryPushStrategy(NotificationContext notificationContext) {
		this.context = notificationContext; 
	}
	
	/**
	* Returns a PushNotificationBuilder with the basic attributes a push notification must contain.
	* @return a single PushNotificationBuilder
	*/
	public PushNotificationBuilder getPushNotBuilder(){
		
		return new PushNotificationBuilder(
				context.getNotificationAction(),
				context.getNotificationType(),
				context.getDevice(),
				context.getTemplateMsg(),
				context.getBadge());
	}
	
	@Override
	public List<NotificationMessage> createNotifications() {
		NotifiableServiceResult eventData = context.getNotifiableResult();
		NotificationMessage pushNotificationMessage = getPushNotBuilder().append(eventData.toMap()).build();

		return Arrays.asList(pushNotificationMessage);
	}
}
