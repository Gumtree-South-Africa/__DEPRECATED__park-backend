package com.ebay.park.service.rating.command;

import com.ebay.park.db.dao.RatingDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Rating;
import com.ebay.park.db.entity.RatingStatus;
import com.ebay.park.db.entity.User;
import com.ebay.park.event.user.UsersEvent;
import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.aop.Notifiable;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.rating.dto.RateUserRequest;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;
/**
 * Rates the user specified by request.userToRate.
 * 
 * @author marcos.lambolay
 */
@Component
public class RateUserCmdImpl implements RateUserCmd {

	@Autowired
	private RatingDao ratingDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private SessionService sessionService;

	@Override
	@Notifiable(action = NotificationAction.USER_RATED)
	public UsersEvent execute(RateUserRequest request) throws ServiceException {
		UserSessionCache userSession = sessionService.getUserSession(request
				.getToken());

		User user = userDao.findById(Long.parseLong(request.getUserToRate()));
		if (user == null) {
			throw createServiceException(ServiceExceptionCode.USER_NOT_FOUND);
		}

		User rater = userDao.findById(userSession.getUserId());
		if (rater == null) {
			throw createServiceException(ServiceExceptionCode.USER_NOT_FOUND);
		}

		Rating rating = ratingDao.findByUserIdAndRaterIdAndItemId(user.getId(),
				rater.getId(), Long.parseLong(request.getItemId()));

		if (StringUtils.isNotBlank(rating.getComment())
				|| rating.getStatus() != null) {
			throw createServiceException(ServiceExceptionCode.USER_ALREADY_RATED_ERROR);
		}

		rating.setComment(request.getComment());
		rating.setStatus(RatingStatus.fromDescription(request.getRatingStatus()));
		rating.setRateDate(DateTime.now().toDate());

		ratingDao.save(rating);

		return new UsersEvent(rating.getUser(), rating.getRater());
	}
}
