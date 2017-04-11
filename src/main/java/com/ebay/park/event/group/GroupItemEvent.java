package com.ebay.park.event.group;

import com.ebay.park.db.entity.Group;
import com.ebay.park.db.entity.Item;
import com.ebay.park.notification.dto.NotifiableServiceResult;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.HashMap;
import java.util.Map;

public class GroupItemEvent implements NotifiableServiceResult{

	private Item item;
	private Group group;
	
	public GroupItemEvent(Group group, Item item) {
		this.group = group;
		this.item = item;
	}

	public Item getItem() {
		return item;
	}
	
	public void setItem(Item item) {
		this.item = item;
	}
	
	public Group getGroup() {
		return group;
	}
	
	public void setGroup(Group group) {
		this.group = group;
	}
	
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public Map<String, String> toMap() {
		Map<String, String> props = new HashMap<String, String>();
		props.put(ITEM_NAME, getItem().getName());
		props.put(GROUP_NAME, getGroup().getName());
		return props;
	}
	public String getFeedProperties() {
		StringBuilder feedProperties = new StringBuilder();
		feedProperties.append(GROUP_NAME).append(":").append(getGroup().getName());
		feedProperties.append(";");
		feedProperties.append(ITEM_NAME).append(":").append(getItem().getName());
		return feedProperties.toString();
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
	
}
