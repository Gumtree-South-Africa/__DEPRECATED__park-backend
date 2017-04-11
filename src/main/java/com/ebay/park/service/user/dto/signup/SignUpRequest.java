package com.ebay.park.service.user.dto.signup;

import com.ebay.park.service.user.dto.signin.SignInRequest;
import com.ebay.park.util.ParkConstants;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents a registration form the user must 
 * complete in order to start using the app.
 *
 * @author scalderon
 * @since 2.0.2
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignUpRequest extends SignInRequest {

	/** The username. */
	protected String username;
	
	/** The zip code. */
	protected String zipCode;
	
	/** The location name. */
	protected String locationName;
	
	/** The location:  latitude + longitude. 
	 * Ex: "location": "-37.3160648,-59.1356282"*/
	protected String location;
	
	/** The language. */
	protected String lang = ParkConstants.DEFAULT_LANGUAGE;

	
	/**
	 * Gets the username.
	 *
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Sets the username.
	 *
	 * @param username the new username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Gets the zip code.
	 *
	 * @return the zip code
	 */
	public String getZipCode() {
		return zipCode;
	}
	
	/**
	 * Sets the zip code.
	 *
	 * @param zipCode the new zip code
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	/**
	 * Gets the location name.
	 *
	 * @return the location name
	 */
	public String getLocationName() {
		return locationName;
	}
	
	/**
	 * Sets the location name.
	 *
	 * @param locationName the new location name
	 */
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	/**
	 * Gets the location.
	 *
	 * @return the location: latitude and longitude
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * Sets the location.
	 *
	 * @param location the new location: latitude and longitude
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * Gets the lang.
	 *
	 * @return the lang
	 */
	public String getLang() {
		return lang;
	}

	/**
	 * Sets the lang.
	 *
	 * @param lang the new lang
	 */
	public void setLang(String lang) {
		this.lang = lang;
	}
	
}
