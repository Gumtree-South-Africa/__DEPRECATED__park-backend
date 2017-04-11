/*
 * Copyright eBay, 2014
 */
package com.ebay.park.service.user.dto;

/**
 * @author juan.pizarro
 * 
 */
public class CheckValueResponse {

	private boolean available;

	public CheckValueResponse() {
	}

	public CheckValueResponse(boolean available) {
		this.available = available;
	}

	public boolean isAvailable() {
		return available;
	}

}
