package com.ebay.park.service.group.dto;

import com.ebay.park.service.ParkRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * @author gabriel.sideri
 */
public class RemoveGroupItemsRequest extends ParkRequest {

	private long  groupId;
	
	private String ids;

	private List<Long> idsValidated;
	
	private boolean deleteByUsersIds;

	public boolean isDeleteByUsersIds() {
		return deleteByUsersIds;
	}


	public void setDeleteByUsersIds(boolean deleteByUsersIds) {
		this.deleteByUsersIds = deleteByUsersIds;
	}


	public RemoveGroupItemsRequest(){
		
	}
	
	
	public void setGroupId(long groupId){
		this.groupId = groupId;
	}

	public long getGroupId(){
		return groupId;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	@JsonIgnore
	public List<Long> getIdsValidated() {
		return idsValidated;
	}

	public void setIdsValidated(List<Long> idsValidated) {
		this.idsValidated = idsValidated;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RemoveGroupItemsRequest [groupId= ")
			.append(this.groupId).append(", ids= ")
			.append(this.ids).append(", isDeleteByUsersIds= ")
			.append(this.deleteByUsersIds).append("]");
			
	return builder.toString();
	}
}
