package com.ebay.park.service.city.command;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.park.db.dao.CityDao;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.city.dto.SearchCityRequest;
import com.ebay.park.service.city.dto.SearchCityResponse;
import com.ebay.park.util.InternationalizationUtil;

/**
 * 
 * @author scalderon
 *
 */
@Component
public class SearchCityByStateCmdImpl implements SearchCityByStateCmd {

	@Autowired
	private CityDao cityDao;
	
	@Autowired
	private InternationalizationUtil i18nUtil;
	
	private static final String DEFAULT_LANG = "ES";
	
	private static final String EMPTY_LIST_MSSG = "moderation.emptylist.cities_by_state_code";
	
	@Override
	public SearchCityResponse execute(SearchCityRequest request)
			throws ServiceException {
		
		List<String> cities = cityDao.getCityDescriptionsByState(request.getState());
		
		SearchCityResponse response = new SearchCityResponse(cities,cities.size());
		i18nUtil.internationalizeListedResponse(response, EMPTY_LIST_MSSG, DEFAULT_LANG);
		return response;
	}

}
