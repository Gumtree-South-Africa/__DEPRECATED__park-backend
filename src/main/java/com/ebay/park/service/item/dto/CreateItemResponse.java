package com.ebay.park.service.item.dto;


/**
 * 
 * @author marcos.lambolay
 */
public class CreateItemResponse {

	private Long id;

	private String facebookPublishError;
	
	private String twitterPublishError;
	
	private String url;
	
	public CreateItemResponse() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
	
	public String getURL() {
		return url;
	}
	
	public void setURL(String url) {
		this.url = url;
	}

}
