package com.ebay.park.service.device;

import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.device.dto.DeviceRegistrationRequest;

public interface DeviceService {

	public abstract ServiceResponse deviceRegistration(DeviceRegistrationRequest request);
}
