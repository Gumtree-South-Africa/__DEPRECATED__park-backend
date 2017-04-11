/*
 * Copyright eBay, 2014
 */
package com.ebay.park.service.conversation.dto;

/**
 * @author jpizarro
 * 
 */
public class ListConversationsByItemRequest extends ListConversationsRequest {

	public ListConversationsByItemRequest(Long itemId, String token, String language) {
		super(token, language);
		this.itemId = itemId;
	}

	private Long itemId;


	public Long getItemId() {
		return itemId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ListConversationsByItemRequest [[page= ")
			.append(this.getPage()).append(", pageSize= ")
			.append(this.getPageSize()).append(", requestTime= ")
			.append(this.getRequestTime()).append(", lastRequest= ")
			.append(this.getLastRequest()).append(", role= ")
			.append(this.getRole()).append(", itemId= ")
			.append(this.itemId).append("]");
		
		return builder.toString();
	}

}
