package com.ebay.park.service.moderationMode.dto;

import java.util.List;

import com.ebay.park.service.PaginatedResponse;

/**
 * Response containing item ids that result of applying filters and search items of pending moderation.
 * @author Julieta Salvadó
 *
 */
public class ApplyFiltersForModerationModeResponse extends PaginatedResponse{

	private List<Long> items;
	private int amountItemsFound;
	
	public ApplyFiltersForModerationModeResponse(List<Long> items, int amountItemsFound) {
		super((long) amountItemsFound, items.size());
		this.setItems(items);
		this.setAmountItemsFound(amountItemsFound);
	}

	/**
	 * @return the items
	 */
	public List<Long> getItems() {
		return items;
	}

	/**
	 * @param items the items to set
	 */
	public void setItems(List<Long> items) {
		this.items = items;
	}

	/**
	 * @return the amountItemsFound
	 */
	public int getAmountItemsFound() {
		return amountItemsFound;
	}

	/**
	 * @param amountItemsFound the amountItemsFound to set
	 */
	public void setAmountItemsFound(int amountItemsFound) {
		this.amountItemsFound = amountItemsFound;
	}
}
