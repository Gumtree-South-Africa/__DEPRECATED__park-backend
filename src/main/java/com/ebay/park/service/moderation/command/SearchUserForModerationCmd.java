package com.ebay.park.service.moderation.command;

import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.moderation.dto.SearchUserForModerationRequest;
import com.ebay.park.service.moderation.dto.SearchUserForModerationResponse;

public interface SearchUserForModerationCmd
		extends
		ServiceCommand<SearchUserForModerationRequest, SearchUserForModerationResponse> {

	@Override
	SearchUserForModerationResponse execute(
			SearchUserForModerationRequest request) throws ServiceException;
}
