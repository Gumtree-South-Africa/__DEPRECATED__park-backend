package com.ebay.park.service.conversation.dto;

import com.ebay.park.service.ParkRequest;

/**
 * 
 * @author marcos.lambolay
 */
public class SendChatRequest extends ParkRequest {

	private Long itemId;
	private String comment;
	private Long conversationId;
	private String brandPublish;
	private String versionPublish;

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getBrandPublish() {
		return brandPublish;
	}

	public void setBrandPublish(String brandPublish) {
		this.brandPublish = brandPublish;
	}

	public String getVersionPublish() {
		return versionPublish;
	}

	public void setVersionPublish(String versionPublish) {
		this.versionPublish = versionPublish;
	}

	public Long getConversationId() {
		return conversationId;
	}

	public void setConversationId(Long conversationId) {
		this.conversationId = conversationId;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SendChatRequest [itemId= ")
			.append(this.itemId).append(", comment= ")
			.append(this.comment).append(", conversationId= ")
			.append(this.conversationId).append(", brandPublish= ")
			.append(this.brandPublish).append(", versionPublish= ")
			.append(this.versionPublish).append("]");
		
		return builder.toString();
	}

}
