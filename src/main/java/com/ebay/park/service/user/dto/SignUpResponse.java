/**
 * 
 */
package com.ebay.park.service.user.dto;

import java.util.UUID;

/**
 * @author jppizarro
 */
public class SignUpResponse {

	private UUID token;
	private String username;

	public SignUpResponse() {
	}

	public SignUpResponse(String username, UUID parkToken) {
		this.token = parkToken;
		this.username = username;
	}

	public UUID getToken() {
		return token;
	}

	public void setToken(UUID token) {
		this.token = token;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
