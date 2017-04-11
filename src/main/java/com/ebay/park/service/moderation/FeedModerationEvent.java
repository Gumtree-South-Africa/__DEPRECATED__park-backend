package com.ebay.park.service.moderation;

import com.ebay.park.notification.dto.NotifiableServiceResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gabriel.sideri
 */
public class FeedModerationEvent implements NotifiableServiceResult {

	private String message;
	
	private List<Long> recipients;

	public FeedModerationEvent(List<Long> recipients, String message) {
		this.recipients = recipients;
		this.message = message;
	}

	@Override
	public Map<String, String> toMap() {
		Map<String, String> props = new HashMap<String, String>();
		props.put(MODERATION_MESSAGE, message);
		return props;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ebay.park.notification.dto.NotifiableServiceResult#getBasedUserId()
	 */
	@Override
	public Long getBasedUserId() {
		return null;
	}

	@Override
	public Long getItemId() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Long> getRecipients() {
		return recipients;
	}

}
