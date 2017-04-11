package com.ebay.park.service.moderation.command;

import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.moderation.dto.ContactUserRequest;

public interface ContactUserCmd extends
		ServiceCommand<ContactUserRequest, ServiceResponse> {
}
