package com.ebay.park.service.group.validator;

import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.group.dto.ShareGroupOnSocialRequest;
import com.ebay.park.util.ParkConstants;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;

@Component
public class ShareGroupValidator implements
		ServiceValidator<ShareGroupOnSocialRequest>, ParkConstants {

	@Override
	public void validate(ShareGroupOnSocialRequest toValidate) {

		if (!toValidate.isShareOnFacebook() && !toValidate.isShareOnTwitter()) {
			throw createServiceException(ServiceExceptionCode.INVALID_SOCIAL_NETWORK);
		}

	}

}
