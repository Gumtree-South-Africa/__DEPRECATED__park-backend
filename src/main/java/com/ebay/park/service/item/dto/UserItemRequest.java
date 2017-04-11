package com.ebay.park.service.item.dto;

import com.ebay.park.service.ParkRequest;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class UserItemRequest extends ParkRequest {

	private Long itemId;

	public UserItemRequest() {

	}

	public UserItemRequest(String token) {
		super(token);
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	/*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("UserItemRequest [itemId=")
                .append(this.itemId)
                .append(", token=")
                .append(this.getToken())
                .append(", language=")
                .append(this.getLanguage())
                .append("]");
        return builder.toString();
    }
}
