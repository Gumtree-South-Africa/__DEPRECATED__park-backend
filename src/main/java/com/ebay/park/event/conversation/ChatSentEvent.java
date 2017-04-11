package com.ebay.park.event.conversation;

import com.ebay.park.notification.dto.NotifiableServiceResult;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

public class ChatSentEvent implements NotifiableServiceResult {


	private Long conversationId;
	private Long itemId;
	private String itemName;
	private Long receiverId;
	private Long senderId;
	private String senderUsername;
	private String userName;
	/**
	 * URL to the item in the website.
	 */
	private String url;

	/**
	 * Event when sending a chat line.
	 * @param username
	 * 		username of the user that will be reading the new chat line
	 * @param conversationId
	 * 		id of the conversation where the new chat line will be part
	 * @param itemId
	 * 		id of the item involved in the conversation
	 * @param itemName
	 * 		name of the item involved in the conversation
	 * @param receiverId
	 * 		id of the user that will be reading the new chat line
	 * @param senderId
	 * 		id of the user sending the chat line
	 * @param senderUsername
	 * 		username of the user sending the chat line
	 * @param url
	 * 		URL to the item in the website
	 */
	public ChatSentEvent(String username, Long conversationId, Long itemId, String itemName, Long receiverId,
		Long senderId, String senderUsername, String url) {
		Assert.notNull(username, "'username' must be not null");
		Assert.notNull(conversationId, "'conversationId' must be not null");
		Assert.notNull(itemId, "'itemId' must be not null");
		Assert.notNull(itemName, "'itemName' must be not null");
		Assert.notNull(receiverId, "'receiverId' must be not null");
		Assert.notNull(senderId, "'senderId' must be not null");
		Assert.notNull(senderUsername, "'senderUsername' must be not null");
		Assert.notNull(url, "'url' must be not null");

		this.conversationId = conversationId;
		this.itemId = itemId;
		this.itemName = itemName;
		this.receiverId = receiverId;
		this.senderId = senderId;
		this.senderUsername = senderUsername;
		this.userName = username;
		this.url = url;
	}

	public Long getConversationId() {
		return conversationId;
	}

	public void setConversationId(Long conversationId) {
		this.conversationId = conversationId;
	}

	@Override
    public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Long getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(Long receiverId) {
		this.receiverId = receiverId;
	}

	public Long getSenderId() {
		return senderId;
	}

	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}

	public String getSenderUsername() {
		return senderUsername;
	}

	@Override
	public Map<String, String> toMap() {
		Map<String, String> props = new HashMap<String, String>();
		props.put(ITEM_NAME, getItemName());
		props.put(ITEM_ID, getItemId().toString());
		props.put(SENDER_USERNAME, getSenderUsername());
		props.put(CONVERSATION_ID, getConversationId().toString());
		props.put(USER_NAME, userName);
		props.put(URL, getUrl());
		return props;
	}

	/* (non-Javadoc)
	 * @see com.ebay.park.notification.dto.NotifiableServiceResult#getBasedUserId()
	 */
	@Override
	public Long getBasedUserId() {
		return getSenderId();
	}

	public String getUrl() {
		return url;
	}
}
