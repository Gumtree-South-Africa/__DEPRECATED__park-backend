package com.ebay.park.service.moderation.dto;

import com.ebay.park.service.ParkRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class ItemRequest extends ParkRequest {

	@JsonIgnore
	private Long itemId;

	public ItemRequest() {
		super();
	}

	public ItemRequest(Long itemId) {
		super();
		this.itemId = itemId;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
}
