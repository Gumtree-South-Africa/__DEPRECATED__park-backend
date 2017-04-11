package com.ebay.park.service.notification.dto;


import com.ebay.park.notification.NotificationAction;
import com.ebay.park.service.ParkRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;
import java.util.SortedMap;


public class NotificationConfigRequest extends ParkRequest {

	private String username;
	private Map<String, SortedMap<NotificationAction,ActionConfigurationDTO>> userConfigurationPerGroup;

	public NotificationConfigRequest() {
	}

	public NotificationConfigRequest(String username,
			Map<String, SortedMap<NotificationAction,ActionConfigurationDTO>>  userConfigurationPerGroup) {
		super();
		this.username = username;
		this.userConfigurationPerGroup = userConfigurationPerGroup;
	}

	@JsonIgnore
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the userConfigurationPerGroup
	 */
	public Map<String, SortedMap<NotificationAction,ActionConfigurationDTO>>  getUserConfigurationPerGroup() {
		return userConfigurationPerGroup;
	}
	
}
