package com.ebay.park.service.moderation.dto;

import com.ebay.park.db.entity.User;
import com.ebay.park.util.DataCommonUtil;

public class ModerationUserSummary {

	private Long userId;

	private String username;

	private String email;

	private String picture;

	private String status;

	private String registrationDate;

	/**
	 * It represents if the user is either email or mobile verified.
	 */
    private boolean userVerified;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long id) {
		this.userId = id;
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

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(String registrationDate) {
		this.registrationDate = registrationDate;
	}

	public static ModerationUserSummary fromUser(User user) {
		ModerationUserSummary result = new ModerationUserSummary();
		result.setUserId(user.getId());
		result.setEmail(user.getEmail());
		result.setUsername(user.getUsername());
		result.setStatus(user.getStatus().toString());
		result.setPicture(user.getPicture());
		result.setUserVerified(user.isEmailVerified() || user.isMobileVerified());
		if (user.getCreation() != null) {
			result.setRegistrationDate(DataCommonUtil.getDateTimeAsISO(user
					.getCreation()));
		}
		return result;
	}

	public boolean getUserVerified() {
		return userVerified;
	}

	public void setUserVerified(boolean userVerified) {
		this.userVerified = userVerified;
	}
}
