package com.ebay.park.service.item.dto;

public class RepublishItemRequest extends UserItemRequest {
	
	/**
	 * The user has the option to republish an item with 
	 * the same price or 10% less
	 * @see <a href=https://jira.globant.com/browse/EPA001-10504 />
	 */
	private Double price;

	public RepublishItemRequest(String token) {
		super(token);
	}
	
	public RepublishItemRequest() {
		super();
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
}
