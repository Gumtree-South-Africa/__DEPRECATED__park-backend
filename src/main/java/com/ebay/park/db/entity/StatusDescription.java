package com.ebay.park.db.entity;

/**
 * Convenience enum for describing the known item status.
 * 
 * @author marcos.lambolay
 */
public enum StatusDescription {
	//@formatter:off
	IMAGE_PENDING("IMAGE_PENDING"), // The image is in upload process or upload failed, the item is invisible for users, only moderators can see them
	ACTIVE("ACTIVE"),
	SOLD("SOLD"),
	PENDING("PENDING"), //The item was banned (blacklisted or flagged users), only visible for owners and moderators
	EXPIRED("EXPIRED");
	//@formatter:on

	public static final StatusDescription[] visibleStatusForOwner = new StatusDescription[] { StatusDescription.ACTIVE,
			StatusDescription.PENDING, StatusDescription.SOLD, StatusDescription.EXPIRED};

	public static final StatusDescription[] visibleStatusForOther = new StatusDescription[] { StatusDescription.ACTIVE,
			StatusDescription.SOLD };
	
	public static final StatusDescription[] visibleStatusForFollowedItems = new StatusDescription[] { StatusDescription.ACTIVE,
	 };
	
	
	private final String description;

	private StatusDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return description;
	}
}