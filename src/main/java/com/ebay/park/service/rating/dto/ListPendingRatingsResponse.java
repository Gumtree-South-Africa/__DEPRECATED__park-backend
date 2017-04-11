package com.ebay.park.service.rating.dto;

import com.ebay.park.service.PaginatedResponse;

import java.util.List;

public class ListPendingRatingsResponse extends PaginatedResponse {

	private List<ListPendingRatingResult> pendingRatings;
	private String serverTime;

	/**
	 * 
	 * @return the serverTime.
	 */
	public String getServerTime() {
		return serverTime;
	}

	/**
	 * 
	 * @param serverTime
	 *            the serverTime to set.
	 */
	public void setServerTime(String serverTime) {
		this.serverTime = serverTime;
	}

	public ListPendingRatingsResponse(List<ListPendingRatingResult> pendingRatings, long totalElements) {
		super(totalElements, pendingRatings.size());
		this.pendingRatings = pendingRatings;
	}

	public ListPendingRatingsResponse(List<ListPendingRatingResult> pendingRatings, long totalElements,
			String serverTime) {
		this(pendingRatings, totalElements);
		this.serverTime = serverTime;
	}

	public List<ListPendingRatingResult> getPendingRatings() {
		return pendingRatings;
	}

	public void setPendingRatings(List<ListPendingRatingResult> pendingRatings) {
		this.pendingRatings = pendingRatings;
	}

	@Override
	public boolean listIsEmpty() {
		return pendingRatings == null || pendingRatings.isEmpty();
	}
}
