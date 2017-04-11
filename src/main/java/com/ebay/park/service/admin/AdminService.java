package com.ebay.park.service.admin;

import com.ebay.park.service.PaginatedRequest;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.admin.dto.AddModeratorRequest;
import com.ebay.park.service.admin.dto.ListModeratorsResponse;
import com.ebay.park.service.admin.dto.SmallUserAdmin;
import com.ebay.park.service.admin.dto.UpdateModeratorRequest;
import com.ebay.park.service.moderation.dto.UserIdRequest;

public interface AdminService {

	public SmallUserAdmin addModerator(AddModeratorRequest request) throws ServiceException;

	public SmallUserAdmin updateModerator(UpdateModeratorRequest request) throws ServiceException;

	public ServiceResponse removeModerator(UserIdRequest request) throws ServiceException;

	public ListModeratorsResponse listModerators(PaginatedRequest request) throws ServiceException;
}
