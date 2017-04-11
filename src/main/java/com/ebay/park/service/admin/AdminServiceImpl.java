package com.ebay.park.service.admin;

import com.ebay.park.service.PaginatedRequest;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.admin.command.AddModeratorCmd;
import com.ebay.park.service.admin.command.ListModeratorsCmd;
import com.ebay.park.service.admin.command.RemoveModeratorCmd;
import com.ebay.park.service.admin.command.UpdateModeratorCmd;
import com.ebay.park.service.admin.dto.AddModeratorRequest;
import com.ebay.park.service.admin.dto.ListModeratorsResponse;
import com.ebay.park.service.admin.dto.SmallUserAdmin;
import com.ebay.park.service.admin.dto.UpdateModeratorRequest;
import com.ebay.park.service.moderation.dto.UserIdRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static com.ebay.park.service.ServiceException.createServiceException;

@Component
public class AdminServiceImpl implements AdminService {

	@Autowired
	private AddModeratorCmd addModeratorCmd;

	@Autowired
	private UpdateModeratorCmd updateModeratorCmd;

	@Autowired
	private RemoveModeratorCmd removeModeratorCmd;

	@Autowired
	private ListModeratorsCmd listModeratorsCmd;

	@Override
	public SmallUserAdmin addModerator(AddModeratorRequest request) throws ServiceException {

		if (StringUtils.isEmpty(request.getUsername())) {
			throw createServiceException(ServiceExceptionCode.EMPTY_USERNAME);
		}

		if (StringUtils.isEmpty(request.getEmail())) {
			throw createServiceException(ServiceExceptionCode.EMPTY_EMAIL);
		}

		if (StringUtils.isEmpty(request.getPassword())) {
			throw createServiceException(ServiceExceptionCode.EMPTY_PASSWORD);
		}

		return addModeratorCmd.execute(request);
	}

	@Override
	public SmallUserAdmin updateModerator(UpdateModeratorRequest request) throws ServiceException {
		return updateModeratorCmd.execute(request);
	}

	@Override
	public ServiceResponse removeModerator(UserIdRequest request) throws ServiceException {
		return removeModeratorCmd.execute(request);
	}

	@Override
	public ListModeratorsResponse listModerators(PaginatedRequest request) throws ServiceException {
		return listModeratorsCmd.execute(request);
	}

}
