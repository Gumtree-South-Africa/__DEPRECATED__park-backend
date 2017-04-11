/**
 * Copyright eBay, 2014
 */
package com.ebay.park.service.notification.dto;

/**
 * @author diana.gazquez
 *
 */
public enum ConfigurationValue {
	
	TRUE(true), FALSE(false), DISABLED(null);
	
	Boolean choosen;
	
	ConfigurationValue(Boolean choosen){
		this.choosen = choosen;
	}

	/**
	 * @return the choosen
	 */
	public Boolean getChoosen() {
		return choosen;
	}
	
	public boolean isDisabled(){
		return ConfigurationValue.DISABLED.equals(this);
	}

}
