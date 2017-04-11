package com.ebay.park.service.banner.dto;

import com.ebay.park.db.entity.Banner;


public class SmallBanner {
	
	private String message;
	private BannerType type;

	public SmallBanner(){
		
	}
	
	public SmallBanner(Banner banner){
		this.message = banner.getMessage();
		this.type = banner.getType();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public BannerType getType() {
		return type;
	}

	public void setType(BannerType type) {
		this.type = type;
	}
	
}
