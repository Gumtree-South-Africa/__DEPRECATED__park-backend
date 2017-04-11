package com.ebay.park.service.rating.dto;

import com.ebay.park.service.ParkRequest;

/**
 * @author gabriel.sideri
 */
public class RatingRequest extends ParkRequest {

	private Long ratingId;
	
	public RatingRequest(String token, Long ratingId) {
		super(token);
		this.ratingId = ratingId;
	}

	public Long getRatingId() {
		return ratingId;
	}

}
