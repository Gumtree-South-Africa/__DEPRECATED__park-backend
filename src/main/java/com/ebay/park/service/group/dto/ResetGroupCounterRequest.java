package com.ebay.park.service.group.dto;

import com.ebay.park.service.ParkRequest;

public class ResetGroupCounterRequest extends ParkRequest {

	private Long id;
	private Long groupId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
		builder.append("GetGroupItemsRequest [id= ")
			.append(this.id).append(", groupId= ")
			.append(this.groupId).append("]");
			
	return builder.toString();
	}

}
