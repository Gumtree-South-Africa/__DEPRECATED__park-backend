package com.ebay.park.service.asset.dto;

public class GetTutorialResponse {

	private Integer step;
	private String picture;

	public GetTutorialResponse(String picture, Integer step) {
		this.picture = picture;
		this.step = step;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public Integer getStep() {
		return step;
	}

	public void setStep(Integer step) {
		this.step = step;
	}

}
