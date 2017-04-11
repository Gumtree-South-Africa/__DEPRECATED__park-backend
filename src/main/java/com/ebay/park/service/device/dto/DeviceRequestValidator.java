package com.ebay.park.service.device.dto;

import com.ebay.park.db.entity.DeviceType;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceValidator;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;

/**
 * Validates a DeviceRequest {@link DeviceRequest}
 * 
 * @author lucia.masola
 * 
 */

@Component
public class DeviceRequestValidator implements ServiceValidator<DeviceRequest> {


	@Override
	public void validate(DeviceRequest toValidate) {
		if (toValidate != null) {
			if (StringUtils.isNotBlank(toValidate.getDeviceId()) || StringUtils.isNotBlank(toValidate.getDeviceType())) {
				if (StringUtils.isBlank(toValidate.getDeviceId())) {
					throw createServiceException(ServiceExceptionCode.EMPTY_DEVICE_ID);
				}

				if (StringUtils.isBlank(toValidate.getDeviceType())) {
					throw createServiceException(ServiceExceptionCode.EMPTY_DEVICE_TYPE);
				}

				if (!DeviceType.getDevices().contains(toValidate.getDeviceType())) {
					throw createServiceException(ServiceExceptionCode.INVALID_DEVICE_TYPE);
				}
			}
		}

	}

	
}
