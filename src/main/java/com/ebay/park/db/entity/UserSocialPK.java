/*
 * Copyright eBay, 2014
 */
package com.ebay.park.db.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * The primary key class for the user_social database table.
 * 
 * @author juan.pizarro
 * 
 */
@Embeddable
public class UserSocialPK implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "us_user_id")
	private Long userId;

	@Column(name = "us_social_id")
	private Long socialId;

	public UserSocialPK(Long userId, Long socialId) {
		this.userId = userId;
		this.socialId = socialId;
	}

	UserSocialPK() {
	}
	
	public Long getUserId() {
		return this.userId;
	}


	public Long getSocialId() {
		return this.socialId;
	}


	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof UserSocialPK)) {
			return false;
		}
		UserSocialPK castOther = (UserSocialPK) other;
		return this.userId.equals(castOther.userId) && this.socialId.equals(castOther.socialId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.userId.hashCode();
		hash = hash * prime + this.socialId.hashCode();

		return hash;
	}

}