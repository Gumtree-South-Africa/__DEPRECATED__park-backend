package com.ebay.park.service.item.dto;

/**
 * DTO to send product catalog to Facebook Business
 * @see https://www.facebook.com/business/help/1397294963910848
 * 
 * @author scalderon
 *
 */
public class FacebookBusinessItem {
	
	private Long id;
	//hardcoded value
	private String availability = "in stock";
	//hardcoded value
	private String condition = "used";
	private String description;
	/**
	 * The ePS link
	 */
	private String imageLink;
	/**
	 * The web link
	 */
	private String link;
	private String title;
	private String price;
	//hardcoded value
	private String brand = "N/A";
	private String googleProductCategory;
	
	public FacebookBusinessItem() {

	}
	
	public FacebookBusinessItem(Long id, String description, String imageLink, 
			String link, String title, String price, String googleProductCategory) {
		this.id = id;
		this.description = description;
		this.imageLink = imageLink;
		this.link = link;
		this.title = title;
		this.price = price;
		this.googleProductCategory = googleProductCategory;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public String getAvailability() {
		return availability;
	}
	
	public String getCondition() {
		return condition;
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageLink() {
		return imageLink;
	}
	
	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}

	public String getLink() {
		return link;
	}
	
	public void setLink(String link) {
		this.link = link;
	}

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public String getPrice() {
		return price;
	}
	
	public void setPrice(String price) {
		this.price = price;
	}

	public String getBrand() {
		return brand;
	}

	public String getGoogleProductCategory() {
		return googleProductCategory;
	}

	public void setGoogleProductCategory(String googleProductCategory) {
		this.googleProductCategory = googleProductCategory;
	}
	
}
