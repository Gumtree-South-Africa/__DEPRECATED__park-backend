package com.ebay.park.service.rating;

import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.rating.command.*;
import com.ebay.park.service.rating.dto.*;
import com.ebay.park.service.rating.validator.RateUserRequestValidator;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ebay.park.service.ServiceException.createServiceException;

@Service
public class RatingServiceImpl implements RatingService {

	@Autowired
	private RateUserCmd rateUserCmd;

	@Autowired
	private ListRatingStatusCmd listRatingStatusCmd;

	@Autowired
	private ListPendingRatingsCmd listPendingRatingsCmd;

	@Autowired
	private ListRatingsCmd listRatingsCmd;

	@Autowired
	private RateUserRequestValidator rateUserRequestValidator;

	@Autowired
	private DeletePendingRatingCmd deletePendingRatingCmd;
	
	@Override
	public void rateUser(RateUserRequest request) {
		rateUserRequestValidator.validate(request);
		rateUserCmd.execute(request);
	}

	@Override
	public List<String> listRatingStatus() {
		return listRatingStatusCmd.execute();
	}

	@Override
	public ListPendingRatingsResponse listPendingRatings(ListPendingRatingsRequest request) {
		return listPendingRatingsCmd.execute(request);
	}
	
	@Override
	public ListPendingRatingsResponse listPublicPendingRatings(ListPendingRatingsRequest request) {
		validateUsername(request.getUsername());
		return listPendingRatingsCmd.execute(request);
	}

	@Override
	public ListRatingsResponse listRatings(ListRatingsRequest request) {
		return listRatingsCmd.execute(request);
	}
	
	@Override
	public ServiceResponse deletePendingRating(RatingRequest request){
		return deletePendingRatingCmd.execute(request);
	}

	private void validateUsername(String username) {
		if (StringUtils.isBlank(username)) {
			throw createServiceException(ServiceExceptionCode.EMPTY_USERNAME);
		}
	}
}
