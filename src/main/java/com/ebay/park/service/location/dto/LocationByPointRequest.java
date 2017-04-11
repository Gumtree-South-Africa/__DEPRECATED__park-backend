/*
 * Copyright eBay, 2014
 */
package com.ebay.park.service.location.dto;

/**
 * @author jpizarro
 * 
 */
public class LocationByPointRequest {

	private String location;

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LocationByPointRequest [location= ")
			.append(this.location).append("]");
			
	return builder.toString();
	}

}
