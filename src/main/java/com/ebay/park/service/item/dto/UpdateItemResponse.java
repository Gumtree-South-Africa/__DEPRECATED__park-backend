package com.ebay.park.service.item.dto;


public class UpdateItemResponse {

	private boolean success;

	private String facebookPublishError;
	
	private String twitterPublishError;
	
	public UpdateItemResponse() {
	}

	public String getFacebookPublishError() {
		return facebookPublishError;
	}

	public void setFacebookPublishError(String facebookPublishError) {
		this.facebookPublishError = facebookPublishError;
	}

	public String getTwitterPublishError() {
		return twitterPublishError;
	}

	public void setTwitterPublishError(String twitterPublishError) {
		this.twitterPublishError = twitterPublishError;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

}
