/*
 * Copyright eBay, 2014
 */
package com.ebay.park.exception;

/**
 * @author jpizarro
 * 
 */
public class Context {

	private Object data;
	private String token;

	public Object getData() {
		return data;
	}

	public String getToken() {
		return token;
	}
	
	public Context addData(Object data) {
		this.data = data;
		return this;
	}

	public Context addToken(String token) {
		this.token = token;
		return this;
	}

}
