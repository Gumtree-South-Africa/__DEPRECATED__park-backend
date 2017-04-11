package com.ebay.park.service.rating.dto;

import com.ebay.park.service.ParkRequest;

public class RateUserRequest extends ParkRequest {
	private String userToRate;
	private String itemId;
	private String ratingStatus;
	private String comment;
	
	public String getUserToRate() {
		return userToRate;
	}
	public void setUserToRate(String userToRate) {
		this.userToRate = userToRate;
	}
	public String getRatingStatus() {
		return ratingStatus;
	}
	public void setRatingStatus(String ratingStatus) {
		this.ratingStatus = ratingStatus;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	
}
