package com.ebay.park.service.location.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author giriarte
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VenueStats {

	public Integer getCheckinsCount() {
		return checkinsCount;
	}
	public void setCheckinsCount(Integer checkinsCount) {
		this.checkinsCount = checkinsCount;
	}
	public Integer getUsersCount() {
		return usersCount;
	}
	public void setUsersCount(Integer usersCount) {
		this.usersCount = usersCount;
	}
	public Integer getTipCount() {
		return tipCount;
	}
	public void setTipCount(Integer tipCount) {
		this.tipCount = tipCount;
	}
	private Integer checkinsCount;
	private Integer usersCount;
	private Integer tipCount;
	
	
}
