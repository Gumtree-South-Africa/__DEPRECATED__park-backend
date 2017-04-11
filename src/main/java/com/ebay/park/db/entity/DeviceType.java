package com.ebay.park.db.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum DeviceType {

	IOS("ios"), ANDROID("android"), WEB("web") ;

	private String value;

	DeviceType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static List<String> getDevices() {
		List<DeviceType> devices = Arrays.asList(DeviceType.values());
		List<String> devicesList = new ArrayList<String>();
		for (DeviceType device : devices) {
			devicesList.add(device.getValue());
		}
		return devicesList;
	}

	public static DeviceType getDeviceByValue(String value) {
		return DeviceType.valueOf(value.toUpperCase());
	}

}
