package com.ebay.park.service.item.dto;

import java.util.List;

import com.ebay.park.service.PaginatedResponse;

/**
 * Response to support the item ids to improving the 
 * item's transition from V.I.P.
 * @author scalderon
 * @since v2.1.0
 *
 */
public class SearchItemIdsResponse extends PaginatedResponse {
	
	private List<Long> itemIds;
	
	private String serverTime;

	public SearchItemIdsResponse (List<Long> itemIds, long totalElements,
			String serverTime) {
		super(totalElements, itemIds.size());
		this.itemIds = itemIds;
		this.serverTime = serverTime;
	}
	
	public List<Long> getItemIds() {
		return itemIds;
	}

	public void setItemIds(List<Long> itemIds) {
		this.itemIds = itemIds;
	}

	public String getServerTime() {
		return serverTime;
	}

	public void setServerTime(String serverTime) {
		this.serverTime = serverTime;
	}

}
