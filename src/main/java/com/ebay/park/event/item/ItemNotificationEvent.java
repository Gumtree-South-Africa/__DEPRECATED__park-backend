package com.ebay.park.event.item;

import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.User;
import com.ebay.park.notification.dto.NotifiableServiceResult;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemNotificationEvent implements NotifiableServiceResult{

	protected Item item;

	public ItemNotificationEvent(Item item) {
		Assert.notNull(item, "'item' must be not null");
		this.item = item;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
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
		return getItem().getPublishedBy().getId();
	}

	/* (non-Javadoc)
	 * @see com.ebay.park.notification.dto.NotifiableServiceResult#getItemId()
	 */
	@Override
	public Long getItemId() {
		return getItem().getId();
	}

	public List<User> getRecipients() {
		List<User> recipients = new ArrayList<User>();
		recipients.add(item.getPublishedBy());
		return recipients;
	}
}
