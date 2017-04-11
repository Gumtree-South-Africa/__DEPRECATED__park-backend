package com.ebay.park.service.item.dto;

import java.util.List;

import com.ebay.park.service.category.dto.SmallCategory;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * @author marcos.lambolay
 */
public class GetItemResponse {

	private Long id;
	private String description;
	private String location;
	private String locationName;
	private String price;
	private String name;
	private SmallCategory category;
	private List<String> pictures;
	private List<SmallGroupDTO> groups;
	private Long totalOfFollowers;
	private String status;
	private String published;
	private SmallUser user;
	private Double latitude;
	private Double longitude;
	private boolean hasConversations;
	private String localizedStatus;
	private String zipCode;
	private String URL;
	
	@JsonInclude(Include.NON_NULL)
	private Boolean reported;
	
	@JsonInclude(Include.NON_NULL)
	private Boolean followedByUser;
	

	public GetItemResponse() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the groups
	 */
	public List<SmallGroupDTO> getGroups() {
		return groups;
	}

	/**
	 * @param groups
	 *            the groups to set
	 */
	public void setGroups(List<SmallGroupDTO> groups) {
		this.groups = groups;
	}

	/**
	 * @return the totalOfFollowers
	 */
	public Long getTotalOfFollowers() {
		return totalOfFollowers;
	}

	/**
	 * @param totalOfFollowers
	 *            the totalOfFollowers to set
	 */
	public void setTotalOfFollowers(Long totalOfFollowers) {
		this.totalOfFollowers = totalOfFollowers;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @return the price
	 */
	public String getPrice() {
		return price;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	public List<String> getPictures() {
		return pictures;
	}

	public void setPictures(List<String> picturesUrl) {
		this.pictures = picturesUrl;
	}

	public SmallUser getUser() {
		return user;
	}

	public void setUser(SmallUser user) {
		this.user = user;
	}

	public SmallCategory getCategory() {
		return category;
	}

	public void setCategory(SmallCategory category) {
		this.category = category;
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

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getPublished() {
		return published;
	}

	public void setPublished(String published) {
		this.published = published;
	}

	public Boolean getReported() {
		return reported;
	}

	public void setReported(Boolean reported) {
		this.reported = reported;
	}
	
	public Boolean getFollowedByUser() {
		return followedByUser;
	}
	
	public void setFollowedByUser(Boolean followedByUser) {
		this.followedByUser = followedByUser;
	}


	/**
	 * @return the hasConversations
	 */
	public boolean isHasConversations() {
		return hasConversations;
	}

	/**
	 * @param hasConversations the hasConversations to set
	 */
	public void setHasConversations(boolean hasConversations) {
		this.hasConversations = hasConversations;
	}

	/**
	 * @return the localizedStatus
	 */
	public String getLocalizedStatus() {
		return localizedStatus;
	}

	/**
	 * @param localizedStatus the localizedStatus to set
	 */
	public void setLocalizedStatus(String localizedStatus) {
		this.localizedStatus = localizedStatus;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public void setURL(String URL) {
		this.URL = URL;		
	}
	
	public String getURL() {
		return this.URL;
	}

	
}
