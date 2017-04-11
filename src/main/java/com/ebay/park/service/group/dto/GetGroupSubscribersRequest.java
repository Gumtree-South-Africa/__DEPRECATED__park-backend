package com.ebay.park.service.group.dto;

import com.ebay.park.service.PaginatedRequest;

public class GetGroupSubscribersRequest extends PaginatedRequest {

	private final long  groupId;

	public GetGroupSubscribersRequest(long groupId, String token, String language, Integer page,
			Integer pageSize) {
		super(token, language, page, pageSize);
		this.groupId = groupId;
	}

	public long getGroupId() {
		return groupId;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GetGroupSubscribersRequest [groupId= ")
			.append(this.groupId).append(", page= ")
			.append(this.getPage()).append(", pageSize= ")
			.append(this.getPageSize()).append(", requestTime= ")
			.append(this.getRequestTime()).append("]");
			
	return builder.toString();
	}
}
