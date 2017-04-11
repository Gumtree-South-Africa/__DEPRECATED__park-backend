package com.ebay.park.service.group.dto;

public class GetNewItemsInfoResponse {

	private Long ownedGroupsItems;
	private Long subscribedGroupItems;

	public GetNewItemsInfoResponse(Long ownedGroupsItems, Long subscribedGroupItems) {
		this.setOwnedGroupsItems(ownedGroupsItems);
		this.setSubscribedGroupItems(subscribedGroupItems);
	}

	public Long getOwnedGroupsItems() {
		return ownedGroupsItems;
	}

	public void setOwnedGroupsItems(Long ownedGroupsItems) {
		this.ownedGroupsItems = ownedGroupsItems;
	}

	public Long getSubscribedGroupItems() {
		return subscribedGroupItems;
	}

	public void setSubscribedGroupItems(Long subscribedGroupItems) {
		this.subscribedGroupItems = subscribedGroupItems;
	}
}