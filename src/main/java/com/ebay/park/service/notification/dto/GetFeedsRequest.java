package com.ebay.park.service.notification.dto;


import com.ebay.park.service.ParkRequest;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


public class GetFeedsRequest extends ParkRequest {

	protected String username;

	public GetFeedsRequest() {
	}

	public GetFeedsRequest(String username, String token, String language) {
		super(token, language);
		this.username = username;
	}


	public String getUsername() {
		return username;
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}




}
