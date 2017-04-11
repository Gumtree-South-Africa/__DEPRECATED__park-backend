package com.ebay.park.service.conversation.dto;

import com.ebay.park.service.ParkRequest;

/**
 * 
 * @author marcos.lambolay
 */
public class GetConversationRequest extends ParkRequest {

	private final String conversationId;
	private final String lastRequest;

	public GetConversationRequest(String token, String language, String conversationId, String lastRequest) {
		super(token, language);
		this.conversationId = conversationId;
		this.lastRequest = lastRequest;
	}

	public String getConversationId() {
		return conversationId;
	}


	public String getLastRequest() {
		return lastRequest;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GetConversationRequest [conversationId= ")
			.append(this.conversationId).append(", lastRequest=")
			.append(this.lastRequest).append("]");
		
		return builder.toString();
	}

}
