package com.ebay.park.util;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO to retrieve phone number from Account Kit Graph API:
 * Example:
 * "phone":{  
      "number":"+15551234567"
      "country_prefix": "1",
      "national_number": "5551234567"
   }
 * @author scalderon
 * @since 2.0.2
 *
 */
public class PhoneNumber implements Serializable {

	private static final long serialVersionUID = 1L;

	private String number;
	
	@JsonProperty(value="country_prefix")
	private String countryPrefix;
	
	@JsonProperty(value="national_number")
	private String nationalNumber;

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getCountryPrefix() {
		return countryPrefix;
	}

	public void setCountryPrefix(String countryPrefix) {
		this.countryPrefix = countryPrefix;
	}

	public String getNationalNumber() {
		return nationalNumber;
	}

	public void setNationalNumber(String nationalNumber) {
		this.nationalNumber = nationalNumber;
	}
}