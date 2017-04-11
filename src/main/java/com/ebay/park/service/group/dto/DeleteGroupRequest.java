package com.ebay.park.service.group.dto;

import com.ebay.park.service.ParkRequest;

public class DeleteGroupRequest extends ParkRequest {

	private Long id;

	public DeleteGroupRequest(Long id, String parkToken, String lang) {
		this.id = id;
		super.setToken(parkToken);
		super.setLanguage(lang);
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DeleteGroupRequest [id= ")
			.append(this.id).append("]");
		
		return builder.toString();
	}

}
