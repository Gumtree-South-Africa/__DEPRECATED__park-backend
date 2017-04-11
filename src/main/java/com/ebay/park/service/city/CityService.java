package com.ebay.park.service.city;

import com.ebay.park.service.city.dto.SearchCityRequest;
import com.ebay.park.service.city.dto.SearchCityResponse;

/**
 * 
 * @author scalderon
 *
 */
public interface CityService {
	
	/**
	 * Search and retrieves a cities given a state.
	 * 
	 * @param request
	 * @return
	 */
	public SearchCityResponse searchCitiesByState(
			SearchCityRequest request);

}
