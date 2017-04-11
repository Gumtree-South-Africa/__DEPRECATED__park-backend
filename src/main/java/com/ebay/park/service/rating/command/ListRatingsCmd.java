package com.ebay.park.service.rating.command;

import com.ebay.park.db.dao.RatingDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Rating;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.conversation.command.Role;
import com.ebay.park.service.rating.dto.ListRatingResult;
import com.ebay.park.service.rating.dto.ListRatingsRequest;
import com.ebay.park.service.rating.dto.ListRatingsResponse;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;
import com.ebay.park.util.DataCommonUtil;
import com.ebay.park.util.InternationalizationUtil;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.ebay.park.service.ServiceException.createServiceException;

/**
 * Lists the ratings that rate the user with username request.username
 * It can take a special parameter request.role to return only those ratings
 * that the user was rated with where the user plays the request.role in the conversation
 *
 * @author marcos.lambolay
 */
@Component
public class ListRatingsCmd implements ServiceCommand<ListRatingsRequest, ListRatingsResponse> {

	private static final String NO_RESULTS_MESSAGE = "emptylist.user_ratings_${role}";

	@Value("${search.pageSize}")
	private int defaultPageSize;

	@Autowired
	private RatingDao ratingDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private InternationalizationUtil i18nUtil;

	@Override
	public ListRatingsResponse execute(ListRatingsRequest request)
			throws ServiceException {
		int page = request.getPage() != null ? request.getPage() : 0;
		int pageSize = request.getPageSize() != null
				&& request.getPageSize() > 0 ? request.getPageSize()
				: defaultPageSize;

		Pageable pageable = new PageRequest(page, pageSize);

		User user = userDao.findByUsername(request.getUsername());
		if (user == null) {
			throw createServiceException(ServiceExceptionCode.USER_NOT_FOUND);
		}

		
		List<Rating> ratings = null;
		long total;
		if (request.getRole() == null) {
			if(request.getRequestTime() != null){
				ratings = ratingDao.findRatingForUser(user.getId(), pageable, DataCommonUtil.parseUnixTime(request.getRequestTime()));
				total  = ratingDao.findRatingForUserQty(user.getId(), DataCommonUtil.parseUnixTime(request.getRequestTime()));
			}
			else{
				ratings = ratingDao.findRatingForUser(user.getId(), pageable);
				total  = ratingDao.findRatingForUserQty(user.getId());
			}
		} else {
			switch (request.getRole().toLowerCase()) {

			case Role.BUYER:
				if(request.getRequestTime() != null){
					ratings = ratingDao.findRatingForUserAsBuyer(user.getId(), pageable,DataCommonUtil.parseUnixTime(request.getRequestTime()));
					total = ratingDao.findRatingForUserAsBuyerQty(user.getId(),DataCommonUtil.parseUnixTime(request.getRequestTime()));
				}
				else{
					ratings = ratingDao.findRatingForUserAsBuyer(user.getId(), pageable);
					total = ratingDao.findRatingForUserAsBuyerQty(user.getId());
				}
				break;

			case Role.SELLER:
				if(request.getRequestTime() != null){
					ratings = ratingDao.findRatingForUserAsSeller(user.getId(), pageable, DataCommonUtil.parseUnixTime(request.getRequestTime()));
					total  = ratingDao.findRatingForUserAsSellerQty(user.getId(), DataCommonUtil.parseUnixTime(request.getRequestTime()));
				}
				else{
					ratings = ratingDao.findRatingForUserAsSeller(user.getId(), pageable);
					total  = ratingDao.findRatingForUserAsSellerQty(user.getId());
				}
				break;

			default:
				throw createServiceException(ServiceExceptionCode.RATING_INVALID_ROLE);
			}
		}

		List<ListRatingResult> ratingResults = new ArrayList<ListRatingResult>();
		for (Rating r : ratings) {
			ratingResults.add(new ListRatingResult(r.getItem().getId(), r
					.getRater().getId(), r.getRater().getUsername(), r
					.getRater().getPicture(), r.getItem().getName(), r
					.getStatus().toString(), r.getComment(), DataCommonUtil
					.getDateTimeAsISO(r.getRateDate()), r.getItem().getPicture1Url()));
		}
		
		ListRatingsResponse response = new ListRatingsResponse(ratingResults, total, DataCommonUtil.getDateTimeAsUnixFormat(DateTime.now().toDate()));
		
		UserSessionCache session = null;
		if (request.getToken() != null) {
			session = sessionService.getUserSession(request.getToken());
		}
		
		if(StringUtils.isBlank(request.getRole())){
			request.setRole("none");
		}
		
		//language definition
		String language = request.getLanguage();
		if ((language == null) && (session != null)) {
			language = session.getLang();
		}
		
		i18nUtil.internationalizeListedResponse(response, NO_RESULTS_MESSAGE.replace("${role}", request.getRole()),
				language);

		return response;
	}

}
