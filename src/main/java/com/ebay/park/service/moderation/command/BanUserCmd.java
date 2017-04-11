package com.ebay.park.service.moderation.command;

import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.moderation.dto.UserIdRequest;

public interface BanUserCmd extends
		ServiceCommand<UserIdRequest, ServiceResponse> {

	@Override
	ServiceResponse execute(UserIdRequest request) throws ServiceException;
}
