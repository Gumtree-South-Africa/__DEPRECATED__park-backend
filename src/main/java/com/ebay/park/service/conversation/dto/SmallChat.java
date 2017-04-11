package com.ebay.park.service.conversation.dto;

import com.ebay.park.db.entity.Chat;
import com.ebay.park.util.DataCommonUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * @author marcos.lambolay
 * 
 */
public class SmallChat {
	private Long chatId;
	private Long senderId;
	private Long receiverId;
	private String comment;
	private String hint;
	private String postTime;
	private Double offeredPrice;
	private String senderUsername;
	private String receiverUsername;
	private String action;

	public SmallChat() {
	}

	public SmallChat(Chat chat) {
		if (chat != null) {
			this.setChatId(chat.getChatId());
			this.setSenderId(chat.getSender().getId());
			this.setReceiverId(chat.getReceiver().getId());
			this.setComment(chat.getComment());
			this.setPostTime(DataCommonUtil.getDateTimeAsUnixFormat(chat.getPostTime()));
			this.setOfferedPrice(chat.getOfferedPrice());
			this.setReceiverUsername(chat.getReceiver().getUsername());
			this.setSenderUsername(chat.getSender().getUsername());
			if (chat.getAction() != null) {
				this.setAction(chat.getAction().toString());
			}
		}
	}

	@JsonInclude(Include.NON_NULL)
	public Long getChatId() {
		return chatId;
	}

	public void setChatId(Long chatId) {
		this.chatId = chatId;
	}

	@JsonInclude(Include.NON_NULL)
	public Long getSenderId() {
		return senderId;
	}

	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}
	
	@JsonInclude(Include.NON_NULL)
	public Long getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(Long receiverId) {
		this.receiverId = receiverId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	@JsonInclude(Include.NON_NULL)
	public Double getOfferedPrice() {
		return offeredPrice;
	}

	public void setOfferedPrice(Double offeredPrice) {
		this.offeredPrice = offeredPrice;
	}

	public String getPostTime() {
		return postTime;
	}

	public void setPostTime(String postTime) {
		this.postTime = postTime;
	}

	@JsonInclude(Include.NON_NULL)
	public String getSenderUsername() {
		return senderUsername;
	}

	public void setSenderUsername(String senderUsername) {
		this.senderUsername = senderUsername;
	}
	
	@JsonInclude(Include.NON_NULL)
	public String getReceiverUsername() {
		return receiverUsername;
	}

	public void setReceiverUsername(String receiverUsername) {
		this.receiverUsername = receiverUsername;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}
}
