package com.ebay.park.service.notification.validator;


import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.notification.dto.NotificationConfigRequest;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;

@Component
public class NotificationRequestValidator implements ServiceValidator<NotificationConfigRequest> {

	@Override
	public void validate(NotificationConfigRequest toValidate) {

		if (toValidate == null) {
			throw createServiceException(ServiceExceptionCode.INVALID_NOT_REQ);
		}

		if (toValidate.getUsername() == null) {
			throw createServiceException(ServiceExceptionCode.EMPTY_USERNAME);
		}

		if (toValidate.getUserConfigurationPerGroup() == null) {
			throw createServiceException(ServiceExceptionCode.INVALID_NOTIFICATION_PROPERTIES);
		}

	}

}
