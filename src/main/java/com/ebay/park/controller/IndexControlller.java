/**
 * Copyright eBay, 2014
 */
package com.ebay.park.controller;


import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.ServiceResponseStatus;
import com.ebay.park.service.index.IndexService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


/**
 * @author diana.gazquez
 *
 */

@RestController
@RequestMapping("/index")
public class IndexControlller {

	private static final String INDEX_TOKEN = "indexToken";
	private static final Logger LOGGER = LoggerFactory.getLogger(IndexControlller.class);

	@Value("${search.index.token.secret}")
	private String tokenSecret;
	
	@Autowired
	IndexService indexService;

	@RequestMapping(value = "reindex", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public ServiceResponse reindex(@RequestHeader(INDEX_TOKEN) String indexToken) {
		
		LOGGER.info("Full Elasticsearch re index requested");

		if (!tokenSecret.equals(indexToken)) {
			return new ServiceResponse(ServiceResponseStatus.FAIL, "Invalid index token", null);
		}

		if (indexService.reindex()) {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, "Re index finished", null);
		} else {

			return new ServiceResponse(ServiceResponseStatus.FAIL,
					"Re index start failed. Is this a master node?", null);
		}
	}

}
