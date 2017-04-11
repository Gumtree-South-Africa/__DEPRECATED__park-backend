package com.ebay.park.service.city.dto;

/**
 * 
 * @author scalderon
 *
 */
public class SearchCityRequest {
	
	private String state;

	public SearchCityRequest(String stateCode) {
		this.state = stateCode;
	}

	public String getState() {
		return state;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SearchCityRequest [state= ")
			.append(this.state).append("]");
		
		return builder.toString();
	}

}
