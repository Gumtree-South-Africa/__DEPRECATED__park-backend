package com.ebay.park.event.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserFollowsItem;
import com.ebay.park.event.item.ItemNotificationEvent;
import com.ebay.park.notification.dto.NotifiableServiceResult;


public class NotInterestedUserItemToFollowersEvent extends ItemNotificationEvent
implements NotifiableServiceResult {

	/**
	 * User that made the action.
	 */
	protected User userOriginEvent;

	/**
	 * 
	 * @param item
	 * 		item related to the event
	 * @param userOriginEvent
	 * 			User that made the action. Can be null!!
	 */
	public NotInterestedUserItemToFollowersEvent(Item item, User userOriginEvent){
		super(item);
		this.userOriginEvent = userOriginEvent;
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
		return this.getItem().getFollowers().stream()
		    .map(UserFollowsItem::getUser)
		    .filter(follower -> !follower.equals(userOriginEvent)
		            && !getItem().hasConversationWithUser(follower))
		    .collect(Collectors.toList());
	}
}
