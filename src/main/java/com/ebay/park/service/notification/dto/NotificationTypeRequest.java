package com.ebay.park.service.notification.dto;

import com.ebay.park.notification.NotificationAction;
import com.ebay.park.service.ParkRequest;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class NotificationTypeRequest extends ParkRequest {

	private String username;
	private NotificationAction action;
	
	public NotificationTypeRequest(){
	}
	
	public NotificationTypeRequest(String username, NotificationAction type) {
		super();
		this.username = username;
		this.action = type;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public NotificationAction getAction() {
		return action;
	}

	public void setAction(NotificationAction action) {
		this.action = action;
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
