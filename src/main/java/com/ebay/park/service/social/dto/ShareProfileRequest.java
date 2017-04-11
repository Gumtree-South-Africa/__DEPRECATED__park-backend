package com.ebay.park.service.social.dto;

import com.ebay.park.service.ParkRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class ShareProfileRequest extends ParkRequest{

	private String usernameToShare;
	
	@JsonIgnore
	private String sharerUsername;
	
	public ShareProfileRequest(){		
	}

	public String getUsernameToShare() {
		return usernameToShare;
	}

	public void setUsernameToShare(String usernameToShare) {
		this.usernameToShare = usernameToShare;
	}

	public String getSharerUsername() {
		return sharerUsername;
	}

	public void setSharerUsername(String sharerUsername) {
		this.sharerUsername = sharerUsername;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ShareProfileRequest [")
			.append("usernameToShare= ").append(this.usernameToShare)
			.append(", sharerUsername= ").append(this.sharerUsername)
			.append("]");
		
		return builder.toString();
	}
	
}
