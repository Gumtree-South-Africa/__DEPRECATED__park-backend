package com.ebay.park.service.banner.dto;

public enum BannerPriority {

	SYSTEM(1),
	USER_BASED(2),
	GENERAL_MESSAGES(3),
	NON_SHOW(4),
	;
	
	private Integer priority;
	
	BannerPriority(Integer priority){
		this.priority = priority;
	}
	
	public Integer getPriority(){
		return priority;
	}
}
