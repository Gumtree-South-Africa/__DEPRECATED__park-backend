package com.ebay.park.service.social.dto;

import com.ebay.park.service.ParkRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class UnfollowUserRequest extends ParkRequest{

	private String userToUnfollow;
	
	@JsonIgnore
	private String follower;
	
	public UnfollowUserRequest(){
		
	}


	public String getUserToUnfollow() {
		return userToUnfollow;
	}


	public void setUserToUnfollow(String userToUnfollow) {
		this.userToUnfollow = userToUnfollow;
	}


	public String getFollower() {
		return follower;
	}

	public void setFollower(String follower) {
		this.follower = follower;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UnfollowUserRequest [")
			.append("userToUnfollow= ").append(this.userToUnfollow)
			.append(", follower= ").append(this.follower)
			.append("]");
		
		return builder.toString();
	}
	
}
