package com.ebay.park.service.category.dto;

import com.ebay.park.db.entity.Category;
import org.apache.commons.lang.Validate;

public class SmallCategory {
	private Long id;
	private String name;
	private String color;
	private Boolean selectable;

	public SmallCategory() {

	}

	public SmallCategory(Long id, String name, String color, Boolean selectable) {
		Validate.notNull(id, "id should not be null");
		Validate.notEmpty(name, "name should not be empty");
		this.id = id;
		this.name = name;
		this.color = color;
		this.selectable = selectable;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}
	/**
	 * @param color
	 *            the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}

	public static SmallCategory fromCategory(Category category) {
		SmallCategory smallCategory = new SmallCategory();
		smallCategory.setColor(category.getWebColor());
		smallCategory.setId(category.getCategoryId());
		smallCategory.setName(category.getName());
		smallCategory.setSelectable(category.getSelectable());
		return smallCategory;
	}

	public Boolean getSelectable() {
		return selectable;
	}

	public void setSelectable(Boolean selectable) {
		this.selectable = selectable;
	}

	
}
