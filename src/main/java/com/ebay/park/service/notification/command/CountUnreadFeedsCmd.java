package com.ebay.park.service.notification.command;

import com.ebay.park.db.dao.FeedDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;

/**
 * 
 * @author Julieta Salvadó
 *
 */
@Component
public class CountUnreadFeedsCmd implements ServiceCommand<String, Long> {

	@Autowired
	private UserDao userDao;

	@Autowired
	private FeedDao feedDao;

	@Override
	public Long execute(String token) throws ServiceException {

		User user = userDao.findByToken(token);
		if (user == null) {
			throw createServiceException(ServiceExceptionCode.USER_NOT_FOUND);
		}

		return feedDao.countByOwnerAndRead(user, false);
	}

}
