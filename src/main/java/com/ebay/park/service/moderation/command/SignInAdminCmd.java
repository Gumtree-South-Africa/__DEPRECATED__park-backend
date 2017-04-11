package com.ebay.park.service.moderation.command;

import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.moderation.dto.AdminSignInRequest;
import com.ebay.park.service.moderation.dto.AdminSignInResponse;

public interface SignInAdminCmd extends
		ServiceCommand<AdminSignInRequest, AdminSignInResponse> {

	@Override
	AdminSignInResponse execute(AdminSignInRequest request)
			throws ServiceException;
}
