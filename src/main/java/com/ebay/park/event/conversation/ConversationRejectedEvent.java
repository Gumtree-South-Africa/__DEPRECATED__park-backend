package com.ebay.park.event.conversation;

import com.ebay.park.notification.dto.NotifiableServiceResult;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

public class ConversationRejectedEvent implements NotifiableServiceResult {

	private Long userIdThatRejected;
	private String usernameThatRejected;
	private Long userIdToNotify;
	private Long itemId;
	private String itemName;
	private Long conversationId;
	private String userName;

	/**
	 * URL to the item in the website.
	 */
	private String url;

	/**
	 * Event for conversation rejection.
	 * @param username the username from the rejected user
	 * @param userIdThatRejected the user id from the rejector
	 * @param usernameThatRejected the username from the rejector
	 * @param userIdToNotify the user id from the rejected user
	 * @param itemId the id from the involved item
	 * @param itemName the name from the involved item
	 * @param conversationId the id the rejected conversation
	 * @param url the url to the item
	 */
	public ConversationRejectedEvent(String username, Long userIdThatRejected, String usernameThatRejected,
			Long userIdToNotify, Long itemId, String itemName, Long conversationId, String url) {
		Assert.notNull(username, "'username' must be not null");
		Assert.notNull(userIdThatRejected, "'userIdThatRejected' must be not null");
		Assert.notNull(usernameThatRejected, "'usernameThatRejected' must be not null");
		Assert.notNull(userIdToNotify, "'userIdToNotify' must be not null");
		Assert.notNull(itemId, "'itemId' must be not null");
		Assert.notNull(itemName, "'itemName' must be not null");
		Assert.notNull(conversationId, "'conversationId' must be not null");
 		Assert.notNull(url, "'url' must be not null");

		this.userIdThatRejected = userIdThatRejected;
		this.usernameThatRejected = usernameThatRejected;
		this.userIdToNotify = userIdToNotify;
		this.itemId = itemId;
		this.itemName = itemName;
		this.conversationId = conversationId;
		this.userName = username;
		this.url = url;
	}

	public Long getUserIdThatRejected() {
		return userIdThatRejected;
	}

	public void setUserIdThatRejected(Long userIdThatRejected) {
		this.userIdThatRejected = userIdThatRejected;
	}

	public String getUsernameThatRejected() {
		return usernameThatRejected;
	}

	public void setUsernameThatRejected(String usernameThatRejected) {
		this.usernameThatRejected = usernameThatRejected;
	}

	public Long getUserIdToNotify() {
		return userIdToNotify;
	}

	public void setUserIdToNotify(Long userIdToNotify) {
		this.userIdToNotify = userIdToNotify;
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

	public Long getConversationId() {
		return conversationId;
	}

	public void setConversationId(Long conversationId) {
		this.conversationId = conversationId;
	}

	@Override
	public Map<String, String> toMap() {
		Map<String, String> props = new HashMap<String, String>();
		props.put(ITEM_NAME, getItemName());
		props.put(ITEM_ID, getItemId().toString());
		props.put(USERNAME_THAT_REJECTED, getUsernameThatRejected());
		props.put(USER_NAME, userName);
		props.put(CONVERSATION_ID, getConversationId().toString());
		props.put(URL, getUrl());
		return props;
	}

	/* (non-Javadoc)
	 * @see com.ebay.park.notification.dto.NotifiableServiceResult#getBasedUserId()
	 */
	@Override
	public Long getBasedUserId() {
		return getUserIdThatRejected();
	}

	public String getUrl() {
		return url;
	}
}
