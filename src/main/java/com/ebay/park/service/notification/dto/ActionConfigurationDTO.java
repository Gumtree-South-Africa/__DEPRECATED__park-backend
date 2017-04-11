/**
 * Copyright eBay, 2014
 */
package com.ebay.park.service.notification.dto;

import com.ebay.park.notification.NotificationAction;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author diana.gazquez
 *
 */
public class ActionConfigurationDTO {
	

	private String actionDisplayName;
	private NotificationAction notificationAction;
	private ConfigurationValue emailConfig = ConfigurationValue.DISABLED;
	private ConfigurationValue pushConfig = ConfigurationValue.DISABLED;


	public ActionConfigurationDTO(String actionName, NotificationAction notificationAction,
			ConfigurationValue emailConfig, ConfigurationValue pushConfig) {
		super();
		this.actionDisplayName = actionName;
		this.notificationAction = notificationAction;
		this.emailConfig = emailConfig;
		this.pushConfig = pushConfig;
	}
	
	ActionConfigurationDTO() {
		super();
		//FOR JSON PARSER
	}



	/**
	 * @return the emailConfig
	 */
	public ConfigurationValue getEmailConfig() {
		return emailConfig;
	}


	/**
	 * @param emailConfig the emailConfig to set
	 */
	public void setEmailConfig(ConfigurationValue emailConfig) {
		this.emailConfig = emailConfig;
	}


	/**
	 * @return the pushConfig
	 */
	public ConfigurationValue getPushConfig() {
		return pushConfig;
	}


	/**
	 * @param pushConfig the pushConfig to set
	 */
	public void setPushConfig(ConfigurationValue pushConfig) {
		this.pushConfig = pushConfig;
	}

	/**
	 * @return the actionDisplayName
	 */
	public String getActionDisplayName() {
		return actionDisplayName;
	}

	/**
	 * @param actionDisplayName the actionDisplayName to set
	 */
	public void setActionDisplayName(String actionDisplayName) {
		this.actionDisplayName = actionDisplayName;
	}

	/**
	 * @return the notificationAction
	 */
	@JsonIgnore
	public NotificationAction getNotificationAction() {
		return notificationAction;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ActionConfigurationDTO [actionDisplayName=").append(actionDisplayName)
				.append(", notificationAction=").append(notificationAction).append(", emailConfig=")
				.append(emailConfig).append(", pushConfig=").append(pushConfig).append("]");
		return builder.toString();
	}


	
	
}
