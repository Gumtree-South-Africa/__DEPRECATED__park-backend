package com.ebay.park.service.item.command;

import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.item.dto.ListUserItemsRequest;
import com.ebay.park.service.item.dto.ListUserItemsResponse;

public interface ListUserItemsCmd extends
		ServiceCommand<ListUserItemsRequest, ListUserItemsResponse> {

}
