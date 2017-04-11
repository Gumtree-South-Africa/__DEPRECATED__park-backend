/*
 * Copyright eBay, 2014
 */
package com.ebay.park.service.user.command;

import com.ebay.park.db.dao.UserDao;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.user.dto.CheckValueRequest;
import com.ebay.park.service.user.dto.CheckValueResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Command class to perform a key-value check.
 * 
 * @author juan.pizarro
 * 
 */
@Component
public class CheckValueCmd implements ServiceCommand<CheckValueRequest, CheckValueResponse> {

	private static final String USERNAME = "username";

	@Autowired
	private UserDao userDao;

	@Override
	public CheckValueResponse execute(CheckValueRequest request) throws ServiceException {
		boolean exists;

		if (USERNAME.equals(request.getName().toLowerCase()))
			exists = userDao.findByUsername(request.getValue()) != null;
		else
			exists = userDao.findByEmail(request.getValue()) != null;

		return new CheckValueResponse(!exists);
	}

}
