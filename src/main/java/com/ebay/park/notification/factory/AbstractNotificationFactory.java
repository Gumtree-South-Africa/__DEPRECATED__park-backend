/*
 * Copyright eBay, 2014
 */
package com.ebay.park.notification.factory;

import com.ebay.park.db.entity.Device;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserSession;
import com.ebay.park.notification.NotificationType;
import com.ebay.park.notification.aop.NotificationClassifier;
import com.ebay.park.notification.dto.NotifiableServiceResult;
import com.ebay.park.notification.dto.NotificationMessage;
import com.ebay.park.notification.factory.strategies.NotificationFactoryEmailStrategy;
import com.ebay.park.notification.factory.strategies.NotificationFactoryFeedStrategy;
import com.ebay.park.notification.factory.strategies.NotificationFactoryPushStrategy;
import com.ebay.park.service.device.dto.DeviceDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract Class in charge of creating the notifications. The factory must contain the list of valid types, 
 * associated with a strategy @see NotificationFactoryStrategy.
 * @author jpizarro
 * 
 */
public abstract class AbstractNotificationFactory implements NotificationFactory, Serializable {

	protected AbstractNotificationFactory(NotificationContext context) {
		this.context = context;
		putIntoNotificationFactories(NotificationType.PUSH, new NotificationFactoryPushStrategy(context));
		putIntoNotificationFactories(NotificationType.FEED, new NotificationFactoryFeedStrategy(context));
		putIntoNotificationFactories(NotificationType.EMAIL, new NotificationFactoryEmailStrategy(context));
	}
	
	private NotificationClassifier notificationClassifier = new NotificationClassifier();
	protected NotificationContext context;

	/**
	 * Iterates through all recipients. It uses the {@link NotificationClassifier} to filter in what way we need to notify
	 * the user @see NotificationType. Each strategy associated with the type, must contructs the notifications.
	 */
	@Override
	public List<NotificationMessage> createNotifications() {
		List<User> recipients = getRecipients();
		Device device;

		List<NotificationMessage> allNotificationMessages = new ArrayList<NotificationMessage>();
		for (User user : recipients) {
			for(NotificationType type : notificationClassifier.classify(context.getNotificationAction(), user)) {
				//push
				if(NotificationType.PUSH.equals(type)) {
					for (UserSession session : user.getUserSessions()) {
						device = session.getDevice();
						if (device != null && session.isSessionActive() && user.isNotificationEnabled(
								context.getNotificationAction(), type)){
							allNotificationMessages.addAll(createNotification(type, user, device));
						}
					}
				} else {
					//feed and email
				    List<NotificationMessage> notificationMessageList = createNotification(type, user);

				    if (NotificationType.EMAIL.equals(type)) {
				        for (NotificationMessage notificationMessage : notificationMessageList) {
				            notificationMessage.setProperty(NotifiableServiceResult.RECIPIENT, user.getUsername());
				        }
				    }
					allNotificationMessages.addAll(notificationMessageList);
				}
				
			}
		}
		return allNotificationMessages;
	}
	
	private List<NotificationMessage> createNotification(NotificationType type, User user, Device device) {
		NotificationFactoryStrategy strategy = this.getStrategy(type);
		DeviceDTO deviceDTO = new DeviceDTO(device);
		device.setPlatform(device.getPlatform());
		// TODO make a new instance of context if this changes to multithread tasking
		context.setNotificationType(type);
		context.setRecipient(deviceDTO, user);

		return strategy.createNotifications();
	}
	
	private List<NotificationMessage> createNotification(NotificationType type, User user) {
		NotificationFactoryStrategy strategy = this.getStrategy(type);
		
		// TODO make a new instance of context if this changes to multithread tasking
		context.setNotificationType(type);
		context.setRecipient(user);
		
		return strategy.createNotifications();
	}

	/**
	 * Returns the users to be notify.
	 * @return list of user to be notified
	 */
	public abstract List<User> getRecipients();

	private Map<NotificationType, NotificationFactoryStrategy> notificationFactories = 
		new HashMap<NotificationType, NotificationFactoryStrategy>();
	
	protected void putIntoNotificationFactories(NotificationType type, NotificationFactoryStrategy strategy) {
		notificationFactories.put(type, strategy);
	}
	
	public NotificationFactoryStrategy getStrategy(NotificationType type) {
		return notificationFactories.get(type);
	}
}
