package com.ebay.park.event.conversation;

import com.ebay.park.notification.dto.NotifiableServiceResult;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;


public class ConversationAcceptedEvent implements NotifiableServiceResult {

	
	private Long userIdThatAccepted;
	private String usernameThatAccepted;
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
	 * Sets the values regarding the acceptation.
	 * @param username
	 * 			Name of the user that made the offer.
	 * @param userIdThatAccepted
	 * 			Id of the user accepting the offer.
	 * @param usernameThatAccepted
	 * 			Name of the user accepting the offer.
	 * @param userIdToNotify
	 * 			Id of the user that made the offer.
	 * @param itemId
	 * 			Id of the item sold.
	 * @param itemName
	 * 			Name of the item sold.
	 * @param conversationId
	 * 			Id of the conversation related to the offer.
	 * @param url
	 * 			URL to the item page in the website.
	 */
	public ConversationAcceptedEvent(String username, Long userIdThatAccepted, String usernameThatAccepted, Long userIdToNotify,
			Long itemId, String itemName, Long conversationId, String url) {
		Assert.notNull(username, "'username' must be not null");
		Assert.notNull(userIdThatAccepted, "'userIdThatAccepted' must be not null");
		Assert.notNull(usernameThatAccepted, "'usernameThatAccepted' must be not null");
		Assert.notNull(userIdToNotify, "'userIdToNotify' must be not null");
		Assert.notNull(itemId, "'itemId' must be not null");
		Assert.notNull(itemName, "'itemName' must be not null");
		Assert.notNull(conversationId, "'conversationId' must be not null");
		Assert.notNull(url, "'url' must be not null");

		this.userIdThatAccepted = userIdThatAccepted;
		this.usernameThatAccepted = usernameThatAccepted;
		this.userIdToNotify = userIdToNotify;
		this.itemId = itemId;
		this.itemName = itemName;
		this.conversationId = conversationId;
		this.userName = username;
		this.url = url;
	}

	public Long getUserIdToNotify() {
		return userIdToNotify;
	}

	public String getItemName() {
		return itemName;
	}

	@Override
	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Long getConversationId() {
		return conversationId;
	}

	public void setConversationId(Long conversationId) {
		this.conversationId = conversationId;
	}

	public void setUserIdToNotify(Long userIdToNotify) {
		this.userIdToNotify = userIdToNotify;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Long getUserIdThatAccepted() {
		return userIdThatAccepted;
	}

	public void setUserIdThatAccepted(Long userIdThatAccepted) {
		this.userIdThatAccepted = userIdThatAccepted;
	}

	public String getUsernameThatAccepted() {
		return usernameThatAccepted;
	}

	public void setUsernameThatAccepted(String usernameThatAccepted) {
		this.usernameThatAccepted = usernameThatAccepted;
	}

	@Override
	public Map<String, String> toMap() {

		Map<String, String> map = new HashMap<String, String>();
		map.put(USER_ID_THAT_ACCEPTED, userIdThatAccepted.toString());
		map.put(USERNAME_THAT_ACCEPTED, getUsernameThatAccepted());
		map.put(USER_ID_TO_NOTIFY, userIdToNotify.toString());
		map.put(ITEM_ID, itemId.toString());
		map.put(ITEM_NAME, itemName);
		map.put(CONVERSATION_ID, getConversationId().toString());
		map.put(USER_NAME, userName);
		map.put(URL, url);
		return map;
	}

	/* (non-Javadoc)
	 * @see com.ebay.park.notification.dto.NotifiableServiceResult#getBasedUserId()
	 */
	@Override
	public Long getBasedUserId() {
		return getUserIdThatAccepted();
	}

	public String getUrl() {
		return url;
	}
}
