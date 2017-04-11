package com.ebay.park.event.user;

import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserFollowsItem;
import com.ebay.park.event.item.ItemNotificationEvent;
import com.ebay.park.notification.dto.NotifiableServiceResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UserItemToFollowersEvent extends ItemNotificationEvent implements NotifiableServiceResult {

	/**
	 * User that made the action.
	 */
	protected User userOriginEvent;
	/**
	 * Another user who has to be excluded of notification.
	 */
	private User userExcluded;


	/**
	 * 
	 * @param item
	 * 		item related to the event
	 * @param userOriginEvent
	 * 			User that made the action. Can be null!!
	 * @param userExcluded
	 * 			Another user who has to be excluded of notification. Can be null!!
	 */
	public UserItemToFollowersEvent(Item item, User userOriginEvent, User userExcluded){
		super(item);
		this.userOriginEvent = userOriginEvent;
		this.userExcluded = userExcluded;
	}

	@Override
	public Map<String, String> toMap() {
		Map<String, String> props = new HashMap<String, String>();
		props.put(ITEM_NAME, getItem().getName());
		props.put(ITEM_ID, getItem().getId().toString());
		props.put(USER_NAME, getItem().getPublishedBy().getUsername());
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

	/* (non-Javadoc)
	 * @see com.ebay.park.notification.dto.NotifiableServiceResult#getItemId()
	 */
	@Override
	public Long getItemId() {
		return getItem().getId();
	}


	@Override
    public List<User> getRecipients() {
		List<User> recipients = new ArrayList<User>();
		for ( UserFollowsItem follower : this.getItem().getFollowers()){
			User followerUser = follower.getUser();
			if (!followerUser.equals(userOriginEvent) && !followerUser.equals(userExcluded)){
				recipients.add(followerUser);
			}
		}
		return recipients;
	}

}
