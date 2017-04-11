package com.ebay.park.util;

import java.io.Serializable;

/**
 * POJO to retrieve Account Kit access token information.
 * Example:
 * {  
   "id":"12345",
   "phone":{  
      "number":"+15551234567"
      "country_prefix": "1",
      "national_number": "5551234567"
   }
  }
 * 
 * @author scalderon
 * @since 2.0.2
 *
 */
public class AccountKitPhone implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private PhoneNumber phone;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public PhoneNumber getPhone() {
		return phone;
	}
	
	public void setPhone(PhoneNumber phone) {
		this.phone = phone;
	}

}