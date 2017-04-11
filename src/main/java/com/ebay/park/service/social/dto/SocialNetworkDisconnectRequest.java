/*
 * Copyright eBay, 2014
 */
package com.ebay.park.service.social.dto;

import com.ebay.park.service.ParkRequest;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author federico.jaite
 * 
 */
public class SocialNetworkDisconnectRequest extends ParkRequest {

	@JsonProperty(value = "social_network")
	private String socialNetwork;
	
	private String username;

    public String getSocialNetwork() {
		return socialNetwork;
	}

	public void setSocialNetwork(String socialNetwork) {
		this.socialNetwork = socialNetwork;
	}

	public String getUsername() {
	    return username;
	}

	public void setUsername(String username) {
	    this.username = username;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SocialNetworkDisconnectRequest [")
			.append("socialNetwork= ").append(this.socialNetwork)
			.append(", username= ").append(this.username)
			.append("]");
		
		return builder.toString();
	}

	
}
