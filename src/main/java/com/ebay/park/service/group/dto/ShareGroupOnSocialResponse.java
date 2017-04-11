package com.ebay.park.service.group.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


public class ShareGroupOnSocialResponse {

	private boolean success;

	private String facebookPublishError;
	
	private String twitterPublishError;
	
	public ShareGroupOnSocialResponse() {
	}
	
	@JsonInclude(Include.NON_NULL)
	public String getFacebookPublishError() {
		return facebookPublishError;
	}

	public void setFacebookPublishError(String facebookPublishError) {
		this.facebookPublishError = facebookPublishError;
	}

	@JsonInclude(Include.NON_NULL)
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
