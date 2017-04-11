package com.ebay.park.service.admin.command;

import com.ebay.park.service.PaginatedRequest;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.admin.dto.ListModeratorsResponse;

public interface ListModeratorsCmd extends ServiceCommand<PaginatedRequest, ListModeratorsResponse> {

	@Override
	ListModeratorsResponse execute(PaginatedRequest request) throws ServiceException;
}
