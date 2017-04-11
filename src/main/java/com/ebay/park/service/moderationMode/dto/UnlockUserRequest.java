package com.ebay.park.service.moderationMode.dto;

import com.ebay.park.service.ParkRequest;

public class UnlockUserRequest extends ParkRequest{

	private Long itemId;

	public UnlockUserRequest(Long itemId, String parkToken, String lang) {
		this.setItemId(itemId);
		super.setToken(parkToken);
		super.setLanguage(lang);
	}

	/**
	 * @return the id
	 */
	public Long getItemId() {
		return itemId;
	}

	/**
	 * @param itemId the itemId to set
	 *            
	*/
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

}
