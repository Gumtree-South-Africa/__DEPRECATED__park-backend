package com.ebay.park.controller;

import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.ServiceResponseStatus;
import com.ebay.park.service.banner.BannerService;
import com.ebay.park.service.banner.dto.BannerRequest;
import com.ebay.park.util.ParkConstants;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"/banners/v3","/banners/v3.0"})
public class BannerController implements ParkConstants{

	private static Logger logger = LoggerFactory.getLogger(BannerController.class);
	
	@Autowired
	BannerService bannerService;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public ServiceResponse getBanner(
			@RequestHeader(PARK_TOKEN_HEADER) String token,
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String language)  {

		BannerRequest bannerReq = new BannerRequest();
		bannerReq.setToken(token);
		bannerReq.setLanguage(language);

		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS,
					StringUtils.EMPTY, bannerService.getBanner(bannerReq));
		} catch (ServiceException e) {
            logger.error("error trying to get a banner.Token: {}", token);
			e.setRequestToContext(bannerReq);
			throw e;
		} 
	}
	
}
