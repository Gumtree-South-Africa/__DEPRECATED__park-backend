package com.ebay.park.service.moderation.dto;


/**
 * Request to delete an item from moderation. It has a defined reason.
 * @author Julieta Salvadó
 *
 */
public class RejectItemRequest extends ItemRequest {

	private int reasonId;
	
	public RejectItemRequest(Long itemId, int reasonId) {
		super(itemId);
		setReasonId(reasonId);
	}

	/**
	 * @return the reasonId
	 */
	public int getReasonId() {
		return reasonId;
	}

	/**
	 * @param reasonId the reasonId to set
	 */
	public void setReasonId(int reasonId) {
		this.reasonId = reasonId;
	}
}
