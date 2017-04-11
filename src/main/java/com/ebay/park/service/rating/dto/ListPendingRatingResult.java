package com.ebay.park.service.rating.dto;

public class ListPendingRatingResult {
	private Long ratingId;
	private Long userIdToRate;
	private String userImageUrl;
	private String usernameToRate;
	private String itemName;
	private Long itemId;
	private String itemImageUrl;
	
	public ListPendingRatingResult(Long ratingId, Long userIdToRate, String usernameToRate,
		String itemName, Long itemId, String userImageUrl, String itemImageUrl){
		this.ratingId = ratingId;
		this.userIdToRate = userIdToRate;
		this.usernameToRate = usernameToRate;
		this.itemName = itemName;
		this.itemId = itemId;
		this.userImageUrl = userImageUrl;
		this.itemImageUrl = itemImageUrl;
	}
	
	public Long getRatingId() {
		return ratingId;
	}
	public void setRatingId(Long ratingId) {
		this.ratingId = ratingId;
	}
	public Long getUserIdToRate() {
		return userIdToRate;
	}
	public void setUserIdToRate(Long userIdToRate) {
		this.userIdToRate = userIdToRate;
	}
	public String getUsernameToRate() {
		return usernameToRate;
	}
	public void setUsernameToRate(String usernameToRate) {
		this.usernameToRate = usernameToRate;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	
	public String getUserImageUrl() {
		return userImageUrl;
	}
	
	public void setUserImageUrl(String userImageUrl) {
		this.userImageUrl = userImageUrl;
	}

	public String getItemImageUrl() {
		return itemImageUrl;
	}

	public void setItemImageUrl(String itemImageUrl) {
		this.itemImageUrl = itemImageUrl;
	}
}
