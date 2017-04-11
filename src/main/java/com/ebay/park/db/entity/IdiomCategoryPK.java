/*
 * Copyright eBay, 2014
 */
package com.ebay.park.db.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * The primary key class for the idiom_category database table.
 * 
 * @author juan.pizarro
 * 
 */
@Embeddable
public class IdiomCategoryPK implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "ic_idi_id")
	private Long idiomId;

	@Column(name = "ic_cat_id")
	private Long categoryId;

	public IdiomCategoryPK() {
	}

	public Long getIdiomId() {
		return this.idiomId;
	}

	public void setIdiomId(Long idiomId) {
		this.idiomId = idiomId;
	}

	public Long getCategoryId() {
		return this.categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof IdiomCategoryPK)) {
			return false;
		}
		IdiomCategoryPK castOther = (IdiomCategoryPK) other;
		return this.idiomId.equals(castOther.idiomId) && this.categoryId.equals(castOther.categoryId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.idiomId.hashCode();
		hash = hash * prime + this.categoryId.hashCode();

		return hash;
	}
}