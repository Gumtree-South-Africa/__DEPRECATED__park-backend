package com.ebay.park.service.moderationMode.dto;

import java.util.List;

public class GetUserItemsForModerationModeResponse {
	private List<ItemSummaryForModeration> items;
	private int itemsOnPage;
	private long totalOfItems;
	private int totalPages;

	/**
	 * @return the items
	 */
	public List<ItemSummaryForModeration> getItems() {
		return items;
	}
	/**
	 * @param items the items to set
	 */
	public void setItems(List<ItemSummaryForModeration> items) {
		this.items = items;
	}
	/**
	 * @return the itemsOnPage
	 */
	public int getItemsOnPage() {
		return itemsOnPage;
	}
	/**
	 * @param itemsOnPage the itemsOnPage to set
	 */
	public void setItemsOnPage(int itemsOnPage) {
		this.itemsOnPage = itemsOnPage;
	}
	/**
	 * @return the totalOfItems
	 */
	public long getTotalOfItems() {
		return totalOfItems;
	}
	/**
	 * @param l the totalOfItems to set
	 */
	public void setTotalOfItems(long l) {
		this.totalOfItems = l;
	}
	/**
	 * @return the totalPages
	 */
	public int getTotalPages() {
		return totalPages;
	}
	/**
	 * @param totalPages the totalPages to set
	 */
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
}
