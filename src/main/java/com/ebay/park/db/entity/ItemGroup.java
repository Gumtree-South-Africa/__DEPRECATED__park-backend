/*
 * Copyright eBay, 2014
 */
package com.ebay.park.db.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * The persistent class for the item_group database table.
 * 
 * @author marcos.lambolay
 * 
 */
@Entity
@Table(name = "item_group")
public class ItemGroup extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ItemGroupPK id;

	// bi-directional many-to-one association to User
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ig_ite_id", insertable = false, updatable = false)
	private Item item;

	// bi-directional many-to-one association to Item
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ig_grp_id", insertable = false, updatable = false)
	private Group group;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name =  "ig_date")
	private Date date;

	public ItemGroup(Item item, Group group, Date date) {
		this.id = new ItemGroupPK(item.getId(), group.getId());
		this.group = group;
		this.item = item;
		this.date = date;
	}
	
	ItemGroup() {
	}

	public Item getItem() {
		return item;
	}

	public Group getGroup() {
		return group;
	}

	public Date getDate() {
		return date;
	}
	
	public void setDate(Date newDate) {
		this.date = newDate;
	}
	
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null){
			return false;
		}
		if (!this.getClass().equals(obj.getClass())) {
			return false;
		}
		ItemGroup other = (ItemGroup) obj;
		return other.getGroup().equals(getGroup()) && other.getItem().equals(getItem());
	}
}