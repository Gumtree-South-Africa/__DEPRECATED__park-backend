/*
 * Copyright eBay, 2014
 */
package com.ebay.park.service.user.dto;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.ebay.park.service.ParkRequest;
import com.ebay.park.service.device.dto.DeviceRequest;

/**
 * @author juan.pizarro
 */
public class SignRequest extends ParkRequest {

	private String username;
	private String password;
	private String email;
	private DeviceRequest device;


	public SignRequest() {
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public DeviceRequest getDevice() {
		return device;
	}

	public void setDevice(DeviceRequest device) {
		this.device = device;
	}
	
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
}
