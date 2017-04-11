package com.ebay.park.service.user.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.device.command.RemoveUserSessionsByUserCmd;
import com.ebay.park.service.device.dto.RemoveUserSessionsByUserRequest;
import com.ebay.park.service.user.dto.ChangePwdRequest;
import com.ebay.park.service.user.dto.ChangePwdResponse;
import com.ebay.park.util.PasswdUtil;

/**
 * @author federico.jaite
 * 
 */
@Component
public class ChangePwdCmd implements
		ServiceCommand<ChangePwdRequest, ChangePwdResponse> {

	@Autowired
	private UserDao userDao;

	@Autowired
	private PasswdUtil passwdUtil;
	
	@Autowired
	private RemoveUserSessionsByUserCmd removeUserSessionsByUserCmd;

	@Override
	public ChangePwdResponse execute(ChangePwdRequest param)
			throws ServiceException {
		User user = userDao.findByToken(param.getToken());
		removeUserSessionsByUserCmd.execute(new RemoveUserSessionsByUserRequest(user.getId()));

		//Change Password
		user.setPassword(passwdUtil.hash(param.getNewPassword()));

		//TODO: US EPA001-4865
		//Reactive session for CURRENT device
		
		userDao.save(user);		
		return new ChangePwdResponse(true);
	}

}
