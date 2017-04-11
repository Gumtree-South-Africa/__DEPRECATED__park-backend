package com.ebay.park.service.admin.command;

import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.moderation.dto.UserIdRequest;

public interface RemoveModeratorCmd extends ServiceCommand<UserIdRequest, ServiceResponse> {

	@Override
	ServiceResponse execute(UserIdRequest request) throws ServiceException;
}
