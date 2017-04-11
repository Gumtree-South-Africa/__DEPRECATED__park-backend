/*
 * Copyright eBay, 2014
 */
package com.ebay.park.db.entity;

import org.apache.commons.lang.Validate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * The primary key class for the item_group database table.
 * 
 * @author marcos.lambolay
 * 
 */
@Embeddable
public class ItemGroupPK implements Serializable {
	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name = "ig_ite_id")
	private Long itemId;

	@Column(name = "ig_grp_id")
	private Long groupId;

	public ItemGroupPK() {
	}

	public ItemGroupPK(Long itemId, Long groupId) {
		Validate.notNull(itemId, "itemId should not be null");
		Validate.notNull(groupId);
		this.itemId = itemId;
		this.groupId = groupId;
	}
	
	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((itemId == null) ? 0 : itemId.hashCode());
		result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
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
		ItemGroupPK other = (ItemGroupPK) obj;
		if (itemId == null) {
			if (other.itemId != null)
				return false;
		} else if (!itemId.equals(other.itemId))
			return false;
		if (groupId == null) {
			if (other.groupId != null)
				return false;
		} else if (!groupId.equals(other.groupId))
			return false;
		return true;
	}
}