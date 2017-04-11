package com.ebay.park.service.moderation.dto;

import java.util.List;

public class SendNotificationsForModerationRequest extends FilterForModerationRequest{
	
	private String message;
	private Boolean onlyPush;
	
	public SendNotificationsForModerationRequest(String parkToken, String platform, Boolean
			isGroupFollower, Boolean isGroupOwner, List<Integer >zipCodeList, 
			String accountCreationDateFrom, String accountCreationDateTo,
			Boolean hasActiveItem, Long categoryActiveItems, Boolean hasFacebook, 
			Boolean hasTwitter, Boolean isVerified, String sessionStatus) {
		super(parkToken, platform, isGroupFollower, isGroupOwner, zipCodeList, 
				accountCreationDateFrom, accountCreationDateTo,	hasActiveItem,
				categoryActiveItems, hasFacebook, hasTwitter, isVerified, sessionStatus);
	}
	
	public SendNotificationsForModerationRequest() {
		
	}
	
	
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	public Boolean getOnlyPush() {
		return onlyPush;
	}

	public void setOnlyPush(Boolean push) {
		this.onlyPush = push;
	}

	/* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SendNotificationsForModerationRequest [")
                .append(toStringFields())
                .append(", message=").append(message)
                .append(", onlyPush=").append(onlyPush)
                .append("]");
        return builder.toString();
    }
}
