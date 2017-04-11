/*
 * Copyright eBay, 2014
 */
package com.ebay.park.notification.dto;

import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.NotificationDispatcher;
import com.ebay.park.notification.NotificationType;
import com.ebay.park.service.device.dto.DeviceDTO;

import java.util.HashMap;
import java.util.Map;

/**
 * Push notification
 *
 * @author lucia.masola
 * @author gervasio.amy
 */
public class PushNotificationMessage extends NotificationMessage {

	public static final String DEVICE_ID = "deviceId";
	public static final String DEVICE_TYPE = "deviceType";
	public static final String TEMPLATE_MSG = "templateMsg";
	public static final String BADGE = "badge";

    /**
     * To be used from {@link NotificationDispatcher} in the "double dispatching" implementation of deciding where to queue a
     * {@link NotificationMessage}
     *
     * @param notificationDispatcher
     * @return
     */
	@Override
	public NotificationMessage doDispatch(NotificationDispatcher notificationDispatcher) {
		return notificationDispatcher.dispatchPushNotification(this);
	}

	private PushNotificationMessage(PushNotificationBuilder builder) {
		super(builder.action, builder.type, builder.props);
	}

	public String getDeviceId(){
		return props.get(DEVICE_ID);
	}
	
	public String getTemplateMsg(){
		return props.get(TEMPLATE_MSG);
	}
	
	public String getDeviceType(){
		return props.get(DEVICE_TYPE);
	}
	
	public String getBadge() {
		return props.get(BADGE);
	}

	public static class PushNotificationBuilder {
		
		private Map<String, String> props;
		private final NotificationAction action;
		private final NotificationType type;
		public PushNotificationBuilder(NotificationAction action, NotificationType type,
				DeviceDTO device, String templateMsg, String badge){
			this(action, type, templateMsg, badge);

			if(device != null){
				props.put(DEVICE_TYPE, device.getDeviceType());
				props.put(DEVICE_ID, device.getDeviceId());
			}
		}
		
		public PushNotificationBuilder(NotificationAction action, NotificationType type, String templateMsg){
			this.action = action;
			this.type = type;
			this.props = new HashMap<String, String>();
			props.put(TEMPLATE_MSG, templateMsg);
		}

		public PushNotificationBuilder(NotificationAction action, NotificationType type,
				String templateMsg, String badge){
			this.action = action;
			this.type = type;
			this.props = new HashMap<String, String>();
			props.put(TEMPLATE_MSG, templateMsg);
			props.put(BADGE, badge);
		}
		
		public PushNotificationBuilder append(String prop, String value){
			props.put(prop, value);
			return this;
		}
		
		public PushNotificationBuilder append(Map<String, String> propsToAdd){
			props.putAll(propsToAdd);
			return this;
		}
		
		public PushNotificationMessage build(){
			return new PushNotificationMessage(this);
		}	
	}	
}