package com.ebay.park.service.device.command;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ebay.park.db.dao.UserSessionDao;
import com.ebay.park.db.entity.UserSession;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.device.dto.RemoveUserSessionsRequest;
import com.ebay.park.service.session.SessionService;


/**
 * Find userSessions in order to sign out and delete the user-session from DB.
 * @param <T>
 */
public abstract class RemoveUserSessionsCmd <T extends RemoveUserSessionsRequest>{

	@Autowired
	private SessionService sessionService;
	
	@Autowired
	protected UserSessionDao userSessionDao;
	
	public Boolean execute(T request) throws ServiceException {		
		List<UserSession> userSessionControlList = getUserSessionsToRemove(request);
				
		for (UserSession session : userSessionControlList) {
			sessionService.removeUserSession(session.getToken());
			session.getUser().removeUserSession(session);
			userSessionDao.delete(session.getId());
		}

		return true;
	}

	abstract protected List<UserSession> getUserSessionsToRemove(
			T request);
}
