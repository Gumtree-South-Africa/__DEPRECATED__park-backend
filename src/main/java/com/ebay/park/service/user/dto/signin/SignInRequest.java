package com.ebay.park.service.user.dto.signin;

import com.ebay.park.service.device.dto.DeviceRequest;

/**
 * Parent class to support signIn requests.
 * @author scalderon
 * @since 2.0.2
 *
 */
public class SignInRequest {

	/** The device request data. */
	protected DeviceRequest device;
	
	/**
	 * Gets the device.
	 *
	 * @return the device
	 */
	public DeviceRequest getDevice() {
		return device;
	}

	/**
	 * Sets the device.
	 *
	 * @param device the new device
	 */
	public void setDevice(DeviceRequest device) {
		this.device = device;
	}

}
