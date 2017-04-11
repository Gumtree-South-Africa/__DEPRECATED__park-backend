/*
 * Copyright eBay, 2014
 */
package com.ebay.park.db.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * The persistent class for the follower database table.
 * 
 * @author juan.pizarro
 * 
 */
@Entity
@Table(name = "follower")
public class Follower extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FollowerPK id;

	/**
	 * The user being followed bi-directional many-to-one association to User
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fol_id", insertable = false, updatable = false)
	private User userFollower;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fol_usu_id", insertable = false, updatable = false)
	private User userFollowed;
	
	public Follower() {
	}

	public Follower(FollowerPK id, User user) {
		this.id = id;
		this.userFollower = user;
	}

	public FollowerPK getId() {
		return this.id;
	}

	public void setId(FollowerPK id) {
		this.id = id;
	}

	public User getUserFollower() {
		return this.userFollower;
	}

	public void setUserFollower(User userFollower) {
		this.userFollower = userFollower;
	}
	
	public User getUserFollowed() {
		return userFollowed;
	}

	public void setUserFollowed(User userFollowed) {
		this.userFollowed = userFollowed;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((userFollowed == null) ? 0 : userFollowed.hashCode());
		result = prime * result
				+ ((userFollower == null) ? 0 : userFollower.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Follower other = (Follower) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		
		return true;
 	}

}