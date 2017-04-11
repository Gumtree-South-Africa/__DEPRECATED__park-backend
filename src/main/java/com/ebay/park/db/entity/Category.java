/*
 * Copyright eBay, 2014
 */
package com.ebay.park.db.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * The persistent class for the category database table.
 * 
 * @author juan.pizarro
 * 
 */
@Entity
@Table(name = "category")
public class Category extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cat_id")
	private Long id;

	@Column(name = "cat_key", updatable=false, insertable=false)
	private String key;
	
	@Transient
	private String name;

	@Column(name = "cat_web_color")
	private String webColor;

	// bi-directional many-to-one association to IdiomCategory
	@OneToMany(mappedBy = "category")
	private List<IdiomCategory> idiomCategories;

	// bi-directional many-to-one association to Item
	@OneToMany(mappedBy = "category")
	private List<Item> items;

	@Column(name = "cat_order")
	private Integer catOrder;
	
	@Column(name = "cat_selectable")
	private Boolean selectable;
	
	public Category() {
	}

	public Long getCategoryId() {
		return this.id;
	}

	public void setCategoryId(Long categoryId) {
		this.id = categoryId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<IdiomCategory> getIdiomCategories() {
		return this.idiomCategories;
	}

	public void setIdiomCategories(List<IdiomCategory> idiomCategories) {
		this.idiomCategories = idiomCategories;
	}

	public IdiomCategory addIdiomCategory(IdiomCategory idiomCategory) {
		getIdiomCategories().add(idiomCategory);
		idiomCategory.setCategory(this);

		return idiomCategory;
	}

	public IdiomCategory removeIdiomCategory(IdiomCategory idiomCategory) {
		getIdiomCategories().remove(idiomCategory);
		idiomCategory.setCategory(null);

		return idiomCategory;
	}

	public List<Item> getItems() {
		return this.items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public Item addItem(Item item) {
		getItems().add(item);
		item.setCategory(this);

		return item;
	}

	public Item removeItem(Item item) {
		getItems().remove(item);
		item.setCategory(null);

		return item;
	}

	/**
	 * @return the webColor
	 */
	public String getWebColor() {
		return webColor;
	}

	/**
	 * @param webColor
	 *            the webColor to set
	 */
	public void setWebColor(String webColor) {
		this.webColor = webColor;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Integer getCatOrder() {
		return catOrder;
	}

	public void setCatOrder(Integer catOrder) {
		this.catOrder = catOrder;
	}

	public Boolean getSelectable() {
		return selectable;
	}

	public void setSelectable(Boolean selectable) {
		this.selectable = selectable;
	}

}