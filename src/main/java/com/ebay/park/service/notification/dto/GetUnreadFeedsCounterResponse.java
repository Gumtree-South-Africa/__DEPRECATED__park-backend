package com.ebay.park.service.notification.dto;

/**
 * Response to retrieve the unread feeds counter.
 * @author scalderon
 *
 */
public class GetUnreadFeedsCounterResponse {

	private Long unreadFeedsCounter;

	public Long getUnreadFeedsCounter() {
		return unreadFeedsCounter;
	}

	public void setUnreadFeedsCounter(Long unreadFeedsCounter) {
		this.unreadFeedsCounter = unreadFeedsCounter;
	}

}
