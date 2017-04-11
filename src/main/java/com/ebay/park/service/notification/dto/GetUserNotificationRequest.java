/**
 * Copyright eBay, 2014
 */
package com.ebay.park.service.notification.dto;

import com.ebay.park.service.ParkRequest;

/**
 * @author diana.gazquez
 *
 */
public class GetUserNotificationRequest extends ParkRequest {

	private String username;

	GetUserNotificationRequest() {
		super();
	}

	public GetUserNotificationRequest(String token, String lang, String username) {
		super(token, lang);
		this.username = username;
	}


	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
}
