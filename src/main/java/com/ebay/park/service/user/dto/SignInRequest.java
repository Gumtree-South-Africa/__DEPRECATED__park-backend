/*
 * Copyright eBay, 2014
 */
package com.ebay.park.service.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author juan.pizarro
 */
public class SignInRequest extends SignRequest {

	@JsonProperty(value = "fb_token")
	private String fbToken;

	@JsonProperty(value = "fb_user_id")
	private String fbUserId;

	public SignInRequest() {
	}

	public String getFbToken() {
		return fbToken;
	}

	public void setFbToken(String fbToken) {
		this.fbToken = fbToken;
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	public String getFbUserId() {
		return fbUserId;
	}

	public void setFbUserId(String fbUserId) {
		this.fbUserId = fbUserId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SignInRequest [username=").append(this.getUsername()).append(", email=").append(this.getEmail())
				.append(", device=").append(this.getDevice()).append(", fbToken=").append(fbToken).append(", fbUserId=")
				.append(fbUserId).append("]");
		return builder.toString();
	}

}
