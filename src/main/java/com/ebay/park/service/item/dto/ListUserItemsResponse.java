package com.ebay.park.service.item.dto;

import java.util.List;

public class ListUserItemsResponse {

	private List<ItemSummary> items;
	private int itemsOnPage;
	private long totalOfItems;
	private int totalPages;
	private String serverTime;

	/**
	 * 
	 * @return the serverTime.
	 */
	public String getServerTime() {
		return serverTime;
	}

	/**
	 * 
	 * @param serverTime the serverTime to set.
	 */
	public void setServerTime(String serverTime) {
		this.serverTime = serverTime;
	}

	public ListUserItemsResponse() {
		super();
	}
	
	public ListUserItemsResponse(String serverTime) {
		this.serverTime = serverTime;
	}

	/**
	 * @return the items
	 */
	public List<ItemSummary> getItems() {
		return items;
	}

	/**
	 * @param items
	 *            the items to set
	 */
	public void setItems(List<ItemSummary> items) {
		this.items = items;
	}

	/**
	 * @return the itemsOnPage
	 */
	public int getItemsOnPage() {
		return itemsOnPage;
	}

	/**
	 * @param itemsOnPage
	 *            the itemsOnPage to set
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
	 * @param totalOfItems
	 *            the totalOfItems to set
	 */
	public void setTotalOfItems(long totalOfItems) {
		this.totalOfItems = totalOfItems;
	}

	/**
	 * @return the totalPages
	 */
	public int getTotalPages() {
		return totalPages;
	}

	/**
	 * @param totalPages
	 *            the totalPages to set
	 */
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
}
