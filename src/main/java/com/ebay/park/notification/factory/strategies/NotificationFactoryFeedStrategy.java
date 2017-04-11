package com.ebay.park.notification.factory.strategies;

import java.util.Arrays;
import java.util.List;

import com.ebay.park.notification.dto.FeedNotificationMessage;
import org.json.JSONObject;

import com.ebay.park.notification.dto.NotifiableServiceResult;
import com.ebay.park.notification.dto.NotificationMessage;
import com.ebay.park.notification.factory.NotificationContext;
import com.ebay.park.notification.factory.NotificationFactoryStrategy;

/**
 * Class to be extended by strategies that aim to create feed notification. The feed builder is defined with the 
 * common fields used in all feed notifications.
 * @author lucia.masola
 *
 */
public class NotificationFactoryFeedStrategy implements NotificationFactoryStrategy{

	protected NotificationContext context;

	public NotificationFactoryFeedStrategy(NotificationContext notificationContext) {
		this.context = notificationContext; 
	}
	
	
	@Override
	public List<NotificationMessage> createNotifications() {

		NotifiableServiceResult eventData = context.getNotifiableResult();
		NotificationMessage feedNotificationMessage = getFeedNotBuilder().setItem(eventData.getItemId())
				.setBasedUser(eventData.getBasedUserId()).build();

		return Arrays.asList(feedNotificationMessage);
	}
	
	/**
	* Returns a FeedNotificationBuilder with the basic attributes a feed notification must contain.
	* @return a single FeedNotificationBuilder
	*/
	public FeedNotificationMessage.FeedNotificationBuilder getFeedNotBuilder(){
		return new FeedNotificationMessage.FeedNotificationBuilder(
				context.getNotificationAction(),
				context.getNotificationType(),
				getFeedProperties(),
				context.getUserRecipient().getId());
	}
	
	protected String getFeedProperties() {
		JSONObject properties = new JSONObject(context.getNotifiableResult().toMap());
		return properties.toString();
	}
	
}
