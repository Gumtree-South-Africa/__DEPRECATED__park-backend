package com.ebay.park.service.social.dto;

import com.ebay.park.service.ParkRequest;

public class GetFollowingsRequest extends ParkRequest {

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
		builder.append("GetFollowingsRequest [username= ")
			.append(this.username).append("]");
		
		return builder.toString();
	}

}
