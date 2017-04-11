package com.ebay.park.service.rating.dto;

import com.ebay.park.service.PaginatedRequest;

public class ListPendingRatingsRequest extends PaginatedRequest{

	private String username;

	public ListPendingRatingsRequest(Integer page, Integer pageSize, String token, String language) {
		super(token, language ,page, pageSize);
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
