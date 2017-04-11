package com.ebay.park.service.device.dto;

import com.ebay.park.db.entity.DeviceType;

public class RemoveUserSessionsByDeviceRequest extends RemoveUserSessionsRequest{

	private String deviceId;
	private DeviceType deviceType;
	private String uniqueDeviceId;

	public RemoveUserSessionsByDeviceRequest(String uniqueDeviceId, String deviceId, DeviceType platform) {
		this.setUniqueDeviceId(uniqueDeviceId);
		this.setDeviceId(deviceId);
		this.setDeviceType(platform);
	}
	
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public DeviceType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(DeviceType platform) {
		this.deviceType = platform;
	}

	/**
	 * @return the uniqueDeviceId
	 */
	public String getUniqueDeviceId() {
		return uniqueDeviceId;
	}

	/**
	 * This identifier represents an unique device.
	 * @param uniqueDeviceId the uniqueDeviceId to set
	 */
	public void setUniqueDeviceId(String uniqueDeviceId) {
		this.uniqueDeviceId = uniqueDeviceId;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RemoveUserSessionsByDeviceRequest [deviceId= ")
			.append(this.deviceId).append(", deviceType= ")
			.append(this.deviceType.toString()).append(", uniqueDeviceId= ")
			.append(this.uniqueDeviceId).append("]");
		
		return builder.toString();
	}
	
}
