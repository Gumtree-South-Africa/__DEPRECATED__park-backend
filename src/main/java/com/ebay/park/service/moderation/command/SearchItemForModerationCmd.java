package com.ebay.park.service.moderation.command;

import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.moderation.dto.SearchItemForModerationRequest;
import com.ebay.park.service.moderation.dto.SearchItemForModerationResponse;

public interface SearchItemForModerationCmd
		extends
		ServiceCommand<SearchItemForModerationRequest, SearchItemForModerationResponse> {

	@Override
	SearchItemForModerationResponse execute(
			SearchItemForModerationRequest request) throws ServiceException;
}
