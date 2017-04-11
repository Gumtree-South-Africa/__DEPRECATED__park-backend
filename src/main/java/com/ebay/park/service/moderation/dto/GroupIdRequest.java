package com.ebay.park.service.moderation.dto;

import com.ebay.park.service.ParkRequest;

public class GroupIdRequest extends ParkRequest {

	private Long groupId;

	public GroupIdRequest() {
		super();
	}

	public GroupIdRequest(Long groupId) {
		super();
		this.groupId = groupId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
}
