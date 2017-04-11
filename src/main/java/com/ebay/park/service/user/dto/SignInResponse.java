/*
 * Copyright eBay, 2014
 */
package com.ebay.park.service.user.dto;



/**
 * @author juan.pizarro
 */
public class SignInResponse  {

	private String username;
	private String token;
	private String profilePicture;

	//FIXME the three fields should be always set, but it needs a big refactor in cases when none value wants to be retrieved
	public SignInResponse() {
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public String getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SignInResponse [username=").append(this.getUsername()).append(", profilePicture=")
		.append(this.getProfilePicture()).append(", token=").append(this.getToken()).append("]");
		return builder.toString();
	}
}
