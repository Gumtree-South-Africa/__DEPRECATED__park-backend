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
public class Venue {

	private String id;
	private String name;
	private Location location;
	private VenueStats stats;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public VenueStats getStats() {
		return stats;
	}

	public void setStats(VenueStats stats) {
		this.stats = stats;
	}

}
