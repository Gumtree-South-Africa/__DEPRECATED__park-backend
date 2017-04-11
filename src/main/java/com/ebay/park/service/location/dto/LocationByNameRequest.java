/*
 * Copyright eBay, 2014
 */
package com.ebay.park.service.location.dto;

import com.ebay.park.service.ParkRequest;

/**
 * @author jpizarro
 * 
 */
public class LocationByNameRequest extends ParkRequest {

	private String keyword;
	private Integer size;
	private Double latitude;
	private Double longitude;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}


	@Override
	public String getToken() {
		return this.token;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	@Override
	public void setLanguage(String language) {
		this.language = language;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LocationByNameRequest [keyword= ")
			.append(this.keyword).append(", size= ")
			.append(this.size).append(", latitude= ")
			.append(this.latitude).append(", longitude= ")
			.append(this.longitude).append("]");
			
	return builder.toString();
	}

}
