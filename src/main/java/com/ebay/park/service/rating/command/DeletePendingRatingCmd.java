package com.ebay.park.service.rating.command;

import com.ebay.park.db.dao.RatingDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Rating;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.rating.dto.RatingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;
/**
 * @author gabriel.sideri
 */
@Component
public class DeletePendingRatingCmd {

	@Autowired
	private RatingDao ratingDao;

	@Autowired
	private UserDao userDao;

	public ServiceResponse execute(RatingRequest request) {

		User user = userDao.findByToken(request.getToken());

		if (user == null) {
			throw createServiceException(ServiceExceptionCode.USER_NOT_FOUND);
		}

		Rating rating = ratingDao.findPendingRatingByIdAndRaterId(
				request.getRatingId(), user.getId());

		if (rating == null) {
			throw createServiceException(ServiceExceptionCode.INVALID_PENDING_RATING);
		}

		ratingDao.delete(rating);

		return ServiceResponse.SUCCESS;
	}
}
