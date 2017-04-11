/**
 * 
 */
package com.ebay.park.service.zipcode.dto;


/**
 * @author jppizarro
 *
 */
public class SearchZipCodeRequest {

	private String state;

	public SearchZipCodeRequest(String stateCode) {
		this.state = stateCode;
	}
	
	public String getState() {
		return state;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SearchZipCodeRequest [")
			.append("state= ").append(this.state)
			.append("]");
		
		return builder.toString();
	}
}
