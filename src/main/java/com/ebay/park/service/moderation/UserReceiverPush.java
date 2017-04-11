package com.ebay.park.service.moderation;

import com.ebay.park.db.entity.DeviceType;

public class UserReceiverPush {

	private Long userId;

	private String deviceId;

	private DeviceType platform;
	
	public UserReceiverPush(){
		
	}

	public Long getUserId() {
		return userId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public DeviceType getPlatform() {
		return platform;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public void setPlatform(DeviceType platform) {
		this.platform = platform;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserReceiverPush [userId=").append(userId).append(", deviceId=")
		.append(deviceId == null ? "null" : deviceId).append(", platform=")
		.append(platform == null ? "null" : platform.getValue()).append("]");
		return builder.toString();
	}
}
