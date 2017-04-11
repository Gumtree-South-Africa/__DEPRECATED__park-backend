package com.ebay.park.service.group.dto;

import com.ebay.park.service.ParkRequest;

/**
 * @author gabriel.sideri
 */
public class ShareGroupOnSocialRequest extends ParkRequest {

	private Long groupId;
	
	private boolean shareOnFacebook;
	
	private boolean shareOnTwitter;

	public boolean isShareOnFacebook() {
		return shareOnFacebook;
	}

	public void setShareOnFacebook(boolean shareOnFacebook) {
		this.shareOnFacebook = shareOnFacebook;
	}

	public boolean isShareOnTwitter() {
		return shareOnTwitter;
	}

	public void setShareOnTwitter(boolean shareOnTwitter) {
		this.shareOnTwitter = shareOnTwitter;
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
		builder.append("ShareGroupOnSocialRequest [groupId= ")
			.append(this.groupId).append(", shareOnFacebook= ")
			.append(this.shareOnFacebook).append(", shareOnTwitter= ")
			.append(this.shareOnTwitter).append("]");
			
	return builder.toString();
	}
}
