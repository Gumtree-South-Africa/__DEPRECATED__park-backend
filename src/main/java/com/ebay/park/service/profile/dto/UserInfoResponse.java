package com.ebay.park.service.profile.dto;

import com.ebay.park.db.entity.User;
import com.ebay.park.util.DataCommonUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Class used to return the user information after an update.
 * 
 * @author lucia.masola
 * 
 */
public class UserInfoResponse {

	@JsonIgnore
	private User user;

	public UserInfoResponse() {
	}

	/**
	 * Constructs a UserInfoResponse with the given <code>user</code>
	 * 
	 * @param user
	 */

	public UserInfoResponse(User user) {
		this.user = user;
	}

	public String getMobile() {
		return user.getMobile();
	}

	public String getLocation() {
		return DataCommonUtil.buildLocation(user.getLatitude(),
				user.getLongitude());
	}

	public String getLocationName() {
		return user.getLocationName();
	}

	public String getPicture() {
		return user.getPicture();
	}

	public String getZipCode() {
		return user.getZipCode();
	}
	
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
}
