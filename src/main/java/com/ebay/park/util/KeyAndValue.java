/*
 * Copyright eBay, 2014
 */
package com.ebay.park.util;

/**
 * @author jpizarro
 * 
 */
public class KeyAndValue {

	private String key;
	private String value;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static KeyAndValue from(String key, String value) {
		KeyAndValue kav = new KeyAndValue();
		kav.setKey(key);
		kav.setValue(value);
		return kav;
	}

}
