package com.ebay.park.service.social.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;

public class FollowUserResponse {

	private String userToFollow;

	private String follower;

	private Boolean successfull;

	public FollowUserResponse() {

	}

	public FollowUserResponse(FollowUserRequest request, Boolean result) {
		this.userToFollow = request.getUserToFollow();
		this.follower = request.getFollower();
		this.successfull = result;
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

	public Boolean getSuccessfull() {
		return successfull;
	}

	public void setSuccessfull(Boolean successfull) {
		this.successfull = successfull;
	}

	@JsonIgnore
	public String getToken() {
		return null;
	}

	@JsonIgnore
	public String getUsername() {
		return follower;
	}

}
