package com.ebay.park.service;

/**
 * Service Response when an error occured.
 * 
 * @author lucia.masola
 * 
 */
public class ServiceErrorResponse extends ServiceResponse {

	private int errorCode;

	public ServiceErrorResponse(ServiceResponseStatus status, int errorCode, String message, Object data) {
		super(status, message, data);
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

}
