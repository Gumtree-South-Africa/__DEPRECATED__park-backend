package com.ebay.park.service.device.dto;

import com.ebay.park.db.entity.Device;
import com.ebay.park.db.entity.DeviceType;

import java.util.Date;

public class SmallDevice {

	private Long id;

	private DeviceType description;

	private String deviceId;

	private Date creationDate;
	
	public SmallDevice(Device device){
		this.id = device.getId();
		//this.creationDate = device.getCreationDate();
		this.deviceId = device.getDeviceId();
		//this.description = device.getDescription();
	}
	
	public SmallDevice(){
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DeviceType getDescription() {
		return description;
	}

	public void setDescription(DeviceType description) {
		this.description = description;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	
	
	
}
