package com.ebay.park.service.moderation.validator;

import static com.ebay.park.service.ServiceException.createServiceException;

import org.springframework.stereotype.Component;

import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.moderation.dto.RejectItemRequest;
import com.ebay.park.service.moderation.rejection.RejectItemType;
import com.ebay.park.util.ParkConstants;

/**
 * It validates the request for item rejection.
 * @author Julieta Salvadó
 *
 */
@Component
public class RejectItemValidator implements ServiceValidator<RejectItemRequest>, ParkConstants {

	private static int MIN_REASON_VALUE = 0;
	
	@Override
	public void validate(RejectItemRequest toValidate) {
		if (toValidate != null) {
			if (toValidate.getItemId() == null) {
				throw createServiceException(ServiceExceptionCode.INVALID_REJECT_ITEM_ID_REQ);
			}
			int reasonId = toValidate.getReasonId();
			if (reasonId < MIN_REASON_VALUE || reasonId > RejectItemType.getSize() - 1) {
				throw createServiceException(ServiceExceptionCode.INVALID_REJECT_ITEM_REASON_REQ);
			}
		} else {
			throw createServiceException(ServiceExceptionCode.INVALID_REJECT_ITEM_REQ);
		}
	}

}
