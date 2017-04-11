/*
 * Copyright eBay, 2014
 */
package com.ebay.park.service.social.dto;

import com.ebay.park.service.ParkRequest;

/**
 * @author federico.jaite
 * 
 */
public class DiscoverUsersRequest extends ParkRequest {

	public DiscoverUsersRequest(String username, Double latitude, Double longitude, Double radius, String token, String language) {
		super(token, language);
		this.username = username;
		this.latitude = latitude;
		this.longitude = longitude;
		this.radius = radius;
	}

	private String username;
	private Double latitude;
	private Double longitude;
	private Double radius;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public Double getRadius() {
		return radius;
	}

	public void setRadius(Double radius) {
		this.radius = radius;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DiscoverUsersRequest [username= ")
			.append(this.username).append(", latitude= ")
			.append(this.latitude).append(", longitude= ")
			.append(this.longitude).append("]");
		
		return builder.toString();
	}

}
