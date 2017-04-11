package com.ebay.park.service.user.dto;

import com.ebay.park.service.ParkRequest;

/**
 * Change password request.
 * 
 * @author federico.jaite
 */
public class ChangePwdRequest extends ParkRequest {

	private String currentPassword;
	private String newPassword;

	public ChangePwdRequest() {
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
}
