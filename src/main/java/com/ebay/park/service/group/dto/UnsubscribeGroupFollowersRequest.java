package com.ebay.park.service.group.dto;

import com.ebay.park.service.ParkRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * @author gabriel.sideri
 */
public class UnsubscribeGroupFollowersRequest extends ParkRequest {

	private long  groupId;
	
	private String followersIds;

	private List<Long> followersIdsValidated;

	public void setGroupId(long groupId){
		this.groupId = groupId;
	}

	public long getGroupId(){
		return groupId;
	}

	public void setFollowersIds(String followersIds){
		this.followersIds = followersIds;
	}
	
	public String getFollowersIds(){
		return followersIds;
	}

	@JsonIgnore
	public List<Long> getFollowersIdsValidated() {
		return followersIdsValidated;
	}

	public void setFollowersIdsValidated(List<Long> followersCleanIds) {
		this.followersIdsValidated = followersCleanIds;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UnsubscribeGroupFollowersRequest [groupId= ")
			.append(this.groupId).append(", followersIds= ")
			.append(this.followersIds).append("]");
			
	return builder.toString();
	}
}
