package com.ebay.park.event.user;

import com.ebay.park.db.entity.User;
import com.ebay.park.notification.dto.NotifiableServiceResult;

import java.util.HashMap;
import java.util.Map;

public class UserEvent implements NotifiableServiceResult {

	private User user;
	
	public UserEvent(User user){
		this.user = user;
	}
	
	@Override
	public Map<String, String> toMap() {
		Map<String, String> props = new HashMap<String, String>();
		return props;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	/* (non-Javadoc)
	 * @see com.ebay.park.notification.dto.NotifiableServiceResult#getBasedUserId()
	 */
	@Override
	public Long getBasedUserId() {
		return getUser().getId();
	}

	/* (non-Javadoc)
	 * @see com.ebay.park.notification.dto.NotifiableServiceResult#getItemId()
	 */
	@Override
	public Long getItemId() {
		return null;
	}	

}
