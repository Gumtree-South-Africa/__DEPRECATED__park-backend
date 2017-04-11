package com.ebay.park.service.profile.dto;

import com.ebay.park.service.ParkRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Represents the basic profile information to be updated for a user.
 * 
 * @author lucia.masola
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfoRequest extends ParkRequest {

	public UserInfoRequest() {
	}

	@JsonIgnore
	private String username;

	private String mobile;

	private String picture;

	private String location;

	private String locationName;
	
	private String zipCode;

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
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
		builder.append("UserInfoRequest [token= ")
				.append(this.token)
				.append(", lang=")
				.append(this.language)
				.append(", username=")
				.append(this.username)
				.append(", location=")
				.append(this.location)
				.append(", locationName=")
				.append(this.locationName)
				.append(", mobile=")
				.append(this.mobile)
				.append(", picture=")
				.append(this.picture)
				.append(", zipCode=")
				.append(this.zipCode)
				.append("]");

		return builder.toString();
	}
}
