package com.ebay.park.service.blacklist.dto;

import com.ebay.park.service.PaginatedRequest;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class SearchBlacklistedWordsRequest extends PaginatedRequest {


	private String order;
	private String description;

	public SearchBlacklistedWordsRequest(Integer page, Integer pageSize, String order, String description) {
		super(page, pageSize);
		this.order = order;
		this.description = description;
	}
	
	public String getOrder() {
		return order;
	}

	public String getDescription() {
		return description;
	}


	// @formatter:off
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("SearchItemForModerationRequest: [");
		result.append("page: ").append(integerToString(getPage())).append(" - ").
			   append("pageSize: ").append(integerToString(getPageSize())).append(" - ").
			   append("description: ").append(getDescription()).append(" - ").
			   append("order: ").append(getOrder()).append(" - ");
		return result.toString();
	// @formatter:on
	}

	private String integerToString(Integer integer) {
		return (integer != null) ? integer.toString() : "NULL";
	}

}
