package com.ebay.park.controller;

import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ebay.park.service.ParkRequest;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.item.ItemService;
import com.ebay.park.util.ItemListToCsvMapper;
import com.ebay.park.util.ParkConstants;

@RestController
public class FacebookBusinessController implements ParkConstants {
	
	private static Logger logger = LoggerFactory.getLogger(FacebookBusinessController.class);

	@Autowired
	private ItemService itemService;

	
	@Autowired
	private ItemListToCsvMapper itemListToCsvMapper;


	/**
	 * This endpoint is used to feed Facebook Business product catalog
	 */
	@RequestMapping(value = "/public/others/facebookbusiness/items", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@Produces("text/csv")
	public String getFacebookBusinessItemsCsv() {
		ParkRequest request = new ParkRequest();
		request.setLanguage(LANG_ENG);
				
		try {
			return itemListToCsvMapper
					.getActiveItemsCommaDelimited(itemService.getFacebookBusinessItems(request));
			
		} catch (ServiceException e) {
			logger.error("error trying to get Facebook Business items.");
			e.setRequestToContext(request);
			throw e;
		} 
	}

	
}
