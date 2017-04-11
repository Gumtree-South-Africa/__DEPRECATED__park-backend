package com.ebay.park.service.moderation.dto;

import com.ebay.park.service.PaginatedResponse;

import java.util.List;

public class SearchItemForModerationResponse extends PaginatedResponse{

	private List<ModerationItemSummary> items;
	private int amountItemsFound;

	public SearchItemForModerationResponse( List<ModerationItemSummary> items,
			int amountItemsFound) {
		super((long) amountItemsFound, items.size());
		this.items  = items;
		this.amountItemsFound = amountItemsFound;
	}
	
	public List<ModerationItemSummary> getItems() {
		return items;
	}

	public void setItems(List<ModerationItemSummary> items) {
		this.items = items;
	}

	public int getAmountItemsFound() {
		return amountItemsFound;
	}

	public void setAmountItemsFound(int amountItemsFound) {
		this.amountItemsFound = amountItemsFound;
	}
}
