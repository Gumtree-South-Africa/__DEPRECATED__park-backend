package com.ebay.park.service.item.dto;

import com.ebay.park.service.PaginatedResponse;

import java.util.List;

public class SearchItemResponse extends PaginatedResponse {

	private List<ItemSummary> items;
	
	private String serverTime;


	public SearchItemResponse (List<ItemSummary> items, long totalElements,
			String serverTime) {
		super(totalElements, items.size());
		this.items = items;
		this.serverTime = serverTime;
	}
	
	public List<ItemSummary> getItems() {
		return items;
	}

	public void setItems(List<ItemSummary> items) {
		this.items = items;
	}

	public String getServerTime() {
		return serverTime;
	}

	public void setServerTime(String serverTime) {
		this.serverTime = serverTime;
	}

	@Override
	public boolean listIsEmpty() {
		return items == null || items.isEmpty();
	}
}
