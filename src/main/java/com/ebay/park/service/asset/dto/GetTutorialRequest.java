package com.ebay.park.service.asset.dto;

import com.ebay.park.service.ParkRequest;

public class GetTutorialRequest extends ParkRequest {

	private Integer step;

	public GetTutorialRequest() {
	}

	public GetTutorialRequest(String token) {
		this.setToken(token);
	}

	public Integer getStep() {
		return step;
	}

	public void setStep(Integer step) {
		this.step = step;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GetTutorialRequest [step= ")
			.append(this.step).append("]");
		
		return builder.toString();
	}
}
