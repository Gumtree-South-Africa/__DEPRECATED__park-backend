package com.ebay.park.service.moderationMode.command;

import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.moderationMode.dto.UpdateItemForModerationModeRequest;


public interface UpdateItemForModerationModeCmd extends ServiceCommand<UpdateItemForModerationModeRequest,
ServiceResponse> {
	@Override
    public ServiceResponse execute(UpdateItemForModerationModeRequest request) throws ServiceException;
}
