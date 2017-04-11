/*
 * Copyright eBay, 2014
 */
package com.ebay.park.service.profile.command;

import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.profile.dto.ProfilePictureRequest;
import com.ebay.park.service.session.SessionService;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;

/**
 * @author jpizarro
 * 
 */
@Component
public class UpdateUserProfileCmd implements ServiceCommand<ProfilePictureRequest, ServiceResponse> {

	@Autowired
	private UserDao userDao;

	@Autowired
	private SessionService sessionService;

	@Override
	public ServiceResponse execute(ProfilePictureRequest request) throws ServiceException {
		if (!UrlValidator.getInstance().isValid(request.getUrl())) {
			throw createServiceException(ServiceExceptionCode.INVALID_URL);
		}

		User user = userDao.findByUsername(request.getUsername());
		if (user == null) {
			throw createServiceException(ServiceExceptionCode.USER_NOT_FOUND);
		} else {
			if (isTokenValidForUsername(request)) {
				user.setPicture(request.getUrl());
				userDao.save(user);
				return ServiceResponse.SUCCESS;
			} else {
				throw createServiceException(ServiceExceptionCode.USER_UNAUTHORIZED);
			}
		}
	}

	private boolean isTokenValidForUsername(ProfilePictureRequest request) {
		return sessionService.getUserSession(request.getToken()).getUsername().equals(request.getUsername());
	}

}
