package com.ebay.park.service.item.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.Arrays;

/**
 * 
 * @author marcos.lambolay
 */
@JsonInclude(Include.NON_NULL)
public class UpdateItemRequest extends UserItemRequest {

	private String description;
	private String location; //foursquare id
	private String locationName; //foursquare Name
	private String name;
	private String price;
	private Long categoryId;
	private String[] groups;
	private String latitude;
	private String longitude;
	private boolean shareOnFacebook;
	private boolean shareOnTwitter;
	private String zipCode;
	private boolean feedWhenItemBanned;

	public UpdateItemRequest() {
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String[] getGroups() {
		return groups;
	}

	public void setGroups(String[] groups) {
		this.groups = groups;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String lattitude) {
		this.latitude = lattitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}


	/**
	 * @return the locationName
	 */
	public String getLocationName() {
		return locationName;
	}

	/**
	 * @param locationName the locationName to set
	 */
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public boolean getShareOnFacebook() {
		return shareOnFacebook;
	}

	public void setShareOnFacebook(boolean shareOnFacebook) {
		this.shareOnFacebook = shareOnFacebook;
	}

	public boolean getShareOnTwitter() {
		return shareOnTwitter;
	}

	public void setShareOnTwitter(boolean shareOnTwitter) {
		this.shareOnTwitter = shareOnTwitter;
	}
	
	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UpdateItemRequest [description=").append(description)
				.append(", location=").append(location)
				.append(", locationName=").append(locationName)
				.append(", name=").append(name).append(", price=")
				.append(price).append(", categoryId=").append(categoryId)
				.append(", groups=").append(Arrays.toString(groups))
				.append(", latitude=").append(latitude).append(", longitude=")
				.append(longitude).append(", zipCode=").append(zipCode).append("]");
		return builder.toString();
	}

    /**
     * @return the feedWhenItemBanned
     */
    public boolean isFeedWhenItemBanned() {
        return feedWhenItemBanned;
    }

    /**
     * @param feedWhenItemBanned the feedWhenItemBanned to set
     */
    public void setFeedWhenItemBanned(boolean feedWhenItemBanned) {
        this.feedWhenItemBanned = feedWhenItemBanned;
    }

}
