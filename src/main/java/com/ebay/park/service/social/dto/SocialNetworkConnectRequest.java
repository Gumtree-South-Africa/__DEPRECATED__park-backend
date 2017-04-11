/*
 * Copyright eBay, 2014
 */
package com.ebay.park.service.social.dto;

import com.ebay.park.service.ParkRequest;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author jpizarro
 * 
 */
public class SocialNetworkConnectRequest extends ParkRequest {

	@JsonProperty(value = "social_network")
	private String socialNetwork;
	
	@JsonProperty(value = "social_token")
	private String socialToken;
	
	@JsonProperty(value = "social_token_secret")
	private String socialTokenSecret;
	
	@JsonProperty(value = "social_user_id")
	private String socialUserId;
	
	private String userName;

    public String getSocialNetwork() {
		return socialNetwork;
	}

	public void setSocialNetwork(String socialNetwork) {
		this.socialNetwork = socialNetwork;
	}

	public String getSocialToken() {
		return socialToken;
	}

	public void setSocialToken(String socialToken) {
		this.socialToken = socialToken;
	}

	public String getSocialUserId() {
		return socialUserId;
	}

	public void setSocialUserId(String socialUserId) {
		this.socialUserId = socialUserId;
	}
	public String getUserName() {
	    return userName;
	}

	public void setUserName(String userName) {
	    this.userName = userName;
	}

	public String getSocialTokenSecret() {
		return socialTokenSecret;
	}

	public void setSocialTokenSecret(String socialTokenSecret) {
		this.socialTokenSecret = socialTokenSecret;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SocialNetworkConnectRequest [socialNetwork=").append(socialNetwork).append(", socialToken=")
				.append(socialToken).append(", socialTokenSecret=").append(socialTokenSecret).append(", socialUserId=")
				.append(socialUserId).append(", userName=").append(userName).append("]");
		return builder.toString();
	}

}
