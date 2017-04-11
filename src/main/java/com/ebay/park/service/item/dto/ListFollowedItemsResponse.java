package com.ebay.park.service.item.dto;

import com.ebay.park.service.PaginatedResponse;

import java.util.List;

public class ListFollowedItemsResponse extends PaginatedResponse {

	private List<ItemSummary> items;
	
	//FIXME coordinate with FE use numberOfElements instead
	private int itemsOnPage;
	
	//FIXME coordinate with FE use totalElements instead
	private long totalOfItems;
	
	private int totalPages;
	
	private String serverTime;

	/**
	 * 
	 * @return the requestTime.
	 */
	public String getRequestTime() {
		return serverTime;
	}

	/**
	 * 
	 * @param serverTime the serverTime to set.
	 */
	public void setRequestTime(String serverTime) {
		this.serverTime = serverTime;
	}

	public ListFollowedItemsResponse( List<ItemSummary> items, int itemsOnPage, long totalOfItems,
			int totalPages) {
		super(totalOfItems, itemsOnPage);
		this.items = items;
		this.itemsOnPage = itemsOnPage;
		this.totalOfItems = totalOfItems;
		this.totalPages = totalPages;
	}
	
	public ListFollowedItemsResponse( List<ItemSummary> items, int itemsOnPage, long totalOfItems,
			int totalPages, String serverTime) {
		this(items,itemsOnPage,totalOfItems,totalPages);
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
	
	public int getItemsOnPage() {
		return itemsOnPage;
	}
	
	public void setItemsOnPage(int itemsOnPage) {
		this.itemsOnPage = itemsOnPage;
	}
	
	public long getTotalOfItems() {
		return totalOfItems;
	}
	
	public void setTotalOfItems(long totalOfItems) {
		this.totalOfItems = totalOfItems;
	}
	
	public int getTotalPages() {
		return totalPages;
	}
	
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	@Override
	public boolean listIsEmpty() {
		return items == null || items.isEmpty();
	}

}
