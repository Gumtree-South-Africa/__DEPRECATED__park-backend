package com.ebay.park.service.rating.command;

import static com.ebay.park.service.ServiceException.createServiceException;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.ebay.park.db.dao.RatingDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Rating;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.rating.dto.ListPendingRatingResult;
import com.ebay.park.service.rating.dto.ListPendingRatingsRequest;
import com.ebay.park.service.rating.dto.ListPendingRatingsResponse;
import com.ebay.park.util.DataCommonUtil;
import com.ebay.park.util.InternationalizationUtil;

/**
 * Lists the rating objects that remain to be rated by the current logged user.
 * 
 * @author marcos.lambolay
 */
@Component
public class ListPendingRatingsCmd implements ServiceCommand<ListPendingRatingsRequest, ListPendingRatingsResponse> {
	
	private static final String NO_RESULTS_MESSAGE = "emptylist.pending_user_ratings";

	@Autowired
	private RatingDao ratingDao;

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private InternationalizationUtil i18nUtil;
	
	@Value("${search.pageSize}")
	private int defaultPageSize;
	
	@Override
	public ListPendingRatingsResponse execute(ListPendingRatingsRequest request) throws ServiceException {
					
		if (request.getPage() == null || request.getPage() < 0) {
			request.setPage(0);
		}

		if (request.getPageSize() == null || request.getPageSize() < 1) {
			request.setPageSize(defaultPageSize);
		}
		
		User user = null;
		if (request.getToken() != null) {
			user = userDao.findByToken(request.getToken());
		} else {
			user = userDao.findByUsername(request.getUsername());
		}
		
		if (user == null) {
			throw createServiceException(ServiceExceptionCode.USER_NOT_FOUND);
		}
		
		PageRequest pageReq = new PageRequest(request.getPage(), request.getPageSize());
		List<Rating> ratings;
		if(request.getRequestTime() != null){
			ratings = ratingDao.findPendingRatingsForRater(user.getUserId(), pageReq, DataCommonUtil.parseUnixTime(request.getRequestTime()));
		}
		else{
			ratings = ratingDao.findPendingRatingsForRater(user.getUserId(), pageReq);
		}
		List<ListPendingRatingResult> pendingRatings = new ArrayList<ListPendingRatingResult>();
		for(Rating r : ratings) {
			pendingRatings.add(new ListPendingRatingResult(r.getRateId(), r
					.getUser().getId(), r.getUser().getUsername(), r.getItem()
					.getName(), r.getItem().getId(), r.getUser().getPicture(), r.getItem().getPicture1Url()));
		}
		ListPendingRatingsResponse response = new ListPendingRatingsResponse(pendingRatings, ratingDao.findPendingRatingsForRaterQty(user.getUserId()), DataCommonUtil.getDateTimeAsUnixFormat(DateTime.now().toDate()));
		
		//language definition
		String language = request.getLanguage();
		if ((language == null) && (user != null)) {
			language = user.getIdiom().getCode();
		}
		
		i18nUtil.internationalizeListedResponse(response, NO_RESULTS_MESSAGE, language);
		
		return response;
	}
}
