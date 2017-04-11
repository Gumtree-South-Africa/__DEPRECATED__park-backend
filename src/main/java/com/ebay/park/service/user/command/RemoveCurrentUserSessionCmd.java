package com.ebay.park.service.user.command;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ebay.park.db.entity.UserSession;
import com.ebay.park.service.device.command.RemoveUserSessionsCmd;
import com.ebay.park.service.user.dto.RemoveCurrentUserSessionRequest;

/**
 * This command removes sessions from one particular user token.
 * @author Julieta Salvadó
 *
 */
@Component
public class RemoveCurrentUserSessionCmd extends RemoveUserSessionsCmd<RemoveCurrentUserSessionRequest>{

	@Override
	protected List<UserSession> getUserSessionsToRemove(
			RemoveCurrentUserSessionRequest request) {
		List<UserSession> session = new ArrayList<UserSession>();
		session.add(userSessionDao.findUserSessionByToken(request.getCurrentToken()));
		return session;
	}

}
