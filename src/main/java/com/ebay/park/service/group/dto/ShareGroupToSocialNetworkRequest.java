package com.ebay.park.service.group.dto;

import com.ebay.park.service.ParkRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ShareGroupToSocialNetworkRequest extends ParkRequest {

	@JsonProperty(value = "social_network")
	private String socialNetwork;

	@JsonIgnore
	private Long groupId;

	public ShareGroupToSocialNetworkRequest() {
	}

	public ShareGroupToSocialNetworkRequest(Long groupId, String token,
			String socialNetwork) {
		this.groupId = groupId;
		this.socialNetwork = socialNetwork;
		setToken(token);
	}

	public String getSocialNetwork() {
		return socialNetwork;
	}

	public void setSocialNetwork(String socialNetwork) {
		this.socialNetwork = socialNetwork;
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
		builder.append("ShareGroupToSocialNetworkRequest [groupId= ")
			.append(this.groupId).append(", socialNetwork= ")
			.append(this.socialNetwork).append("]");
			
	return builder.toString();
	}
}
