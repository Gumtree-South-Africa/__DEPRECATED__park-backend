package com.ebay.park.service.moderation.dto;

import com.ebay.park.service.ParkRequest;

public class AdminSignInRequest extends ParkRequest {

	private String username;
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AdminSignInRequest [username= ")
			.append(this.username).append("]");
			
	return builder.toString();
	}

}
