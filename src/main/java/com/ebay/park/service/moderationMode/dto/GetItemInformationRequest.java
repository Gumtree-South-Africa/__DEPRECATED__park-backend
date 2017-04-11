package com.ebay.park.service.moderationMode.dto;

import com.ebay.park.service.ParkRequest;

/**
 * 
 * @author scalderon
 *
 */
public class GetItemInformationRequest extends ParkRequest{

	private Long itemId;

	public GetItemInformationRequest(Long itemId, String parkToken, String lang) {
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
     * It set an item id.
     * @param itemId id to set
     */
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

}
