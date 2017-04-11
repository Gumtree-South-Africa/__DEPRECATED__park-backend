package com.ebay.park.service.admin.command;

import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.admin.dto.AddModeratorRequest;
import com.ebay.park.service.admin.dto.SmallUserAdmin;

public interface AddModeratorCmd extends ServiceCommand<AddModeratorRequest, SmallUserAdmin> {

	@Override
	SmallUserAdmin execute(AddModeratorRequest request) throws ServiceException;
}
