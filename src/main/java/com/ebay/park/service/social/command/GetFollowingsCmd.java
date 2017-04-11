package com.ebay.park.service.social.command;

import com.ebay.park.db.dao.FollowerDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Follower;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.social.dto.BasicUser;
import com.ebay.park.service.social.dto.GetFollowingsRequest;
import com.ebay.park.service.social.dto.ListUsersFollowedResponse;
import com.ebay.park.util.InternationalizationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.ebay.park.service.ServiceException.createServiceException;
@Component
public class GetFollowingsCmd implements
		ServiceCommand<GetFollowingsRequest, ListUsersFollowedResponse> {

	private static final String NO_RESULTS_MESSAGE = "emptylist.following_users";

	@Autowired
	private FollowerDao followerDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private InternationalizationUtil i18nUtil;

	@Override
	public ListUsersFollowedResponse execute(GetFollowingsRequest request)
			throws ServiceException {

		User user = userDao.findByUsername(request.getUsername());
		if (user == null) {
			throw createServiceException(ServiceExceptionCode.USER_NOT_FOUND);
		}

		User loggedUser = null;
		if (request.getToken() != null) {
			loggedUser = userDao.findByToken(request.getToken());
		}


		List<Follower> followings = followerDao.findFollowings(user.getId());

		List<BasicUser> users = new ArrayList<>();
		if (loggedUser != null) {
			for (Follower following : followings) {
				User userFollowing = following.getUserFollowed();
				BasicUser basicUser = new BasicUser(userFollowing);
				basicUser.setFollowsUser(user.isFollowedByUser(userFollowing));
				basicUser.setFollowedByUser(userFollowing.isFollowedByUser(loggedUser));
				users.add(basicUser);
			}
		} else {
			for (Follower following : followings) {
				User userFollowing = following.getUserFollowed();
				BasicUser basicUser = new BasicUser(userFollowing);
				basicUser.setFollowsUser(user.isFollowedByUser(userFollowing));
				users.add(basicUser);
			}
		}

		ListUsersFollowedResponse response = new ListUsersFollowedResponse(users);

		//language definition
		String language = request.getLanguage();
		if ((language == null) && (loggedUser != null)) {
			language = loggedUser.getIdiom().getCode();
		}
		
		i18nUtil.internationalizeListedResponse(response, NO_RESULTS_MESSAGE, language);

		return response;
	}

}
