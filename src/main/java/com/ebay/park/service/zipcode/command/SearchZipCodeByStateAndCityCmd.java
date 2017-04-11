package com.ebay.park.service.zipcode.command;

import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.zipcode.dto.SearchZipCodeByStateAndCityRequest;
import com.ebay.park.service.zipcode.dto.SearchZipCodeResponse;

public interface SearchZipCodeByStateAndCityCmd extends
		ServiceCommand<SearchZipCodeByStateAndCityRequest, SearchZipCodeResponse> {

	@Override
	SearchZipCodeResponse execute(SearchZipCodeByStateAndCityRequest request)
			throws ServiceException;

}
