package com.ebay.park.service.item.dto;

import com.ebay.park.service.ParkRequest;

/**
 * 
 * @author marcos.lambolay
 */
public class GetItemRequest extends ParkRequest {

	private String id;

	public GetItemRequest(String id, String parkToken, String lang) {
		this.id = id;
		super.setToken(parkToken);
		super.setLanguage(lang);
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GetItemRequest [id= ")
			.append(this.id).append("]");
			
	return builder.toString();
	}
}
