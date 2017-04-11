package com.ebay.park.service.profile.command;

import com.ebay.park.db.dao.ConversationDao;
import com.ebay.park.db.dao.FollowerDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Rating;
import com.ebay.park.db.entity.RatingStatus;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.picture.ResetEPSPictureExpireDateService;
import com.ebay.park.service.profile.dto.GetUserProfileRequest;
import com.ebay.park.service.profile.dto.UserProfile;
import com.ebay.park.service.social.dto.RatingsPredicate;
import com.ebay.park.util.InternationalizationUtil;
import com.ebay.park.util.TextUtils;
import com.google.common.collect.Collections2;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.ebay.park.service.ServiceException.createServiceException;

@Component
public class GetUserProfileCmd implements ServiceCommand<GetUserProfileRequest, UserProfile> {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private FollowerDao followerDao;
	
	@Autowired
	private ConversationDao conversationDao;
	
	@Autowired
	private InternationalizationUtil i18nUtil;
	
	@Autowired
	private TextUtils textUtils;
	
	@Autowired 
	private ResetEPSPictureExpireDateService resetEPSExpirateDate;

	private static Logger logger = LoggerFactory.getLogger(GetUserProfileCmd.class);
	
	@Override
	public UserProfile execute(GetUserProfileRequest request) throws ServiceException {

		User user = userDao.findByUsername(request.getUsername());
		if (user == null) {
			throw createServiceException(ServiceExceptionCode.USER_NOT_FOUND);
		}

		// calculate # of followers
		Integer followers = user.getFollowers().size();
		
		// calculate # of followings
		Integer followings = followerDao.findFollowings(user.getUserId()).size();
		
		// calculate scorings
		Integer negativeRating = getRates(user.getRatings(), RatingStatus.NEGATIVE.getDescription());
		Integer neutralRating = getRates(user.getRatings(), RatingStatus.NEUTRAL.getDescription());
		Integer positiveRating = getRates(user.getRatings(), RatingStatus.POSITIVE.getDescription());

		// amount of offers made and received
		Integer offersMade 		= conversationDao.findConversationsForBuyerCount(user.getUserId());
		Integer offersReceived  = conversationDao.findConversationsForSellerCount(user.getUserId());
		
		User userSeen = null;
		if (!StringUtils.isEmpty(request.getToken())) {
		    userSeen = userDao.findByToken(request.getToken());		
		}
		//private means the the user is seing their own profile, and moro info is available, like pending items
		boolean privateProfile = user.equals(userSeen);
		
		// FIXME This should be part of the UserProfile contruction, use a factory
		if (privateProfile) {
			i18nUtil.internationalizeItems(user.getPublishedItems(), request);
		} else {
			i18nUtil.internationalizeItems(user.publicPublishedItems(), request);
		}
		
		//Reset EPS expired Date
		if(StringUtils.isNotBlank(user.getPicture())){
            logger.info("Try to Reset Profile's Picture Expire Date into EPS: {}UserId:{}", user.getPicture(), user.getId());
			resetEPSExpirateDate.resetEPSExpireDate(user.getPicture());
		}
		//
		
		//create UserProfile with previous data
		UserProfile userProfile = new UserProfile(user, privateProfile);
		userProfile.setFollowers(followers);
		userProfile.setFollowing(followings);
		userProfile.setNegativeRatings(negativeRating);
		userProfile.setNeutralRatings(neutralRating);
		userProfile.setPositiveRatings(positiveRating);
		userProfile.setVerified(user.isEmailVerified());
		userProfile.setMobileVerified(user.isMobileVerified());
		userProfile.setZipCode(user.getZipCode());
		userProfile.setOffersMade(offersMade);
		userProfile.setOffersReceived(offersReceived);
		userProfile.setURL(textUtils.createProfileSEOURL(user.getUsername()));
		userProfile.setPhoneNumber(privateProfile ? user.getMobile() : null);
		
		if (userSeen != null) {
			userProfile.setFollowedByUser(user.isFollowedByUser(userSeen));
		}
		
		return userProfile;
	}
	
	
	private Integer getRates(List<Rating> ratings, String ratingStatus){
		
		List<String> status = new ArrayList<String>();
		status.add(ratingStatus);
		
		Collection<Rating> filteredRatings = Collections2.filter(ratings, new RatingsPredicate(status));
		return filteredRatings.size();
		
	}

}
