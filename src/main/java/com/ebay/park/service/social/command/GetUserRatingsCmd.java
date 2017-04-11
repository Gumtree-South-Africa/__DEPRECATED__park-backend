package com.ebay.park.service.social.command;

import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Rating;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.social.dto.RatingsPredicate;
import com.ebay.park.service.social.dto.SmallRating;
import com.ebay.park.service.social.dto.UserRatesRequest;
import com.google.common.collect.Collections2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.ebay.park.service.ServiceException.createServiceException;
@Component
public class GetUserRatingsCmd implements ServiceCommand<UserRatesRequest, List<SmallRating>>{

	@Autowired
	private UserDao userDao;
	
	@Override
	public List<SmallRating> execute(UserRatesRequest request) throws ServiceException {
		
		User user = userDao.findByUsername(request.getUsername());
		if (user == null) {
			throw createServiceException(ServiceExceptionCode.USER_NOT_FOUND);
		}

		Collection<Rating> filteredRatings = user.getRatings();
		
		List<String> statuses = request.getRateStatus();
		if (statuses != null){
			filteredRatings = Collections2.filter(user.getRatings(), new RatingsPredicate(statuses));
		}
		
		return getRatings(filteredRatings);
		
	}

	private List<SmallRating> getRatings(Collection<Rating> ratings) {
		List<SmallRating> smallRatings = new ArrayList<SmallRating>();
		for (Rating rating : ratings){
			smallRatings.add(new SmallRating(rating));
		}
		return smallRatings;
	}

}
