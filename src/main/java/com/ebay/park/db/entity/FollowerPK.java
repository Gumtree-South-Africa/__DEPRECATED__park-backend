/*
 * Copyright eBay, 2014
 */
package com.ebay.park.db.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * The primary key class for the follower database table.
 * 
 * @author juan.pizarro
 * 
 */
@Embeddable
public class FollowerPK implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * The id of the follower user.
	 */
	@Column(name = "fol_id")
	private Long followerId;

	/**
	 * The userId of the user being followed
	 */
	@Column(name = "fol_usu_id")
	private Long userId;

	public FollowerPK() {
	}

	public FollowerPK(Long followerId, Long userId){
		this.followerId = followerId;
		this.userId = userId;
	}
	
	public Long getFollowerId() {
		return this.followerId;
	}

	public void setFollowerId(Long followerId) {
		this.followerId = followerId;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof FollowerPK)) {
			return false;
		}
		FollowerPK castOther = (FollowerPK) other;
		return this.followerId.equals(castOther.followerId) && this.userId.equals(castOther.userId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.followerId.hashCode();
		hash = hash * prime + this.userId.hashCode();

		return hash;
	}
}