package com.ebay.park.service.session.dto;

public class UserSessionUpdaterRequest {

	private UserSessionCache userSessionCache;
	private String token;

	public UserSessionUpdaterRequest(UserSessionCache userSessionCache, String token) {
		setUserSessionCache(userSessionCache);
		setToken(token);
	}
	
	private void setUserSessionCache(UserSessionCache userSessionCache) {
		this.userSessionCache = userSessionCache;
	}

	public UserSessionCache getUserSessionCache() {
		return userSessionCache;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
