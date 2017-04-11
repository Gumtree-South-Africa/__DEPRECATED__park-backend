package com.ebay.park.service.device;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.device.command.DeviceRegistrationCmd;
import com.ebay.park.service.device.dto.DeviceRegistrationRequest;
import com.ebay.park.service.device.validator.DeviceRegistrationRequestValidator;

@Component
public class DeviceServiceImpl implements DeviceService {
	
	@Autowired
	private DeviceRegistrationCmd deviceRegistrationCmd;

	@Autowired
	private DeviceRegistrationRequestValidator deviceRegistrationReqValidator;
	
	@Override
	public ServiceResponse deviceRegistration(DeviceRegistrationRequest request) {
		deviceRegistrationReqValidator.validate(request);
		return deviceRegistrationCmd.execute(request);
	}

}
