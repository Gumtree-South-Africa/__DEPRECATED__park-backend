package com.ebay.park.service.notification.dto;

import com.ebay.park.service.ListedResponse;

import java.util.List;

public class GetFeedsResponse extends ListedResponse {

	private List<Feed> feeds;
	
	public GetFeedsResponse(List<Feed> feeds) {
		super();
		this.feeds = feeds;
	}
	

	public List<Feed> getFeeds() {
		return feeds;
	}

	public void setFeeds(List<Feed> feeds) {
		this.feeds = feeds;
	}
	
	@Override
	public boolean listIsEmpty() {
		return feeds == null || feeds.isEmpty();
	}

	
	
}
