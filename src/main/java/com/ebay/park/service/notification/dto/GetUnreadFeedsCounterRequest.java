package com.ebay.park.service.notification.dto;

import com.ebay.park.service.ParkRequest;

public class GetUnreadFeedsCounterRequest extends ParkRequest {
	
	public GetUnreadFeedsCounterRequest(String token, String language) {
		super(token, language);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GetUnreadFeedsCounterRequest [token= ")
			.append(this.token).append("]");
		
		return builder.toString();
	}
}

