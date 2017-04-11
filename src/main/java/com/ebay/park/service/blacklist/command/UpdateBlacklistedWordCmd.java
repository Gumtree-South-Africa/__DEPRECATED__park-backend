package com.ebay.park.service.blacklist.command;

import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.moderation.dto.BlacklistedWordRequest;

public interface UpdateBlacklistedWordCmd extends
		ServiceCommand<BlacklistedWordRequest, ServiceResponse> {

	@Override
	ServiceResponse execute(BlacklistedWordRequest request)
			throws ServiceException;
}
