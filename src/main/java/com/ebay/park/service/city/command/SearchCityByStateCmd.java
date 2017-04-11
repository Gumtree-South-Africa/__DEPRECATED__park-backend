package com.ebay.park.service.city.command;

import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.city.dto.SearchCityRequest;
import com.ebay.park.service.city.dto.SearchCityResponse;

public interface SearchCityByStateCmd extends
		ServiceCommand<SearchCityRequest, SearchCityResponse> {

	@Override
	SearchCityResponse execute(SearchCityRequest request)
			throws ServiceException;
}
