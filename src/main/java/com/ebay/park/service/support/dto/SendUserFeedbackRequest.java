package com.ebay.park.service.support.dto;

import com.ebay.park.service.ParkRequest;

public class SendUserFeedbackRequest extends ParkRequest {
	private String message;
	private String deviceModel;
	private String appVersion;
	private String countryCode;
	
	public SendUserFeedbackRequest(){}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	
	
}
