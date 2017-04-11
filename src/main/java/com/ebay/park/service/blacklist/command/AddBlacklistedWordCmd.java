package com.ebay.park.service.blacklist.command;

import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.blacklist.dto.BlacklistedWord;
import com.ebay.park.service.moderation.dto.BlacklistedWordRequest;

public interface AddBlacklistedWordCmd extends
		ServiceCommand<BlacklistedWordRequest, BlacklistedWord> {

	@Override
	BlacklistedWord execute(BlacklistedWordRequest request)
			throws ServiceException;
}
