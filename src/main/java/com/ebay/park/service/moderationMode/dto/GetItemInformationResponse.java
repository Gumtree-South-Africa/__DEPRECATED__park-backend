package com.ebay.park.service.moderationMode.dto;

import java.util.List;

import com.ebay.park.service.category.dto.SmallCategory;
import com.ebay.park.service.item.dto.SmallGroupDTO;
import com.ebay.park.service.item.dto.SmallUser;

public class GetItemInformationResponse {

	private Long id;
	private String description;
	private String location;
	private String locationName;
	private String price;
	private String name;
	private SmallCategory category;
	private String picture1;
	private String picture2;
	private String picture3;
	private String picture4;
	private List<SmallGroupDTO> groups;
	private Long totalOfFollowers;
	private String lastModificationDate;
	private SmallUser user;
	private String zipCode;
	private boolean isPendingModeration;
	
	public GetItemInformationResponse() {
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
	 * 
	 * @return picture1 url
	 */
	public String getPicture1() {
		return picture1;
	}

	public void setPicture1(String picture1) {
		this.picture1 = picture1;
	}

	/**
	 * 
	 * @return picture2 url
	 */
	public String getPicture2() {
		return picture2;
	}

	public void setPicture2(String picture2) {
		this.picture2 = picture2;
	}

	/**
	 * 
	 * @return picture3 url
	 */
	public String getPicture3() {
		return picture3;
	}

	public void setPicture3(String picture3) {
		this.picture3 = picture3;
	}

	/**
	 * 
	 * @return picture4 url
	 */
	public String getPicture4() {
		return picture4;
	}

	public void setPicture4(String picture4) {
		this.picture4 = picture4;
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

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getLastModificationDate() {
		return lastModificationDate;
	}

	public void setLastModificationDate(String lastModificationDate) {
		this.lastModificationDate = lastModificationDate;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public boolean isPendingModeration() {
		return isPendingModeration;
	}

	public void setPendingModeration(boolean isPendingModeration) {
		this.isPendingModeration = isPendingModeration;
	}

}
