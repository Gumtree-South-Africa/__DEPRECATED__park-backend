/*
 * Copyright eBay, 2014
 */
package com.ebay.park.service.location.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author jpizarro
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchVenueResponse {

	private Venue[] venues;

	public Venue[] getVenues() {
		return venues;
	}

	public void setVenues(Venue[] venues) {
		this.venues = venues;
	}
	
}
