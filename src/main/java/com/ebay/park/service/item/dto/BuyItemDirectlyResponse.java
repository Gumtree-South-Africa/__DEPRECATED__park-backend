package com.ebay.park.service.item.dto;


/**
 * 
 * @author cbirge
 * 
 */
public class BuyItemDirectlyResponse {

	private String conversationId;

	public BuyItemDirectlyResponse(Long conversationId) {
		super();
		this.conversationId = "" + conversationId;
	}

	public String getConversationId() {
		return conversationId;
	}

	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}
}
