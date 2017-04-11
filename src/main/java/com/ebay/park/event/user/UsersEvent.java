package com.ebay.park.event.user;

import com.ebay.park.db.entity.User;
import com.ebay.park.notification.dto.NotifiableServiceResult;

import java.util.HashMap;
import java.util.Map;

public class UsersEvent implements NotifiableServiceResult {

	private User basedUser;
	private User userActionGenerator;
	
	public UsersEvent(User userToNotify, User userActionGenerator){
		this.basedUser = userToNotify;
		this.userActionGenerator = userActionGenerator;
	}
	
	@Override
	public Map<String, String> toMap() {
		Map<String, String> props = new HashMap<String, String>();
		props.put(USER_NAME, getUserActionGenerator().getUsername());
		props.put(RATER, getUserActionGenerator().getUsername()); //mail template
		return props;
	}

	public User getBasedUser() {
		return basedUser;
	}

	public void setBasedUser(User basedUser) {
		this.basedUser = basedUser;
	}

	public User getUserActionGenerator() {
		return userActionGenerator;
	}

	public void setUserActionGenerator(User userActionGenerator) {
		this.userActionGenerator = userActionGenerator;
	}

	/* (non-Javadoc)
	 * @see com.ebay.park.notification.dto.NotifiableServiceResult#getBasedUserId()
	 */
	@Override
	public Long getBasedUserId() {
		return getUserActionGenerator().getId();
	}

	/* (non-Javadoc)
	 * @see com.ebay.park.notification.dto.NotifiableServiceResult#getItemId()
	 */
	@Override
	public Long getItemId() {
		return null;
	}	
	
}
