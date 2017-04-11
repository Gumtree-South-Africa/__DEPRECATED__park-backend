/*
 * Copyright eBay, 2014
 */
package com.ebay.park.db.entity;

import javax.persistence.*;

import com.ebay.park.util.DataCommonUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * The persistent class for the user_group database table.
 * 
 * @author juan.pizarro
 * 
 */
@Entity
@Table(name = "user_group")
public class UserFollowsGroup extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private UserFollowsGroupPK id;

	// bi-directional many-to-one association to Group
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ug_grp_id", insertable = false, updatable = false)
	private Group group;

	// bi-directional many-to-one association to User
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ug_use_id", insertable = false, updatable = false)
	private User user;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ug_last_access")
	private Date lastAccess;

	UserFollowsGroup() {
	}

	public UserFollowsGroup(Group group, User user) {
		super();
		this.group = group;
		this.user = user;
		this.id = new UserFollowsGroupPK(user.getId(),group.getId());
		this.lastAccess = DataCommonUtil.getCurrentTime();
	}

	public UserFollowsGroupPK getId() {
		return this.id;
	}
	
	public Date getLastAccess() {
		return lastAccess;
	}
	
	public void setLastAccess(Date newLastAccess) {
		this.lastAccess = newLastAccess;
	}

	public Group getGroup() {
		return this.group;
	}
	public User getUser() {
		return this.user;
	}
	
	public boolean isGroup(Group group){
		return this.getGroup().equals(group);
	}

	public boolean isUser(User user){
		return this.getUser().equals(user);
	}

	public boolean isUser(Long userId) {
		return this.getUser().getId().equals(userId);
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

		if (!(obj instanceof UserFollowsGroup))
			return false;

		UserFollowsGroup other = (UserFollowsGroup) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserFollowsGroup [id=").append(id).append(", group=").append(group).append(", user=")
		.append(user).append("]");
		return builder.toString();
	}

}