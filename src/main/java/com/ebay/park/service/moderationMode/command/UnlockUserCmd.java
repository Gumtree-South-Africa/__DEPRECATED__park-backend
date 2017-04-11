package com.ebay.park.service.moderationMode.command;

import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.moderationMode.dto.UnlockUserRequest;

/**
 * Interface to unlock a user.
 *
 */
public interface UnlockUserCmd extends 
	ServiceCommand<UnlockUserRequest, ServiceResponse> {

	@Override
    public ServiceResponse execute(UnlockUserRequest request) throws ServiceException;

}
