/**
 * Copyright eBay, 2014
 */
package com.ebay.park.service.zipcode;

import com.ebay.park.service.zipcode.dto.SearchZipCodeByStateAndCityRequest;
import com.ebay.park.service.zipcode.dto.SearchZipCodeRequest;
import com.ebay.park.service.zipcode.dto.SearchZipCodeResponse;

/**
 * @author jppizarro
 */
public interface ZipCodeService {

	/**
	 * Search and retrieves zip codes given a state.
	 * 
	 * @param request
	 * @return
	 */
	public SearchZipCodeResponse searchZipCodesByState(
			SearchZipCodeRequest request);

	public SearchZipCodeResponse searchZipCodesByStateAndCity(
			SearchZipCodeByStateAndCityRequest request);

}
