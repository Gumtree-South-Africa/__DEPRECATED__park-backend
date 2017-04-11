package com.ebay.park.service.item.dto;

import com.ebay.park.service.category.dto.SmallCategory;

/**
 * Summary of an item containing the most general information.
 * @author Julieta Salvadó
 *
 */
public class AbstracterItemSummary {

	private Long id;
	private String name;
	private String description;
	private Double price;
	private String URL;
	private Integer totalOfFollowers;
	private SmallCategory category;
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
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
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the price
	 */
	public Double getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(Double price) {
		this.price = price;
	}
	/**
	 * @return the url
	 */
	public String getURL() {
		return URL;
	}
	/**
	 * @param url the url to set
	 */
	public void setURL(String url) {
		this.URL = url;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the totalOfFollowers
	 */
	public Integer getTotalOfFollowers() {
		return totalOfFollowers;
	}
	/**
	 * @param totalOfFollowers the totalOfFollowers to set
	 */
	public void setTotalOfFollowers(Integer totalOfFollowers) {
		this.totalOfFollowers = totalOfFollowers;
	}
	/**
	 * @return the category
	 */
	public SmallCategory getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(SmallCategory category) {
		this.category = category;
	}
}
