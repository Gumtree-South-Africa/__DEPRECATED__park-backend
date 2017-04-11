/**
 * 
 */
package com.ebay.park.db.entity;


/**
 * @author jppizarro
 * 
 */
public enum RatingStatus {

	NEGATIVE("negative"), 
	NEUTRAL("neutral"), 
	POSITIVE("positive");

	private String description;

	private RatingStatus(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
	
	public static RatingStatus fromDescription(String description) {
		if (description != null) {
			for (RatingStatus rating : values()) {
				if (rating.description.equals(description)) {
					return rating;
				}
			}
		}

		return null;
	}
}
