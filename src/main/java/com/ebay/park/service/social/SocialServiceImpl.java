package com.ebay.park.service.social;

import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.User;
import com.ebay.park.event.user.UsersEvent;
import com.ebay.park.service.ParkRequest;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.notification.FeedServiceHelper;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;
import com.ebay.park.service.social.command.*;
import com.ebay.park.service.social.dto.*;
import com.ebay.park.service.social.validator.SocialConnectValidator;
import com.ebay.park.service.social.validator.SocialDisconnectValidator;
import com.ebay.park.service.social.validator.UserRatesRequestValidator;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ebay.park.service.ServiceException.createServiceException;
@Service
public class SocialServiceImpl implements SocialService {

	@Autowired
	private GetFollowersCmd getFollowersCmd;

	@Autowired
	@Qualifier("addFollowerCmd")
	private ServiceCommand<FollowUserRequest, UsersEvent> addFollowerCmd;

	@Autowired
	@Qualifier("removeFollowerCmd")
	private ServiceCommand<UnfollowUserRequest, Void> removeFollowerCmd;

	@Autowired
	private GetFollowingsCmd getFollowingsCmd;

	@Autowired
	private SocialConnectCmd socialConnectCmd;

	@Autowired
	private SocialDisconnectCmd socialDisconnectCmd;

	@Autowired
	private ShareProfileOnFacebookCmd shareProfileOnFacebookCmd;

	@Autowired
	private ShareProfileOnTwitterCmd shareProfileOnTwitterCmd;

	@Autowired
	private GetUserRatingsCmd getUserRatingsCmd;

	@Autowired
	private SocialConnectValidator socialConnectValidator;

	@Autowired
	private SocialDisconnectValidator socialDisconnectValidator;

	@Autowired
	private UserRatesRequestValidator userRatesRequestValidator;

	@Autowired
	private ServiceValidator<ShareProfileRequest> shareProfReqValidator;

	@Autowired
	private ServiceCommand<ListFacebookFriendsRequest, ListFacebookFriendsResponse> listFriendsCmd;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private DiscoverUsersCmd discoverUsersCmd;

	@Autowired
	private SearchUserCmd searchUserCmd;
	
	@Autowired
	private UnreadCountCmd unreadCountCmd;

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private FeedServiceHelper feedServiceHelper;

	@Override
	public ListUsersFollowedResponse getFollowers(GetFollowersRequest request) {
		validateUsername(request.getUsername());
		return getFollowersCmd.execute(request);
	}

	@Override
	public ListUsersFollowedResponse getFollowings(GetFollowingsRequest request) {
		validateUsername(request.getUsername());
		return getFollowingsCmd.execute(request);
	}

	@Override
	public FollowUserResponse addFollowerToUser(FollowUserRequest request) {
		validateUsername(request.getFollower());
		validateUsername(request.getUserToFollow());
		validateUserMakingRequest(request.getFollower(), request);

		if (request.getFollower().equals(request.getUserToFollow())) {
			throw createServiceException(ServiceExceptionCode.FOLLOW_SELF_REFERENTIAL);
		}

		addFollowerCmd.execute(request);

		//Update feed property "followedByUser"
		feedServiceHelper.updateFollowByUserFeedProperty(userDao.findByUsername(request.getFollower()), 
				userDao.findByUsername(request.getUserToFollow()), Boolean.TRUE.toString());
		
		return new FollowUserResponse(request, true);
	}

	@Override
	public List<User> getFollowers(User user) {
		return userDao.findFollowers(user.getId());
	}

	@Override
	public Boolean removeFollowToUser(UnfollowUserRequest request) {
		validateUsername(request.getFollower());
		validateUsername(request.getUserToUnfollow());
		validateUserMakingRequest(request.getFollower(), request);
		removeFollowerCmd.execute(request);
		
		//Update feed property "followedByUser"
		feedServiceHelper.updateFollowByUserFeedProperty(userDao.findByUsername(request.getFollower()), 
				userDao.findByUsername(request.getUserToUnfollow()), Boolean.FALSE.toString());
		
		return true;
	}

	@Override
	public List<SmallRating> getUserRates(UserRatesRequest request) {
		userRatesRequestValidator.validate(request);
		return getUserRatingsCmd.execute(request);
	}

	private void validateUsername(String username) {
		if (StringUtils.isBlank(username)) {
			throw createServiceException(ServiceExceptionCode.EMPTY_USERNAME);
		}
	}

	/**
	 * This method is intended to validate that a user making a request is the
	 * same to which the changes will be applied. This is intended to valid
	 * having a user making a call in the name of the other
	 * 
	 * @param userName
	 *            user to be altered
	 * @param request
	 *            ParkRequest to get the user in session.
	 */
	private void validateUserMakingRequest(String userName, ParkRequest request) {
		UserSessionCache session = sessionService.getUserSession(request.getToken());
		if (session == null) {
			throw createServiceException(ServiceExceptionCode.SESSION_NOT_STORED);
		}
		if (!StringUtils.equalsIgnoreCase(session.getUsername(), userName)) {
			throw createServiceException(ServiceExceptionCode.USER_UNAUTHORIZED);
		}
	}

	@Override
	public Boolean shareProfileOnFacebook(ShareProfileRequest request) {
		validateUserMakingRequest(request.getUsernameToShare(), request);
		shareProfReqValidator.validate(request);
		return shareProfileOnFacebookCmd.execute(request);
	}

	@Override
	public Boolean shareProfileOnTwitter(ShareProfileRequest request) {
		validateUserMakingRequest(request.getUsernameToShare(), request);
		shareProfReqValidator.validate(request);
		return shareProfileOnTwitterCmd.execute(request);
	}

	@Override
	public Boolean connectToSocialNetwork(SocialNetworkConnectRequest request) {
		validateUserMakingRequest(request.getUserName(), request);
		socialConnectValidator.validate(request);
		return socialConnectCmd.execute(request);
	}

	@Override
	public Boolean disconnectSocialNetwork(SocialNetworkDisconnectRequest request) {
		validateUserMakingRequest(request.getUsername(), request);
		socialDisconnectValidator.validate(request);
		return socialDisconnectCmd.execute(request);
	}

	@Override
	public ListFacebookFriendsResponse listFacebookFriends(ListFacebookFriendsRequest request) {
		validateUserMakingRequest(request.getUsername(), request);
		return listFriendsCmd.execute(request);
	}

	@Override
	public ListUsersFollowedResponse discoverUsers(DiscoverUsersRequest request) {
		validateUserMakingRequest(request.getUsername(), request);
		return discoverUsersCmd.execute(request);
	}
	
	@Override
	public ListUsersFollowedResponse discoverPublicUsers(DiscoverUsersRequest request) {
		return discoverUsersCmd.execute(request);
	}

	@Override
	public SearchUserResponse searchUsers(SearchUserRequest request) {
		validateUserMakingRequest(request.getUsername(), request);
		return searchUserCmd.execute(request);
	}

	@Override
	public SearchUserResponse searchPublicUsers(SearchUserRequest request) {
		return searchUserCmd.execute(request);
	}

	@Override
	public UnreadCountResponse unreadCount(UnreadCountRequest request) {
		return unreadCountCmd.execute(request);
	}

}
