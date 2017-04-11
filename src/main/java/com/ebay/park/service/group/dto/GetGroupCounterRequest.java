package com.ebay.park.service.group.dto;

import com.ebay.park.service.item.dto.SmallGroupDTO;

import java.util.List;

public class GetGroupCounterRequest {

	private List<SmallGroupDTO> groupsDTO;
	private Long userId;

	public GetGroupCounterRequest(List<SmallGroupDTO> groupsDTO, Long userId) {
		this.groupsDTO = groupsDTO;
		this.setUserId(userId);
	}

	public List<SmallGroupDTO> getGroups() {
		return groupsDTO;		
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GetGroupCounterRequest [userId= ")
			.append(this.userId).append("]");
		
		return builder.toString();
	}

}
