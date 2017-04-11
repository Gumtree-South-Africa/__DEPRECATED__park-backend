package com.ebay.park.service.moderation.dto;

import com.ebay.park.service.ParkRequest;

public class UserIdRequest extends ParkRequest {

	private Long userId;

	public UserIdRequest() {
		super();
	}

	public UserIdRequest(Long userId) {
		super();
		this.userId = userId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
