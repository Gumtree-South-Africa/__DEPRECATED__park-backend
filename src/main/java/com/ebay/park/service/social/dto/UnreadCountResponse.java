package com.ebay.park.service.social.dto;

/**
 * 
 * @author Julieta Salvadó
 *
 */
public class UnreadCountResponse {

	private Long unreadFeeds;
	private Long unreadGroupItems;

	public void setUnreadFeeds(Long countUnreadFeeds) {
		this.unreadFeeds = countUnreadFeeds;
	}
	
	public Long getUnreadFeeds() {
		return this.unreadFeeds;
	}

	public void setUnreadGroupItems(Long countUnreadGroupItems) {
		this.unreadGroupItems = countUnreadGroupItems;		
	}
	
	public Long getUnreadGroupItems() {
		return unreadGroupItems;
	}

}
