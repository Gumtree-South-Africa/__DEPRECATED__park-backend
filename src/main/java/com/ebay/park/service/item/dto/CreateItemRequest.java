package com.ebay.park.service.item.dto;

import com.ebay.park.service.ParkRequest;

import java.util.Arrays;

/**
 * 
 * @author marcos.lambolay
 */
public class CreateItemRequest extends ParkRequest {

	private String brandPublish;
	private String description;
	private String location; //foursquare id
	private String locationName; //foursquare Name
	private String name;
	private String price;
	private String versionPublish;
	private boolean shareOnFacebook;
	private boolean shareOnTwitter;
	private Long categoryId;
	private String[] groups;
	private Double latitude;
	private Double longitude;
	private String zipCode;

	public CreateItemRequest() {
	}

	public String getBrandPublish() {
		return brandPublish;
	}

	public void setBrandPublish(String brandPublish) {
		this.brandPublish = brandPublish;
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

	public String getVersionPublish() {
		return versionPublish;
	}

	public void setVersionPublish(String versionPublish) {
		this.versionPublish = versionPublish;
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

	public String[] getGroups() {
		return groups;
	}

	public void setGroups(String[] groups) {
		this.groups = groups;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CreateItemRequest [brandPublish=").append(brandPublish)
				.append(", description=").append(description)
				.append(", location=").append(location)
				.append(", locationName=").append(locationName)
				.append(", name=").append(name).append(", price=")
				.append(price).append(", versionPublish=")
				.append(versionPublish).append(", shareOnFacebook=")
				.append(shareOnFacebook).append(", shareOnTwitter=")
				.append(shareOnTwitter).append(", categoryId=")
				.append(categoryId).append(", groups=")
				.append(Arrays.toString(groups)).append(", latitude=")
				.append(latitude).append(", longitude=").append(longitude)
				.append(", zipCode=").append(zipCode).append("]");
		return builder.toString();
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

}
