package com.ebay.park.service.location.dto;

import com.ebay.park.service.ListedResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author marcos.lambolay
 */
public class LocationByNameResponse extends ListedResponse {
	
	private List<Map<String, String>> locations = new ArrayList<Map<String, String>>();
	private Integer size;

	public void add(Map<String, String> element) {
		locations.add(element);
	}

	public List<Map<String, String>> getLocations() {
		return locations;
	}

	public void setLocations(List<Map<String, String>> locations) {
		this.locations = locations;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	@Override
	public boolean listIsEmpty() {
		return locations == null || locations.isEmpty();
	}

}
