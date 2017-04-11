/*
 * Copyright eBay, 2014
 */
package com.ebay.park.service.location.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This class represents the foursquare venue search response.
 * 
 * @author jpizarro
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FoursquareSearchResult {

	private Meta meta;
	private SearchVenueResponse response;

	public Meta getMeta() {
		return meta;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	public SearchVenueResponse getResponse() {
		return response;
	}

	public void setResponse(SearchVenueResponse response) {
		this.response = response;
	}

}
