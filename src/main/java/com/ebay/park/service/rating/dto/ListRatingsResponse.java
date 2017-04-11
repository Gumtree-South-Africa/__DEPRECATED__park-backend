package com.ebay.park.service.rating.dto;

import com.ebay.park.service.PaginatedResponse;

import java.util.List;

public class ListRatingsResponse extends PaginatedResponse {

	private List<ListRatingResult> ratings;
	
	/**
	* Response time.
	*/
	private String serverTime;
	
	public ListRatingsResponse(List<ListRatingResult> ratings, long total) {
		super(total, ratings.size());
		this.ratings = ratings;
	}
	
	public ListRatingsResponse(List<ListRatingResult> ratings, long total, String serverTime) {
		this(ratings,total);
		this.setServerTime(serverTime);
	}
	
	/**
	* @return the serverTime
	*/
	public String getServerTime() {
		return serverTime;
	}

	/**
	* @param serverTime the serverTime to set
	*/
	public void setServerTime(String serverTime) {
		this.serverTime = serverTime;
	}
	
	public List<ListRatingResult> getRatings() {
		return ratings;
	}

	public void setRatings(List<ListRatingResult> ratings) {
		this.ratings = ratings;
	}

	@Override
	public boolean listIsEmpty() {
		return ratings == null || ratings.isEmpty();
	}

}
