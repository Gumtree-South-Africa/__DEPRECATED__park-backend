package com.ebay.park.service.notification.dto;

import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.NotificationType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class NotificationTemplateRequest {

	private NotificationType notificationType;
	
	private NotificationAction notificationAction;

	public NotificationTemplateRequest() {
	}
	
	public NotificationTemplateRequest(String type, String action) {
		super();
		this.notificationType = NotificationType.fromValue(type);
		this.notificationAction = NotificationAction.fromValue(action);
	}
	
	public NotificationType getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(NotificationType notificationType) {
		this.notificationType = notificationType;
	}

	public NotificationAction getNotificationAction() {
		return notificationAction;
	}

	public void setNotificationAction(NotificationAction notificationAction) {
		this.notificationAction = notificationAction;
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
	
}
