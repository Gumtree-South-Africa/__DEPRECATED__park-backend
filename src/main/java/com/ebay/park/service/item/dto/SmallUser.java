package com.ebay.park.service.item.dto;

import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.User;

public class SmallUser {

	private Long id;
	private String username;
	private String profilePicture;

	/**
	 * Only used for integration testing purposes.
	 */
	public SmallUser() {
		super();
	}

	private String locationName;

	public SmallUser(Item item) {
		this(item.getPublishedBy());
	}

	public SmallUser(User user) {
		super();
		if (user != null) {
			this.username = user.getUsername();
			this.profilePicture = user.getPicture();
			this.id = user.getId();
			this.locationName = user.getLocationName();
		}
	}

	public String getProfilePicture() {
		return profilePicture;
	}

	protected void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}

	public Long getId() {
		return id;
	}

	protected void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	protected void setUsername(String username) {
		this.username = username;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

}
