package com.ebay.park.service.user.dto;

import com.ebay.park.service.ParkRequest;

/**
 * @author federico.jaite
 * 
 */
public class VerifyEmailRequest extends ParkRequest {

	private String email;
	private String temporaryToken;

	public VerifyEmailRequest(String email, String temporaryToken) {
		super();
		this.email = email;
		this.temporaryToken = temporaryToken;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTemporaryToken() {
		return temporaryToken;
	}

	public void setTemporaryToken(String temporaryToken) {
		this.temporaryToken = temporaryToken;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("VerifyEmailRequest [")
			.append("email= ").append(this.email)
			.append(", temporaryToken= ").append(this.temporaryToken)
			.append("]");
		
		return builder.toString();
	}

}
