package com.ebay.park.service.item.dto;

import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.StatusDescription;

public class SmallItem {

	private Long id;
	private String itemName;
	private String picture;
	private SmallUser owner;
	private StatusDescription status;

	/**
	 * Only used for integration testing purposes.
	 */
	public SmallItem() {
		super();
	}

	public SmallItem(Item item) {
		super();
		if (item != null) {
			this.itemName = item.getName();
			this.picture = item.getPicture1Url();
			this.id = item.getId();
			this.owner = new SmallUser(item.getPublishedBy());
			this.status = item.getStatus();
		}
	}
	
	/**
	 * Constructor.
	 * @param name
	 * 			item name
	 * @param picture
	 * 			item picture
	 */
	public SmallItem(String name, String picture) {
		this.itemName = name;
		this.picture = picture;
	}

	public Long getId() {
		return id;
	}


	public String getItemName() {
		return itemName;
	}


	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public SmallUser getOwner() {
		return owner;
	}

	/**
	 * @return the status
	 */
	public StatusDescription getStatus() {
		return status;
	}

}
