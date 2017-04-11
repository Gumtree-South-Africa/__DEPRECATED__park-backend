/*
 * Copyright eBay, 2014
 */
package com.ebay.park.controller;

import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.ServiceResponseStatus;
import com.ebay.park.util.DoNotLog;
import com.google.common.collect.Maps;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * It provides health information that can be use to check the status of the running application.
 * @author jpizarro
 * 
 */
@RestController
public class HealthController {

	private static Map<String, String> props = new HashMap<String, String>();
	private static Object lock = new Object();
	private static Logger LOGGER = LoggerFactory.getLogger(HealthController.class);

	@RequestMapping(value ={"/health/v3/probe","/health/v3.0/probe", "/health/probe", "/boot/status"}, method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@DoNotLog // Load Balancer will ping this endpoint every 4 seconds to verify the instance is up&running. We don't want to be so verbose
	public ServiceResponse healthCheck() {
		Map<String, String> probeData = new HashMap<String, String>();
		probeData.put("status", "healthy");
		probeData.put("timestamp", DateTime.now().toString());
		if (props.isEmpty()) {
			synchronized (lock) {
				if (props.isEmpty()) {
					Properties properties = new Properties();
					try {
						properties.load(this.getClass().getClassLoader().getResourceAsStream("version.properties"));
						props.putAll(Maps.fromProperties(properties));
					} catch (Exception e) {
						LOGGER.error("could not load version.properties", e);
					}
				}
			}
		}
		probeData.putAll(props);
		return new ServiceResponse(ServiceResponseStatus.SUCCESS, "health check", probeData);
	}
}
