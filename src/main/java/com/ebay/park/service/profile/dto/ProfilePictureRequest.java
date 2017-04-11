/*
 * Copyright eBay, 2014
 */
package com.ebay.park.service.profile.dto;

import com.ebay.park.service.ParkRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author jpizarro
 * 
 */
public class ProfilePictureRequest extends ParkRequest {

	@JsonIgnore
	private String username;

	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
