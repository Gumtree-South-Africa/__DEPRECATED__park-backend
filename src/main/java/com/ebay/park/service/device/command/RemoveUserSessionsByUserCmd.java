package com.ebay.park.service.device.command;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ebay.park.db.entity.UserSession;
import com.ebay.park.service.device.dto.RemoveUserSessionsByUserRequest;

@Component
public class RemoveUserSessionsByUserCmd extends RemoveUserSessionsCmd<RemoveUserSessionsByUserRequest>{

	@Override
	protected List<UserSession> getUserSessionsToRemove(
			RemoveUserSessionsByUserRequest request) {
		//Find usersessions who has the same user id, sign out this token and delete the user-session from DB.		
		return userSessionDao.findUserSessionsByUser(request.getUserId());
	}
}
