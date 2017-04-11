package com.ebay.park.service.group.dto;

import com.ebay.park.service.ParkRequest;

/**
 * 
 * @author federico.jaite
 */
public class SubscribeGroupRequest extends ParkRequest {

	private Long groupId;

	public SubscribeGroupRequest() {
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SubscribeGroupRequest [groupId= ")
			.append(this.groupId).append("]");
			
	return builder.toString();
	}
}
