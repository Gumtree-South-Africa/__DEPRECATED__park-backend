package com.ebay.park.service.moderation.validator;

import static com.ebay.park.service.ServiceException.createServiceException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.moderation.dto.FilterForModerationRequest;
import com.ebay.park.service.moderation.dto.SendNotificationsForModerationRequest;
import com.ebay.park.util.ParkConstants;

@Component
public class SendNotificationsForModerationRequestValidator extends PreFilterForModerationRequestValidator 
implements ParkConstants {

	@Value("${moderation.message_max_length}")
	private Integer maxMessageLength;
	
	public void validate(SendNotificationsForModerationRequest toValidate) {
		super.validate((FilterForModerationRequest)toValidate);
		if (toValidate != null) {			
			if (toValidate.getOnlyPush() == null) {
				throw createServiceException(ServiceExceptionCode.EMPTY_SEND_MODERATION_NOTIF_ONLYPUSH);
			}
			if (StringUtils.isBlank(toValidate.getMessage())) {
				throw createServiceException(ServiceExceptionCode.EMPTY_SEND_MODERATION_NOTIF_MESSAGE);
			}
			
			//unescape message		

			
			if (toValidate.getMessage().length() > maxMessageLength) {
				throw createServiceException(ServiceExceptionCode.INVALID_SEND_MODERATION_NOTIF_MESSAGE_LENGTH);
			}
			
		} else {
			throw createServiceException(ServiceExceptionCode.INVALID_SEND_MODERATION_NOTIF_REQ);
		}
	}

}
