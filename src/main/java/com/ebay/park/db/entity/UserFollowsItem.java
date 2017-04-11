/*
 * Copyright eBay, 2014
 */
package com.ebay.park.db.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * The persistent class for the user_itemliked database table.
 * 
 * @author juan.pizarro
 * 
 */
@Entity
@Table(name = "user_itemliked")
public class UserFollowsItem extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private UserFollowsItemPK id;

	// bi-directional many-to-one association to User
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ul_usu_id", insertable = false, updatable = false)
	private User user;

	// bi-directional many-to-one association to Item
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ul_ite_id", insertable = false, updatable = false)
	private Item item;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ul_dateliked")
	private Date dateFollowed;

	public UserFollowsItem() {
	}
	
	public UserFollowsItem(UserFollowsItemPK id, User user, Item item) {
		this.id = id;
		this.user = user;
		this.item = item;
	}

	public UserFollowsItemPK getId() {
		return this.id;
	}

	public void setId(UserFollowsItemPK id) {
		this.id = id;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Item getItem() {
		return this.item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
	
	public Date getDateFollowed() {
		return dateFollowed;
	}
	
	public void setDateFollowed(Date dateFollowed) {
		this.dateFollowed = dateFollowed;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		UserFollowsItem other = (UserFollowsItem) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}