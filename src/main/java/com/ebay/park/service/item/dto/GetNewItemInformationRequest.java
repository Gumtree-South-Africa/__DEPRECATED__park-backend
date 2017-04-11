package com.ebay.park.service.item.dto;

import java.util.List;

import com.ebay.park.service.ParkRequest;

public class GetNewItemInformationRequest extends ParkRequest {

	private List<ItemSummary> itemsDTO;
	private Long userId;
	private Long groupId;

	public GetNewItemInformationRequest(List<ItemSummary> itemsDTO, Long userId, Long groupId) {
		this.setItemsDTO(itemsDTO);
		this.setUserId(userId);
		this.setGroupId(groupId);
	}

	public List<ItemSummary> getItemsDTO() {
		return itemsDTO;
	}

	public void setItemsDTO(List<ItemSummary> itemsDTO) {
		this.itemsDTO = itemsDTO;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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
		builder.append("GetNewItemInformationRequest [groupId= ")
			.append(this.groupId).append(", userId= ")
			.append(this.userId).append("]");
			
	return builder.toString();
	}
}
