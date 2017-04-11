package com.ebay.park.service.moderationMode.command;

import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.moderationMode.dto.ApplyFiltersForModerationModeRequest;
import com.ebay.park.service.moderationMode.dto.ApplyFiltersForModerationModeResponse;

public interface ApplyFiltersForModerationModeCmd extends ServiceCommand<ApplyFiltersForModerationModeRequest, 
ApplyFiltersForModerationModeResponse> {
	@Override
    public ApplyFiltersForModerationModeResponse execute(ApplyFiltersForModerationModeRequest
			request) throws ServiceException;
}
