package com.ebay.park.event.user;

import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserFollowsItem;
import com.ebay.park.event.item.ItemNotificationEvent;
import com.ebay.park.notification.dto.NotifiableServiceResult;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class InterestedUserItemToFollowersEvent extends ItemNotificationEvent
implements NotifiableServiceResult {

	/**
	 * User that made the action.
	 */
	private User userOriginEvent;

	/**
	 * URL to the item in the website.
	 */
	private String url;

	/**
	 * 
	 * @param item
	 * 		item related to the event
	 * @param userOriginEvent
	 * 			User that made the action. Can be null!!
	 * @param url
	 * 			url to the item website page
	 */
	public InterestedUserItemToFollowersEvent(Item item, User userOriginEvent, String url){
		super(item);
		Assert.notNull(url, "'url' must be not null");
		this.userOriginEvent = userOriginEvent;
		this.url = url;
	}

	@Override
	public Map<String, String> toMap() {
		Map<String, String> props = new HashMap<String, String>();
		props.put(ITEM_NAME, getItem().getName());
		props.put(ITEM_ID, getItem().getId().toString());
		props.put(SENDER_USERNAME, getItem().getPublishedBy().getUsername());
		props.put(URL, getUrl());
		return props;
	}

	/* (non-Javadoc)
	 * @see com.ebay.park.notification.dto.NotifiableServiceResult#getBasedUserId()
	 */
	@Override
	public Long getBasedUserId() {
		if (userOriginEvent == null){
			return null;
		}
		return userOriginEvent.getId();
	}

	public String getUrl() {
		return url;
	}

	/* (non-Javadoc)
	 * @see com.ebay.park.notification.dto.NotifiableServiceResult#getItemId()
	 */
	@Override
	public Long getItemId() {
		return getItem().getId();
	}


	@Override
    public List<User> getRecipients() {
		return this.getItem().getFollowers().stream()
		    .map(UserFollowsItem::getUser)
		    .filter(follower -> !follower.equals(userOriginEvent)
		            && getItem().hasConversationWithUser(follower))
		    .collect(Collectors.toList());
	}
}
