/*
 * Copyright eBay, 2014
 */
package com.ebay.park.service.social.dto;

import com.ebay.park.service.ParkRequest;

/**
 * @author federico.jaite
 * 
 */
public class ListFacebookFriendsRequest extends ParkRequest {

	private String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ListFacebookFriendsRequest [username=").append(username).append("]");
		return builder.toString();
	}
}
