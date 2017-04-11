/*
 * Copyright eBay, 2014
 */
package com.ebay.park.service.location.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author jpizarro
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Meta {

	private int code;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
