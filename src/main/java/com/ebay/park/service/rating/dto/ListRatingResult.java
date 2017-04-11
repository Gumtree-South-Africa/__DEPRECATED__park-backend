package com.ebay.park.service.rating.dto;

public class ListRatingResult {
	private Long ratingId;
	private Long userId;
	private String username;
	private String userImageUrl;
	private String itemImageUrl;
	private String itemName;
	private String ratingStatus;
	private String comment;
	private String rateDate;
		
	public ListRatingResult(Long ratingId, Long userId, String username,
		String userImageUrl, String itemName, String ratingStatus, String comment,
		String rateDate, String itemImageUrl){

		this.ratingId = ratingId;
		this.userId = userId;
		this.username = username;
		this.userImageUrl = userImageUrl;
		this.itemName = itemName;
		this.ratingStatus = ratingStatus;
		this.comment = comment;
		this.rateDate = rateDate;
		this.itemImageUrl = itemImageUrl;
	}


	public Long getRatingId() {
		return ratingId;
	}


	public void setRatingId(Long ratingId) {
		this.ratingId = ratingId;
	}


	public Long getUserId() {
		return userId;
	}


	public void setUserId(Long userId) {
		this.userId = userId;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getUserImageUrl() {
		return userImageUrl;
	}


	public void setUserImageUrl(String userImageUrl) {
		this.userImageUrl = userImageUrl;
	}


	public String getItemName() {
		return itemName;
	}


	public void setItemName(String itemName) {
		this.itemName = itemName;
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


	public String getRateDate() {
		return rateDate;
	}


	public void setRateDate(String rateDate) {
		this.rateDate = rateDate;
	}


	public String getItemImageUrl() {
		return itemImageUrl;
	}


	public void setItemImageUrl(String itemImageUrl) {
		this.itemImageUrl = itemImageUrl;
	}
	
}
