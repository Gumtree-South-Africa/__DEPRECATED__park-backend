package com.ebay.park.service.moderationMode.command;

import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.moderationMode.dto.GetUserItemsForModerationModeRequest;
import com.ebay.park.service.moderationMode.dto.GetUserItemsForModerationModeResponse;

public interface GetUserItemsForModerationModeCmd
extends ServiceCommand<GetUserItemsForModerationModeRequest, GetUserItemsForModerationModeResponse> {
	@Override
    public GetUserItemsForModerationModeResponse execute(
			GetUserItemsForModerationModeRequest request) throws ServiceException;
}
