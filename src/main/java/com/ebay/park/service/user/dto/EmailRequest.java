package com.ebay.park.service.user.dto;

import com.ebay.park.service.ParkRequest;
import org.springframework.util.Assert;

/**
 * Email related request.
 * 
 * @author marcos.lambolay
 */
public class EmailRequest extends ParkRequest {

	private String email;
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		Assert.notNull(email, "Email address must be not null");
		this.email = email;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("EmailRequest [email=").append(email).append("]");
		return builder.toString();
	}
}
