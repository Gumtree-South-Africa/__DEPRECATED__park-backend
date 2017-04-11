package com.ebay.park.service.notification.validator;

import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.notification.dto.NotificationTemplateRequest;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;

@Component
public class NotTemplateReqValidator implements ServiceValidator<NotificationTemplateRequest>{

	@Override
	public void validate(NotificationTemplateRequest toValidate) {
		
		if (toValidate == null){
			throw createServiceException(ServiceExceptionCode.INVALID_NOT_TEMPLATE_REQ);
		}

		
		if (toValidate.getNotificationType() == null){
			throw createServiceException(ServiceExceptionCode.EMPTY_NOTIFICATION_TYPE);
		}
		
		if (toValidate.getNotificationType() == null){
			throw createServiceException(ServiceExceptionCode.EMPTY_NOTIFICATION_ACTION);
		}

	}


}
