/*
 * Copyright eBay, 2014
 */
package com.ebay.park.db.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * The persistent class for the idiom_category database table.
 * 
 * @author juan.pizarro
 * 
 */
@Entity
@Table(name = "idiom_category")
public class IdiomCategory extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private IdiomCategoryPK id;

	@Column(name = "ic_lbldescription")
	private String lblDescription;

	@Column(name = "ic_lblname")
	private String lblName;

	// bi-directional many-to-one association to Category
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ic_cat_id", insertable = false, updatable = false)
	private Category category;

	// bi-directional many-to-one association to Idiom
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ic_idi_id", insertable = false, updatable = false)
	private Idiom idiom;

	public IdiomCategory() {
	}

	public IdiomCategoryPK getId() {
		return this.id;
	}

	public void setId(IdiomCategoryPK id) {
		this.id = id;
	}

	public String getLbldescription() {
		return this.lblDescription;
	}

	public void setLbldescription(String lbldescription) {
		this.lblDescription = lbldescription;
	}

	public String getLblName() {
		return this.lblName;
	}

	public void setLblName(String lblName) {
		this.lblName = lblName;
	}

	public Category getCategory() {
		return this.category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Idiom getIdiom() {
		return this.idiom;
	}

	public void setIdiom(Idiom idiom) {
		this.idiom = idiom;
	}

}