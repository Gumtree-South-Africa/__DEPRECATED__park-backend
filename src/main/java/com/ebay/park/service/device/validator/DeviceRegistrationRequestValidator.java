package com.ebay.park.service.device.validator;

import static com.ebay.park.service.ServiceException.createServiceException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.device.dto.DeviceRegistrationRequest;

/**
 * It validates the device registration.
 * @author Julieta Salvadó
 *
 */
@Component
public class DeviceRegistrationRequestValidator implements ServiceValidator<DeviceRegistrationRequest> {
	private static Logger logger = LoggerFactory.getLogger(DeviceRegistrationRequestValidator.class);
	
	@Override
	public void validate(DeviceRegistrationRequest toValidate) {
		if (toValidate != null) {
			if (StringUtils.isBlank(toValidate.getUniqueDeviceId())) {
				logger.error("Device Registration: Empty Unique Device Id");
				throw createServiceException(ServiceExceptionCode.EMPTY_UNIQUE_DEVICE_ID);
			}
			
			if (!StringUtils.isBlank(toValidate.getDeviceId())
					|| toValidate.getDeviceType() != null) {
				if (toValidate.getDeviceType() == null) {
					logger.error("Device Registration: Empty Device Type");
					throw createServiceException(ServiceExceptionCode.EMPTY_DEVICE_TYPE);
				}
				if (StringUtils.isBlank(toValidate.getDeviceId())) {
					logger.error("Device Registration: Empty Device Id");
					throw createServiceException(ServiceExceptionCode.EMPTY_DEVICE_ID);
				}
			}
		}
	}
}
