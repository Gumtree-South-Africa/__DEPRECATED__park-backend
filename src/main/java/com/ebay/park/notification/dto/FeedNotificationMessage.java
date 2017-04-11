/*
 * Copyright eBay, 2014
 */
package com.ebay.park.notification.dto;

import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.NotificationDispatcher;
import com.ebay.park.notification.NotificationType;

import java.util.HashMap;
import java.util.Map;

/**
 * Feed notification.
 *
 * @author lucia.masola
 * @author gervasio.amy
 */
public class FeedNotificationMessage extends NotificationMessage {

	@Override
	public NotificationMessage doDispatch(NotificationDispatcher notificationDispatcher) {
		return notificationDispatcher.dispatchFeedNotification(this);
	}
	
	private FeedNotificationMessage(FeedNotificationBuilder builder){
		super(builder.action, builder.type, builder.props);
	}

	public String getFeedProperties(){
		return props.get("feedProperties");
	}
	
	public Long getUserToNotify(){
		return new Long(props.get("userToNotify"));
	}
	
	public Long getBasedUser(){
		String userId = props.get("basedUser");
		if (userId != null){
			return new Long(userId);
		}
		return null;
	}
	
	public Long getItem(){
		String itemId = props.get("item");
		if (itemId != null){
			return new Long(itemId);
		}
		return null;
	}

	public static class FeedNotificationBuilder{
		
		private Map<String, String> props;
		private final NotificationAction action;
		private final NotificationType type;
		
		public FeedNotificationBuilder(NotificationAction action, NotificationType type, 
										String feedProperty, Long userToNotifyId){
			this.action = action;
			this.type = type;
			this.props = new HashMap<String, String>();
			props.put("feedProperties",feedProperty);
			props.put("userToNotify", userToNotifyId.toString());
		}
		
		public FeedNotificationBuilder append(String prop, String value){
			props.put(prop, value);
			return this;
		}
		
		public FeedNotificationBuilder setBasedUser(Long value){
			if (value != null) {
				props.put("basedUser", value.toString());
			}
			return this;
		}
		
		public FeedNotificationBuilder setItem(Long value){
			if (value != null){
				props.put("item", value.toString());
			}
			return this;
		}
		
		public FeedNotificationMessage build(){
			return new FeedNotificationMessage(this);
		}
		
	}
	
}
