package com.ebay.park.service.device.dto;

import com.ebay.park.db.entity.Device;

import java.io.Serializable;

public class DeviceDTO implements Serializable{

	private static final long serialVersionUID = 4742278862168358476L;

	private String deviceId;
	private String deviceType;
	
	public DeviceDTO() {
	}

    public DeviceDTO(String deviceId, String deviceType) {
        this();
        this.deviceId = deviceId;
        this.deviceType = deviceType;
    }

    public DeviceDTO(Device device) {
		this();
		this.deviceId = device.getDeviceId();
		if (device.getPlatform() != null){
			this.deviceType = device.getPlatform().getValue();
		}
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

}
