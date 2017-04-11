package com.ebay.park.util;

import java.io.Serializable;

/**
 * POJO to retrieve email address from Account Kit Graph API:
 * Example:
 * "email":{  
      "address":"nicolas.porpiglia\u0040gmail.com"
   }
 * @author scalderon
 * @since 2.0.2
 *
 */
public class Email implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The address. */
	private String address;

	/**
	 * Gets the address.
	 *
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Sets the address.
	 *
	 * @param address the new address
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	
}
