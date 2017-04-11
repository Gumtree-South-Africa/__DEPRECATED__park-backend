package com.ebay.park.service.conversation.dto;

import com.ebay.park.service.ParkRequest;

/**
 * 
 * @author marcos.lambolay
 */

public class RejectConversationRequest extends ParkRequest {
	private String conversationId;
	private String explanation;

	public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

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
		builder.append("RejectConversationRequest [conversationId= ")
			.append(this.conversationId).append(", explanation= ")
			.append(this.explanation).append("]");
		
		return builder.toString();
	}
	
}
