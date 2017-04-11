package com.ebay.park.service.item.validator;

import com.ebay.park.db.entity.Social;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.item.dto.ShareItemRequest;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;

@Component
public class ShareItemValidator implements ServiceValidator<ShareItemRequest> {

	@Override
	public void validate(ShareItemRequest toValidate) {
		if (Social.FACEBOOK.equalsIgnoreCase(toValidate.getSocialNetwork())) {
			toValidate.setSocialNetwork(Social.FACEBOOK);
		} else if (Social.TWITTER.equalsIgnoreCase(toValidate
				.getSocialNetwork())) {
			toValidate.setSocialNetwork(Social.TWITTER);
		} else {
			throw createServiceException(ServiceExceptionCode.INVALID_SOCIAL_NETWORK);
		}
	}

}
