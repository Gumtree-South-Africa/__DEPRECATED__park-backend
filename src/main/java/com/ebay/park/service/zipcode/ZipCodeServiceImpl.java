/**
 * Copyright eBay, 2014
 */
package com.ebay.park.service.zipcode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebay.park.service.ServiceException;
import com.ebay.park.service.zipcode.command.SearchZipCodeByStateAndCityCmd;
import com.ebay.park.service.zipcode.command.SearchZipCodeByStateCmd;
import com.ebay.park.service.zipcode.dto.SearchZipCodeByStateAndCityRequest;
import com.ebay.park.service.zipcode.dto.SearchZipCodeRequest;
import com.ebay.park.service.zipcode.dto.SearchZipCodeResponse;

/**
 * @author jppizarro
 */
@Service
public class ZipCodeServiceImpl implements ZipCodeService {


	@Autowired
	private SearchZipCodeByStateCmd searchZipCodeByStateCmd;
	
	@Autowired
	private SearchZipCodeByStateAndCityCmd searchZipCodeByStateAndCityCmd;
	
	@Override
	public SearchZipCodeResponse searchZipCodesByState(SearchZipCodeRequest request)
			throws ServiceException {
		return searchZipCodeByStateCmd.execute(request);
	}

	@Override
	public SearchZipCodeResponse searchZipCodesByStateAndCity(SearchZipCodeByStateAndCityRequest request) 
			throws ServiceException{
		return searchZipCodeByStateAndCityCmd.execute(request);
	}

}
