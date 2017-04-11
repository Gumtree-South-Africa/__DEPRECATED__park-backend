/*
 * Copyright eBay, 2014
 */
package com.ebay.park.db.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * The primary key class for the user_group database table.
 * 
 * @author juan.pizarro
 * 
 */
@Embeddable
public class UserFollowsGroupPK implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "ug_use_id")
	private Long userId;

	@Column(name = "ug_grp_id")
	private Long groupId;

	UserFollowsGroupPK() {
	}

	public UserFollowsGroupPK(Long userId, Long groupId) {
		this.groupId = groupId;
		this.userId = userId;
	}

	public Long getUserId() {
		return this.userId;
	}

	public Long getGroupId() {
		return this.groupId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	
	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof UserFollowsGroupPK)) {
			return false;
		}
		UserFollowsGroupPK castOther = (UserFollowsGroupPK) other;
		return this.userId.equals(castOther.userId) && this.groupId.equals(castOther.groupId);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.userId.hashCode();
		hash = hash * prime + this.groupId.hashCode();

		return hash;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserFollowsGroupPK [userId=").append(userId).append(", groupId=").append(groupId).append("]");
		return builder.toString();
	}

}