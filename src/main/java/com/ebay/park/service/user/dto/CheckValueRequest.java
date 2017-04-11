/*
 * Copyright eBay, 2014
 */
package com.ebay.park.service.user.dto;

import com.ebay.park.service.ParkRequest;

/**
 * Pair field - value.
 * 
 * @author juan.pizarro
 * 
 */
public class CheckValueRequest extends ParkRequest {

	private String name;
	private String value;

	public CheckValueRequest() {
	}

	public CheckValueRequest(String field, String value) {
		this.name = field;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
