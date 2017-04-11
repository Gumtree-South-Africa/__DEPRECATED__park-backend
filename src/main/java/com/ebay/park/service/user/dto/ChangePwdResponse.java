package com.ebay.park.service.user.dto;

/**
 * @author federico.jaite
 */
public class ChangePwdResponse {

private boolean success;

//	private String token;

	public ChangePwdResponse(boolean success) {
		this.setSuccess(success);
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	//TODO Reactive session for CURRENT device
//	public ChangePwdResponse(String parkToken) {
//		this.token = parkToken;
//	}
//
//	public String getToken() {
//		return token;
//	}
//
//	public void setToken(String token) {
//		this.token = token;
//	}

}
