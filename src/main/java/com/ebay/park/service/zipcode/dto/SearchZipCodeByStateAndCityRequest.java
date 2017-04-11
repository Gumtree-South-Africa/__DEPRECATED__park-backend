package com.ebay.park.service.zipcode.dto;

public class SearchZipCodeByStateAndCityRequest {
	
	private String state;
	private String city;

	public SearchZipCodeByStateAndCityRequest(String stateCode, String city) {
		this.state = stateCode;
		this.city = city;
	}
	
	public String getState() {
		return state;
	}
	
	public String getCity() {
		return city;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SearchZipCodeByStateAndCityRequest [")
			.append("state= ").append(this.state)
			.append(", city=").append(this.city)
			.append("]");
		
		return builder.toString();
	}

}
