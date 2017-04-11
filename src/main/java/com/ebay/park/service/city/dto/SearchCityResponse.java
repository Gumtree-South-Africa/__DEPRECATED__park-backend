package com.ebay.park.service.city.dto;

import java.util.ArrayList;
import java.util.List;

import com.ebay.park.service.ListedResponse;

public class SearchCityResponse extends ListedResponse {
	
	private List<String> cities = new ArrayList<>();
	
	private int totalElements;
	
	public SearchCityResponse(List<String> cities, int totalElements) {
		this.cities = cities;
		this.totalElements = totalElements;
	}

	public List<String> getCities() {
		return cities;
	}

	public void setCities(List<String> cities) {
		this.cities = cities;
	}
	
	public int getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(int totalElements) {
		this.totalElements = totalElements;
	}

	@Override
	public boolean listIsEmpty() {
		return cities.isEmpty();
	}

}
