package com.ebay.park.service.social.dto;

import com.ebay.park.db.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class BasicUser {

	private String username;

	private String email;

	private String profilePicture;

	private String locationName;
	
	private Long userId;

	@JsonInclude(Include.NON_NULL)
	private Boolean followedByUser;

	@JsonInclude(Include.NON_NULL)
	private Boolean followsUser;

	public BasicUser() {

	}

	public BasicUser(User user) {
		this.userId = user.getId();
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.locationName = user.getLocationName();
		this.profilePicture = user.getPicture();	
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getFollowedByUser() {
		return followedByUser;
	}

	public void setFollowedByUser(Boolean followedByUser) {
		this.followedByUser = followedByUser;
	}

	public Boolean getFollowsUser() {
		return followsUser;
	}

	public void setFollowsUser(Boolean followsUser) {
		this.followsUser = followsUser;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
