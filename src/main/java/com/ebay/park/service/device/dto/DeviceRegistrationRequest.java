package com.ebay.park.service.device.dto;

import com.ebay.park.db.entity.DeviceType;
import com.ebay.park.service.ParkRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * It represents a request of registering a device in the system when it cannot be linked to a user.
 * @author Julieta Salvadó
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceRegistrationRequest extends ParkRequest {
	private String uniqueDeviceId;
	
	@JsonProperty(value = "deviceId") 
	private String deviceId;
	
	@JsonProperty(value = "deviceType")
	private DeviceType deviceType;
	
	private Boolean newInstall;
	
	public DeviceRegistrationRequest() {
		
	}
	
	public DeviceRegistrationRequest(String uuid, String deviceId, String deviceType) {
		super();
		this.setUniqueDeviceId(uuid);
		this.setDeviceId(deviceId);
		if (deviceType != null) {
			this.setDeviceType(deviceType.toUpperCase());
		}
	}
	
	public DeviceRegistrationRequest(String uuid, String deviceId, String deviceType, Boolean newInstall) {
		super();
		this.setUniqueDeviceId(uuid);
		this.setDeviceId(deviceId);
		if (deviceType != null) {
			this.setDeviceType(deviceType.toUpperCase());
		}
		this.setNewInstall(newInstall);
	}

	/**
	 * @return the uniqueDeviceId
	 */
	public String getUniqueDeviceId() {
		return uniqueDeviceId;
	}

	/**
	 * @param uuid the uniqueDeviceId to set
	 */
	public void setUniqueDeviceId(String uuid) {
		this.uniqueDeviceId = uuid;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DeviceRegistrationRequest [uniqueDeviceId=").append(uniqueDeviceId)
				.append("]");
		return builder.toString();
	}

	/**
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * @param deviceId the deviceId to set
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * @return the deviceType
	 */
	public DeviceType getDeviceType() {
		return deviceType;
	}

	/**
	 * @param deviceType the deviceType to set
	 */
	public void setDeviceType(String deviceType) {
		this.deviceType = DeviceType.getDeviceByValue(deviceType.toUpperCase());
	}

	/**
	 * @return the newInstall
	 */
	public Boolean getNewInstall() {
		return newInstall;
	}

	/**
	 * @param newInstall the newInstall to set
	 */
	public void setNewInstall(Boolean newInstall) {
		this.newInstall = newInstall;
	}
}
