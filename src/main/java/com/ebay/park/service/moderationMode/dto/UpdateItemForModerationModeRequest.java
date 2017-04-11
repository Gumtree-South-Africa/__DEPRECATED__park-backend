package com.ebay.park.service.moderationMode.dto;

import com.ebay.park.service.ParkRequest;

/**
 * Request for updating an item from ModTool and labeling it as already
 * moderated.
 * 
 * @author Julieta Salvadó
 *
 */

public class UpdateItemForModerationModeRequest extends ParkRequest {
	private Long itemId;
	private String description;
	private String name;
	private Long category;
	private String picture1;
	private String picture2;
	private String picture3;
	private String picture4;
	private String[] groups;

	public String[] getGroups() {
		return groups;
	}

	public void setGroups(String[] groups) {
		this.groups = groups;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getCategory() {
		return category;
	}

	public void setCategory(Long category) {
		this.category = category;
	}

	public String getPicture1() {
		return picture1;
	}

	public void setPicture1(String picture1) {
		this.picture1 = picture1;
	}

	public String getPicture2() {
		return picture2;
	}

	public void setPicture2(String picture2) {
		this.picture2 = picture2;
	}

	public String getPicture3() {
		return picture3;
	}

	public void setPicture3(String picture3) {
		this.picture3 = picture3;
	}

	public String getPicture4() {
		return picture4;
	}

	public void setPicture4(String picture4) {
		this.picture4 = picture4;
	}

	public UpdateItemForModerationModeRequest() {
		super();
	}

	/**
	 * @return the itemId
	 */
	public Long getItemId() {
		return itemId;
	}

	/**
	 * @param itemId
	 *            the itemId to set
	 */
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

}
