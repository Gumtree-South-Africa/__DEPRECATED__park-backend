package com.ebay.park.db.entity;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * The primary key class for the user_itemreport database table.
 * 
 * @author cbirge
 * 
 */
public class UserReportItemPK implements Serializable {

	private static final long serialVersionUID = -7295514331245154211L;

	@Column(name = "ur_usu_id")
	private Long userReporterId;

	@Column(name = "ur_ite_id")
	private Long itemReportedId;

	public UserReportItemPK() {
		super();
	}

	/**
	 * @return the userReporterId
	 */
	public Long getUserReporterId() {
		return userReporterId;
	}

	/**
	 * @param userReporterId
	 *            the userReporterId to set
	 */
	public void setUserReporterId(Long userReporterId) {
		this.userReporterId = userReporterId;
	}

	/**
	 * @return the itemReportedId
	 */
	public Long getItemReportedId() {
		return itemReportedId;
	}

	/**
	 * @param itemReportedId
	 *            the itemReportedId to set
	 */
	public void setItemReportedId(Long itemReportedId) {
		this.itemReportedId = itemReportedId;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof UserReportItemPK)) {
			return false;
		}
		UserReportItemPK castOther = (UserReportItemPK) other;
		return this.userReporterId.equals(castOther.userReporterId)
				&& this.itemReportedId.equals(castOther.itemReportedId);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.userReporterId.hashCode();
		hash = hash * prime + this.itemReportedId.hashCode();

		return hash;
	}
}
