package com.ebay.park.service.item.dto;

import com.ebay.park.service.ParkRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ShareItemRequest extends ParkRequest {

	@JsonProperty(value = "social_network")
	private String socialNetwork;

	@JsonIgnore
	private Long itemId;

	public ShareItemRequest() {
	}
	
	public ShareItemRequest(Long itemId, String token, String socialNetwork) {
		this.itemId = itemId;
		this.socialNetwork = socialNetwork;
		setToken(token);
	}

	public String getSocialNetwork() {
		return socialNetwork;
	}

	public void setSocialNetwork(String socialNetwork) {
		this.socialNetwork = socialNetwork;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ShareItemRequest [socialNetwork= ")
			.append(this.socialNetwork).append(", itemId= ")
			.append(this.itemId).append("]");
			
	return builder.toString();
	}

}
