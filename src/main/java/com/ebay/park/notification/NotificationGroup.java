/**
 * Copyright eBay, 2014
 */
package com.ebay.park.notification;

/**
 * @author diana.gazquez
 *
 */
public enum NotificationGroup {
	MY_PUBLICATION("notification.group.action.my_publication"),
	NEGOTIATION_CHAT("notification.group.negociation"),
	GENERAL("notification.group.action.general");
	
	private final String messageKey;

	private NotificationGroup(String messageKey) {
		this.messageKey = messageKey;
	}

	/**
	 * @return the messageKey
	 */
	public String getMessageKey() {
		return messageKey;
	}

}
