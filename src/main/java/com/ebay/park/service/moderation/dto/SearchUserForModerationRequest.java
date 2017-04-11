package com.ebay.park.service.moderation.dto;

import com.ebay.park.service.PaginatedRequest;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class SearchUserForModerationRequest extends PaginatedRequest {

	private String username;
	private String email;
	private String status;
    private Boolean userVerified;
	private String order;
	private boolean exactMatch;

	public SearchUserForModerationRequest(Integer page, Integer pageSize) {
		super(page, pageSize);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isExactMatch() {
		return exactMatch;
	}

	public void setExactMatch(boolean exactMatch) {
		this.exactMatch = exactMatch;
	}

	// @formatter:off
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("SearchItemForModerationRequest: [");
		result.append("page: ").append(integerToString(getPage())).append(" - ").
			   append("pageSize: ").append(integerToString(getPageSize())).append(" - ").
			   append("username: ").append(getUsername()).append(" - ").
			   append("email: ").append(getEmail()).append(" - ").
			   append("userVerified: ").append(getUserVerified()).append(" - ").
			   append("order: ").append(getOrder()).append(" - ").
			   append("exactMatch: ").append(isExactMatch());
		return result.toString();
	// @formatter:on
	}

	private String integerToString(Integer integer) {
		return (integer != null) ? integer.toString() : "NULL";
	}

	public Boolean getUserVerified() {
		return userVerified;
	}

	public void setUserVerified(Boolean userVerified) {
		this.userVerified = userVerified;
	}
}