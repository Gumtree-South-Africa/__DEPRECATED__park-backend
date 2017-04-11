/*
 * Copyright eBay, 2014
 */
package com.ebay.park.db.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * The primary key class for the user_itemliked database table.
 * 
 * @author juan.pizarro
 * 
 */
@Embeddable
public class UserFollowsItemPK implements Serializable {
	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name = "ul_usu_id")
	private Long userId;

	@Column(name = "ul_ite_id")
	private Long itemId;

	public UserFollowsItemPK() {
	}

	public UserFollowsItemPK(User user, Item item) {
		this.userId = user.getId();
		this.itemId = item.getId();
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getItemId() {
		return this.itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof UserFollowsItemPK)) {
			return false;
		}
		UserFollowsItemPK castOther = (UserFollowsItemPK) other;
		return this.userId.equals(castOther.userId) && this.itemId.equals(castOther.itemId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.userId.hashCode();
		hash = hash * prime + this.itemId.hashCode();

		return hash;
	}

}