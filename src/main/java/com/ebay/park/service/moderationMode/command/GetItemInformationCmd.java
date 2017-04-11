package com.ebay.park.service.moderationMode.command;

import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.moderationMode.dto.GetItemInformationRequest;
import com.ebay.park.service.moderationMode.dto.GetItemInformationResponse;

/**
 * Command to get an item for moderate.
 * @author scalderon
 *
 */
public interface GetItemInformationCmd extends 
						ServiceCommand<GetItemInformationRequest, GetItemInformationResponse>{

	
}
