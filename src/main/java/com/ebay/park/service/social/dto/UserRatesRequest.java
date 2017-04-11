package com.ebay.park.service.social.dto;

import com.ebay.park.service.ParkRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.Arrays;
import java.util.List;

public class UserRatesRequest  extends ParkRequest{

	@JsonIgnore
	private String username;
	
	private List<String> rateStatus;

	public UserRatesRequest(){
		
	}
	
	public UserRatesRequest(String username, String rateStatus){
		this.username = username;
		if (rateStatus != null){
			String[] statuses = rateStatus.split(",");
			this.rateStatus = Arrays.asList(statuses);
		}
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<String> getRateStatus() {
		return rateStatus;
	}

	public void setRateStatus(List<String> rateStatus) {
		this.rateStatus = rateStatus;
	}
	
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserRatesRequest [")
			.append("username= ").append(this.username)
			.append("]");
		
		return builder.toString();
	}
	
}
