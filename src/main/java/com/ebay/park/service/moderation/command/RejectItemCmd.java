package com.ebay.park.service.moderation.command;

import org.springframework.stereotype.Component;

import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.moderation.dto.RejectItemRequest;

/**
 * Interface for item rejection execution.
 * @author Julieta Salvadó
 *
 */
@Component
public interface RejectItemCmd extends
		ServiceCommand<RejectItemRequest, ServiceResponse> {

	@Override
	ServiceResponse execute(RejectItemRequest request) throws ServiceException;
}
