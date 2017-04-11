package com.ebay.park.service.moderation.command;

import java.util.List;
import org.springframework.stereotype.Component;

import com.ebay.park.service.moderation.UserReceiverPush;
import com.ebay.park.service.moderation.dto.FilterForModerationRequest;
import com.ebay.park.service.moderation.dto.SendNotificationsForModerationResponse;

@Component
public class PreFilterForModerationCmd  extends AbstractFiltererCmd<FilterForModerationRequest>{

	@Override
	public SendNotificationsForModerationResponse execute(
			FilterForModerationRequest request) {
		List<UserReceiverPush> results = getFilteredUsers(request);	
		SendNotificationsForModerationResponse response = new SendNotificationsForModerationResponse();
		response.setReceivers(results.size());
		return response;
	}
}
