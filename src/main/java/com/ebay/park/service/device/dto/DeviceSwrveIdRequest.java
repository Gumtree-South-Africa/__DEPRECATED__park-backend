package com.ebay.park.service.device.dto;

import com.ebay.park.service.ParkRequest;

/**
 * Request to set the swrve id to user session
 * @author scalderon
 *
 */
public class DeviceSwrveIdRequest extends ParkRequest {
	
	/**
	 * The swrve user ID
	 */
	private String swrveId;
	
	public DeviceSwrveIdRequest() {
		
	}
	
	public DeviceSwrveIdRequest(String swrveId) {
		this.swrveId = swrveId;
	}

	public String getSwrveId() {
		return swrveId;
	}

	public void setSwrveId(String swrveId) {
		this.swrveId = swrveId;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DeviceSwrveIdRequest= [")
		    .append("swrveId=")
		    .append(swrveId)
		    .append("]");
		return builder.toString();
	}
	
}
 