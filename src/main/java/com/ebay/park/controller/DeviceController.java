package com.ebay.park.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.device.DeviceService;
import com.ebay.park.service.device.dto.DeviceRegistrationRequest;
import com.ebay.park.util.ParkConstants;

@Controller
@RequestMapping(value = {"/public/device/v3","/public/device/v3.0"})
public class DeviceController implements ParkConstants {
	
	@Autowired
	private DeviceService deviceService;
	
	private static Logger logger = LoggerFactory.getLogger(DeviceController.class);
	
	@RequestMapping(value = "/registration", method = RequestMethod.POST, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public ServiceResponse registration(@RequestBody DeviceRegistrationRequest request)
			throws ServiceException {
		try {
			request.setToken(null);

			return deviceService.deviceRegistration(request);
		} catch (ServiceException e) {
            logger.error("error trying to register a device. UUID: {}, DeviceId: {}, DeviceType: {}", request.getUniqueDeviceId(), request.getDeviceId(), request.getDeviceType());
			e.setRequestToContext(request);
			throw e;
		} 
	}

}
