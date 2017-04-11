package com.ebay.park.service.notification.dto;

import com.ebay.park.service.item.dto.SmallItem;
import com.ebay.park.service.item.dto.SmallUser;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.Date;

public class Feed {

	private SmallUser user;
	private SmallItem item;
	private String message;
	private Date creationDate;
	private String action;
	private Long feedId;
	private String conversationId;
	private String groupName;
	private boolean followedByUser;
	private boolean read;
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public Date getCreationDate() {
		return creationDate;
	}
	
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	public SmallUser getUser() {
		return user;
	}

	public void setUser(SmallUser user) {
		this.user = user;
	}

	public SmallItem getItem() {
		return item;
	}

	public void setItem(SmallItem item) {
		this.item = item;
	}
	
	public String getAction() {
		return action;
	}
	
	public void setAction(String action) {
		this.action = action;
	}
	
	public String getConversationId() {
		return conversationId;
	}

	public void setConversationId(String conversationId) {
		this.conversationId = conversationId; 
	}
	
	public String getGroupName() {
		return groupName;
	}
	
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	public Long getFeedId() {
		return feedId;
	}

	public void setFeedId(Long feedId) {
		this.feedId = feedId;
	}

	public boolean getFollowedByUser() {
		return followedByUser;
	}

	public void setFollowedByUser(boolean followedByUser) {
		this.followedByUser = followedByUser;
	}

	public boolean getRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}
	
}
