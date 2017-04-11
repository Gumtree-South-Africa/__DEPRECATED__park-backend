package com.ebay.park.service.conversation.validator;

import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.conversation.dto.SendChatRequest;
import com.ebay.park.service.conversation.dto.SendOfferRequest;
import com.ebay.park.util.ParkConstants;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;

@Component
/**
 * It validates a SendChatRequest.
 *
 */
public class SendOfferValidator implements ServiceValidator<SendChatRequest> {

	/**
	 * @param toValidate
	 *     the incoming request. It <b>mustn't</b> be null.
	 * @throws BAD_PRICE_FORMAT if:
	 * <li>invalid integer digit price lenght</li>
	 * <li> invalid decimal digit price lenght </li>
	 * <li> invalid price lenght</li>
	 */
	@Override
    public void validate(SendChatRequest toValidate) {
		if(toValidate instanceof SendOfferRequest) {
			SendOfferRequest offReq = (SendOfferRequest) toValidate;
			String number = offReq.getOfferedPrice();

			String[] s = number.split("\\.");
			if ((s[0].length() > ParkConstants.MAX_INTEGER_DIGITS_FOR_PRICES)) {
				throw createServiceException(ServiceExceptionCode.BAD_PRICE_FORMAT);
			}
			if (s.length == 2 && (s[1].length() > ParkConstants.MAX_DECIMAL_DIGITS_FOR_PRICES)) {
				throw createServiceException(ServiceExceptionCode.BAD_PRICE_FORMAT);
			}

			if (s.length > 2) {
				throw createServiceException(ServiceExceptionCode.BAD_PRICE_FORMAT);
			}
		}
		
	}

}