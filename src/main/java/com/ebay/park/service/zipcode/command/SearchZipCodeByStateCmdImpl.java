/**
 * 
 */
package com.ebay.park.service.zipcode.command;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.park.db.dao.CityDao;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.zipcode.dto.SearchZipCodeRequest;
import com.ebay.park.service.zipcode.dto.SearchZipCodeResponse;
import com.ebay.park.util.InternationalizationUtil;

/**
 * @author jppizarro
 *
 */
@Component
public class SearchZipCodeByStateCmdImpl implements SearchZipCodeByStateCmd {

	@Autowired
	private CityDao cityDao;
	
	@Autowired
	private InternationalizationUtil i18nUtil;
	
	private static final String DEFAULT_LANG = "ES";
	
	private static final String EMPTY_LIST_MSSG = "moderation.emptylist.zip_codes_by_state_code";
	
	@Override
	public SearchZipCodeResponse execute(SearchZipCodeRequest request)
			throws ServiceException {
		
		List<String> zipCodes = cityDao.getCityZipCodesByStateCode(request.getState());
		
		SearchZipCodeResponse response = new SearchZipCodeResponse(zipCodes,zipCodes.size());
		i18nUtil.internationalizeListedResponse(response, EMPTY_LIST_MSSG, DEFAULT_LANG);
		return response;
	}

}
