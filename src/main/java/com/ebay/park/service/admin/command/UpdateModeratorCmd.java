package com.ebay.park.service.admin.command;

import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.admin.dto.SmallUserAdmin;
import com.ebay.park.service.admin.dto.UpdateModeratorRequest;

public interface UpdateModeratorCmd extends ServiceCommand<UpdateModeratorRequest, SmallUserAdmin> {

	@Override
	SmallUserAdmin execute(UpdateModeratorRequest request) throws ServiceException;
}
