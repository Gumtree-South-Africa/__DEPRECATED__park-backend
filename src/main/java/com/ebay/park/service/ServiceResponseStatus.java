/**
 * 
 */
package com.ebay.park.service;

/**
 * @author jppizarro
 * 
 */
public enum ServiceResponseStatus {

	SUCCESS(1, "success"), FAIL(2, "fail"), ERROR(3, "error");

	private int code;
	private String description;

	private ServiceResponseStatus(int code, String description) {
		this.code = code;
		this.description = description;
	}

	public int getCode() {
		return this.code;
	}

	public String getDescription() {
		return description;
	}
}
