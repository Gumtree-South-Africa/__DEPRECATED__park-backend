package com.ebay.park.service.conversation.dto;

import com.ebay.park.service.PaginatedRequest;

/**
 * 
 * @author marcos.lambolay
 */
public class ListConversationsRequest extends PaginatedRequest {

	private String lastRequest;
	private String role;

	public ListConversationsRequest(String token, String language) {
		super(token, language);
	}

	public String getLastRequest() {
		return lastRequest;
	}

	public void setLastRequest(String lastRequest) {
	    //Android sends -1 when the conversations is starting
		this.lastRequest = lastRequest.equals("-1") ? String.valueOf(System.currentTimeMillis()) : lastRequest;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	@Override
    public String getLanguage() {
		return language;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ListConversationsRequest [page= ")
			.append(this.getPage()).append(", pageSize= ")
			.append(this.getPageSize()).append(", requestTime= ")
			.append(this.getRequestTime()).append(", lastRequest= ")
			.append(this.lastRequest).append(", role= ")
			.append(this.role).append("]");
		
		return builder.toString();
	}

}
