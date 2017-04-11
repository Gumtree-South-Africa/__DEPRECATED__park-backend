package com.ebay.park.service.rating.dto;

import com.ebay.park.service.PaginatedRequest;

public class ListRatingsRequest extends PaginatedRequest {

	private String username;
	private String role;

	public ListRatingsRequest(Integer page, Integer pageSize, String token, String language) {
		super(token, language ,page, pageSize);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
