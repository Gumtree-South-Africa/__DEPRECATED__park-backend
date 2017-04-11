package com.ebay.park.service.category.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author marcos.lambolay
 */
public class ListCategoriesResponse {

	private List<SmallCategory> categories = new ArrayList<SmallCategory>();

	public ListCategoriesResponse() {
	}

	public void add(SmallCategory sc) {
		categories.add(sc);
	}
	
	/**
	 * @return the categories
	 */
	public List<SmallCategory> getCategories() {
		return categories;
	}

	/**
	 * @param categories
	 *            the categories to set
	 */
	public void setCategories(List<SmallCategory> categories) {
		this.categories = categories;
	}


}
