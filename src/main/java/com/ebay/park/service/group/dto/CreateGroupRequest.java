package com.ebay.park.service.group.dto;

import com.ebay.park.service.ParkRequest;

/**
 * 
 * @author federico.jaite
 */
public class CreateGroupRequest extends ParkRequest {

	private String name;
	private String location; // latitude,longitude
	private String locationName;
	private String zipCode;
	private String description;

	public CreateGroupRequest() {
	}

	public String getName() {
		return name;
	}

	public String getLocation() {
		return location;
	}

	/**
	 * @return the locationName
	 */
	public String getLocationName() {
		return locationName;
	}

	/**
	 * @return the zipcode
	 */
	public String getZipCode() {
		return zipCode;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CreateGroupRequest [name= ")
			.append(this.name).append(", location= ")
			.append(this.location).append(", locationName= ")
			.append(this.locationName).append(", zipCode= ")
			.append(this.zipCode).append(", description= ")
			.append(this.description).append("]");
		
		return builder.toString();
	}

}
