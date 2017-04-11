package com.ebay.park.service.social.command;

import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Follower;
import com.ebay.park.db.entity.FollowerPK;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.social.dto.BasicUser;
import com.ebay.park.service.social.dto.GetFollowersRequest;
import com.ebay.park.service.social.dto.ListUsersFollowedResponse;
import com.ebay.park.util.InternationalizationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.ebay.park.service.ServiceException.createServiceException;
@Component
public class GetFollowersCmd implements
ServiceCommand<GetFollowersRequest, ListUsersFollowedResponse> {

	private static final String NO_RESULTS_MESSAGE = "emptylist.followers_users";

	@Autowired
	private UserDao userDao;

	@Autowired
	private InternationalizationUtil i18nUtil;

	@Override
	public ListUsersFollowedResponse execute(GetFollowersRequest request) throws ServiceException {

		User user = userDao.findByUsername(request.getUsername());
		if (user == null) {
			throw createServiceException(ServiceExceptionCode.USER_NOT_FOUND);
		}

		User loggedUser = null;
		if (request.getToken() != null) {
			loggedUser = userDao.findByToken(request.getToken());
		}


		List<BasicUser> followers = new ArrayList<>();
		if (loggedUser != null) {
			for (Follower follower : user.getFollowers()) {
				FollowerPK followerPK = follower.getId();
				User followerUser = userDao.findOne(followerPK.getFollowerId());
				BasicUser basicUser = new BasicUser(followerUser);
				basicUser.setFollowsUser(user.isFollowedByUser(follower.getUserFollower()));
				basicUser.setFollowedByUser(followerUser.isFollowedByUser(loggedUser));
				followers.add(basicUser);
			}
		} else {
			for (Follower follower : user.getFollowers()) {
				FollowerPK followerPK = follower.getId();
				User followerUser = userDao.findOne(followerPK.getFollowerId());
				BasicUser basicUser = new BasicUser(followerUser);
				basicUser.setFollowsUser(user.isFollowedByUser(follower.getUserFollower()));
				followers.add(basicUser);
			}
		}

		ListUsersFollowedResponse response = new ListUsersFollowedResponse(followers);

		//language definition
		String language = request.getLanguage();
		if ((language == null) && (loggedUser != null)) {
			language = loggedUser.getIdiom().getCode();
		}

		i18nUtil.internationalizeListedResponse(response, NO_RESULTS_MESSAGE, language);

		return response;

	}

}
