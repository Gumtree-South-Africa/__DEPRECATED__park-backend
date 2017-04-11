package com.ebay.park.service.rating;

import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.rating.dto.*;

import java.util.List;

public interface RatingService {

	public void rateUser(RateUserRequest request);

	public List<String> listRatingStatus();

	public ListPendingRatingsResponse listPendingRatings(ListPendingRatingsRequest request);

	public ListRatingsResponse listRatings(ListRatingsRequest request);
	
	public ServiceResponse deletePendingRating(RatingRequest request);

	ListPendingRatingsResponse listPublicPendingRatings(ListPendingRatingsRequest request);
	
}
