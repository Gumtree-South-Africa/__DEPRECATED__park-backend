package com.ebay.park.service.user.dto;

import com.ebay.park.service.device.dto.RemoveUserSessionsRequest;

public class RemoveCurrentUserSessionRequest extends RemoveUserSessionsRequest{

	private String currentToken;

	public RemoveCurrentUserSessionRequest(String token) {
		setCurrentToken(token);
	}

	public String getCurrentToken() {
		return currentToken;
	}

	public void setCurrentToken(String currentToken) {
		this.currentToken = currentToken;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RemoveCurrentUserSessionRequest [")
			.append("currentToken= ").append(this.currentToken)
			.append("]");
		
		return builder.toString();
	}

}
