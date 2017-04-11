/**
 * Copyright eBay, 2014
 */
package com.ebay.park.service.user.dto;

import com.ebay.park.util.LanguageUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author jppizarro
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class SignUpRequest extends SignRequest {

	private String location;
	private String locationName;
	private String lang = DEFAULT_LANGUAGE;
	private String mobile;
	private String zipCode;

	@JsonProperty(value = "fb_user_id")
	private String facebookUserId;

	@JsonProperty(value = "fb_token")
	private String facebookToken;

	public SignUpRequest() {
	}

	public SignUpRequest(String username, String password, String email,
			String location, String facebookUserId, String facebookToken,
			String photoId, String locationName, String gender, String birthday,
			String mobile, String zipCode) {
		super();
		this.setUsername(username);
		this.setPassword(password);
		this.setEmail(email);
		this.location = location;
		this.facebookUserId = facebookUserId;
		this.facebookToken = facebookToken;
		this.locationName = locationName;
		this.mobile = mobile;
		this.zipCode = zipCode;
	}


	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getFacebookUserId() {
		return facebookUserId;
	}

	public void setFacebookUserId(String facebookUserId) {
		this.facebookUserId = facebookUserId;
	}

	public String getFacebookToken() {
		return facebookToken;
	}

	public void setFacebookToken(String facebookToken) {
		this.facebookToken = facebookToken;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = LanguageUtil.getValidLanguage(lang);
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}


	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SignUpRequest [username=").append(this.getUsername())
				.append(", email=")
				.append(this.getEmail())
				.append(", location=")
				.append(location)
				.append(", locationName=")
				.append(locationName)
				.append(", zipCode=")
				.append(zipCode)
				.append(", lang=")
				.append(lang)
				.append(", mobile=")
				.append(mobile)
				.append(", device=")
				.append(this.getDevice())
				.append(", facebookUserId=")
				.append(facebookUserId)
				.append(", facebookToken=")
				.append(facebookToken)
				.append("]");
		return builder.toString();
	}

}
