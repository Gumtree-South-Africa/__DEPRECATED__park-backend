package com.ebay.park.service.device.dto;

public class RemoveUserSessionsByUserRequest extends RemoveUserSessionsRequest{

	private Long userId;
	
	public RemoveUserSessionsByUserRequest(Long id) {
		this.setUserId(id);
	}

	private void setUserId(Long id) {
		userId = id;
	}

	public Long getUserId() {
		return userId;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RemoveUserSessionsByUserRequest [userId= ")
			.append(this.userId).append("]");
		
		return builder.toString();
	}

}
