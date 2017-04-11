package com.ebay.park.service.moderation.dto;

import com.ebay.park.service.PaginatedRequest;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class SearchItemForModerationRequest extends PaginatedRequest {

	private String username;
	private String description;
	private String name;

	private String filterType;
	private String order;
	private boolean exactMatch;

	public SearchItemForModerationRequest(Integer page, Integer pageSize) {
		super(page, pageSize); 
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFilterType() {
		return filterType;
	}

	public void setFilterType(String filterType) {
		this.filterType = filterType;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
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
			   append("name: ").append(getName()).append(" - ").
			   append("description: ").append(getDescription()).append(" - ").
			   append("filter: ").append(getFilterType()).append(" - ").
			   append("order: ").append(getOrder()).append(" - ").
			   append("exactMatch: ").append(isExactMatch());
		return result.toString();
	// @formatter:on
	}

	private String integerToString(Integer integer) {
		return (integer != null) ? integer.toString() : "NULL";
	}
}