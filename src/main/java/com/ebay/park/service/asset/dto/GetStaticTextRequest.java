package com.ebay.park.service.asset.dto;

import com.ebay.park.service.ParkRequest;

public class GetStaticTextRequest extends ParkRequest {
	private String template;

	public GetStaticTextRequest(){}
	
	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GetStaticTextRequest [template= ")
			.append(this.template).append("]");
		
		return builder.toString();
	}
}
