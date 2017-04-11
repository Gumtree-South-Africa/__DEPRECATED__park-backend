package com.ebay.park.service.conversation.dto;

import com.ebay.park.service.ParkRequest;

/**
 * 
 * @author marcos.lambolay
 */
public class AcceptConversationRequest extends ParkRequest {
	private String conversationId;

	public String getConversationId() {
		return conversationId;
	}

	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AcceptConversationRequest [conversationId= ")
			.append(this.conversationId).append("]");
		
		return builder.toString();
	}
	
}
