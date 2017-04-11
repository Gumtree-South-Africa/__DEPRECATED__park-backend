package com.ebay.park.service.social.dto;

import com.ebay.park.service.ParkRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class FollowUserRequest extends ParkRequest{

	private String userToFollow;
	
	@JsonIgnore
	private String follower;
	
	public FollowUserRequest(){
		
	}

	public String getUserToFollow() {
		return userToFollow;
	}

	public void setUserToFollow(String userToFollow) {
		this.userToFollow = userToFollow;
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
		builder.append("FollowUserRequest [userToFollow= ")
			.append(this.userToFollow).append(", follower= ")
			.append(this.follower).append("]");
		
		return builder.toString();
	}
}
