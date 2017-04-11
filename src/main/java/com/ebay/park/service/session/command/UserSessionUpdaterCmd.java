package com.ebay.park.service.session.command;

import com.ebay.park.db.dao.UserSessionDao;
import com.ebay.park.db.entity.UserSession;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;
import com.ebay.park.service.session.dto.UserSessionUpdaterRequest;
import com.ebay.park.util.DataCommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Updates the last time a session is used.
 * @author Julieta Salvadó
 *
 */
@Component
public class UserSessionUpdaterCmd implements ServiceCommand<UserSessionUpdaterRequest, Void>{
	
	@Autowired
	private UserSessionDao userSessionDao;
	
	@Autowired
	private SessionService sessionService;

	@Override
	public Void execute(UserSessionUpdaterRequest request)
			throws ServiceException {
		UserSessionCache userSessionCache = request.getUserSessionCache();

		//if the date needs to be updated
		if (userSessionCache != null && DataCommonUtil.areDifferentDays(userSessionCache.getLastActivityDay(),
				DataCommonUtil.getCurrentTime())) {
			Date newDate = DataCommonUtil.getCurrentTime();
			updateOnCache(request, userSessionCache, newDate);
			updateOnDatabase(request, newDate);
		}
		return null;
	}

	private void updateOnDatabase(UserSessionUpdaterRequest request, Date newDate) {
		UserSession userSession = userSessionDao.findUserSessionByToken(request.getToken());
		userSession.setLastSuccessfulLogin(newDate);
		userSessionDao.save(userSession);
	}

	private void updateOnCache(UserSessionUpdaterRequest request, UserSessionCache userSessionCache, Date newDate) {
		userSessionCache.setLastActivityDay(newDate);
		sessionService.saveUserSession(request.getToken(), userSessionCache);
	}

}
