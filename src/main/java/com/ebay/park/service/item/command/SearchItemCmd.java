package com.ebay.park.service.item.command;

import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.item.dto.SearchItemRequest;
import com.ebay.park.service.item.dto.SearchItemResponse;

public interface SearchItemCmd extends ServiceCommand<SearchItemRequest, SearchItemResponse> {

	@Override
	SearchItemResponse execute(SearchItemRequest param) throws ServiceException;
}
