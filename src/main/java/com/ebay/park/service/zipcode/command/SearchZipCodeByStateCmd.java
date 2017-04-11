package com.ebay.park.service.zipcode.command;

import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.zipcode.dto.SearchZipCodeRequest;
import com.ebay.park.service.zipcode.dto.SearchZipCodeResponse;

public interface SearchZipCodeByStateCmd extends
		ServiceCommand<SearchZipCodeRequest, SearchZipCodeResponse> {

	@Override
	SearchZipCodeResponse execute(SearchZipCodeRequest request)
			throws ServiceException;

}
