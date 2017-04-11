package com.ebay.park.service.social.dto;

import com.ebay.park.service.ParkRequest;

public class GetFollowersRequest extends ParkRequest {

	public GetFollowersRequest(String username, String token, String language) {
		super(token, language);
		this.username = username;
	}

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
		builder.append("GetFollowersRequest [username= ")
			.append(this.username).append("]");
		
		return builder.toString();
	}
}
