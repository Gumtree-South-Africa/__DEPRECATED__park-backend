/*
 * Copyright eBay, 2014
 */
package com.ebay.park.service.social.dto;

import com.ebay.park.service.ParkRequest;

/**
 * @author jpizarro
 * 
 */
public class SocialConnectRequest extends ParkRequest {

	String socialToken;
	String socialId;

	public String getSocialToken() {
		return socialToken;
	}

	public void setSocialToken(String socialToken) {
		this.socialToken = socialToken;
	}

	public String getSocialId() {
		return socialId;
	}

	public void setSocialId(String socialId) {
		this.socialId = socialId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SocialConnectRequest [")
			.append("socialToken= ").append(this.socialToken)
			.append(", socialId= ").append(this.socialId)
			.append("]");
		
		return builder.toString();
	}
}
