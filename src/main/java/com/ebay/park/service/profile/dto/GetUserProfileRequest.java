package com.ebay.park.service.profile.dto;

import com.ebay.park.service.ParkRequest;

public class GetUserProfileRequest extends ParkRequest {

	private String username;

	public GetUserProfileRequest(String token, String username, String language) {
		this.username = username;
		setToken(token);
		setLanguage(language);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
