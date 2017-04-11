package com.ebay.park.service.group.dto;

import com.ebay.park.service.PaginatedRequest;

/**
 * 
 * @author marcos.lambolay
 */
public class ListGroupsRequest extends PaginatedRequest {

	private String username;
	
	public ListGroupsRequest(Integer page, Integer pageSize, String token, String language, String username) {
		super(token, language, page, pageSize);
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ListGroupsRequest [username= ")
			.append(this.username).append(", page= ")
			.append(this.getPage()).append(", pageSize= ")
			.append(this.getPageSize()).append(", requestTime= ")
			.append(this.getRequestTime()).append("]");
			
	return builder.toString();
	}
}
