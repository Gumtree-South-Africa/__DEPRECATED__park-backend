package com.ebay.park.controller;

import com.ebay.park.service.PaginatedRequest;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.ServiceResponseStatus;
import com.ebay.park.service.admin.AdminService;
import com.ebay.park.service.admin.dto.AddModeratorRequest;
import com.ebay.park.service.admin.dto.UpdateModeratorRequest;
import com.ebay.park.service.moderation.dto.UserIdRequest;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AdminController.ADMIN_PATH)
public class AdminController {

	private static Logger logger = LoggerFactory.getLogger(AdminController.class);
	
	public static final String ADMIN_PATH = ModerationController.MODERATION_PATH + "/admin";

	@Autowired
	AdminService adminService;

	@RequestMapping(value = "/moderator", method = RequestMethod.POST, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.CREATED)
	public ServiceResponse addModerator(@RequestBody AddModeratorRequest request) {
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
					adminService.addModerator(request));
		} catch (ServiceException e) {
			logger.error("error trying to add a moderator");
			throw e;
		}
	}

	@RequestMapping(value = "/moderator/{id}", method = RequestMethod.PUT, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse updateModerator(@PathVariable Long id,
			@RequestBody UpdateModeratorRequest request) {
		try {
			request.setId(id);
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
					adminService.updateModerator(request));
		} catch (ServiceException e) {
            logger.error("error trying to update moderator ID: {}", id);
			throw e;
		} 
	}

	@RequestMapping(value = "/moderator/{id}", method = RequestMethod.DELETE, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse remnoveModerator(@PathVariable Long id) {
		try {
			UserIdRequest request = new UserIdRequest(id);
			return adminService.removeModerator(request);
		} catch (ServiceException e) {
            logger.error("error trying to remove moderator ID: {}", id);
			throw e;
		} 
	}

	@RequestMapping(value = "/moderator", method = RequestMethod.GET, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse listModerators(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize) {
		try {
			PaginatedRequest request = new PaginatedRequest(page, pageSize);
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
					adminService.listModerators(request));
		} catch (ServiceException e) {
			logger.error("error trying to list moderators");
			throw e;
		} 
	}
}
