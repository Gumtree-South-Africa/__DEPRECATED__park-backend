package com.ebay.park.service.city;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebay.park.service.ServiceException;
import com.ebay.park.service.city.command.SearchCityByStateCmd;
import com.ebay.park.service.city.dto.SearchCityRequest;
import com.ebay.park.service.city.dto.SearchCityResponse;

/**
 * 
 * @author scalderon
 *
 */
@Service
public class CityServiceImpl implements CityService {
	
	@Autowired
	private SearchCityByStateCmd searchCityByStateCmd;
	
	@Override
	public SearchCityResponse searchCitiesByState(SearchCityRequest request)
			throws ServiceException {
		return searchCityByStateCmd.execute(request);
	}


}
