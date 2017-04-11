package com.ebay.park.service.device.dto;

import com.ebay.park.service.ParkRequest;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class DeviceRequest extends ParkRequest {

	private String deviceId;
	private String deviceType;
	private String uniqueDeviceId;
	/**
	 * The swrve user ID
	 */
	@JsonProperty(value = "swrve_id")
	private String swrveId;
	
	public DeviceRequest(){
		
	}

	public DeviceRequest(String deviceId, String deviceType, String uniqueDeviceId) {
		super();
		this.deviceId = deviceId;
		this.deviceType = deviceType;
		this.uniqueDeviceId = uniqueDeviceId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DeviceRequest [deviceId=")
		    .append(deviceId)
		    .append(", deviceType=")
		    .append(deviceType)
		    .append(", uniqueDeviceId=")
            .append(uniqueDeviceId)
            .append(", swrveId=")
            .append(swrveId)
				.append("]");
		return builder.toString();
	}

	/**
	 * @return the uniqueDeviceId
	 */
	public String getUniqueDeviceId() {
		return uniqueDeviceId;
	}

	/**
	 * @param uniqueDeviceId the uniqueDeviceId to set
	 */
	public void setUniqueDeviceId(String uniqueDeviceId) {
		this.uniqueDeviceId = uniqueDeviceId;
	}

	public String getSwrveId() {
		return swrveId;
	}

	public void setSwrveId(String swrveId) {
		this.swrveId = swrveId;
	}
	
	
}
