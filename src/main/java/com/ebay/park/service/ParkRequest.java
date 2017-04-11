/**
 * Copyright eBay, 2014
 */
package com.ebay.park.service;

import com.ebay.park.util.ParkConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.joda.time.DateTime;

import java.util.Date;

public class ParkRequest implements ParkConstants {

	@JsonIgnore
	protected String token;

	protected String language;

	/**
	 * This field has the purpose of ensure no  ParkRequest will serialize into an empty object.
	 * TODO Validate format, with the API consumers
	 * */
	protected transient Date timestamp;

	public ParkRequest() {
		timestamp = DateTime.now().toDate();
	}

	public ParkRequest(String token) {
		this();
		this.token = token;
	}

	public ParkRequest(String token, String language) {
		this(token);
		this.language = language;
	}


	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * @return the timestamp
	 */
	public Date getTimestamp() {
		return timestamp;
	}

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

}
