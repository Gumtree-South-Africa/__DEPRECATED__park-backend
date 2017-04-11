package com.ebay.park.service.group.command;

import com.ebay.park.db.dao.GroupDao;
import com.ebay.park.db.dao.SocialDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.dao.UserSocialDao;
import com.ebay.park.db.entity.Group;
import com.ebay.park.db.entity.Social;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserSocial;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.group.dto.ShareGroupToSocialNetworkRequest;
import com.ebay.park.util.FacebookUtil;
import com.ebay.park.util.TextUtils;
import com.ebay.park.util.TwitterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;

@Component
public class ShareGroupCmd implements ServiceCommand<ShareGroupToSocialNetworkRequest, ServiceResponse> {
	
	@Autowired
	private GroupDao groupDao;
	
	@Autowired
	private UserDao userDao;

	@Autowired
	private UserSocialDao userSocialDao;

	@Autowired
	private SocialDao socialDao;

	@Autowired
	private FacebookUtil facebookUtil;

	@Autowired
	private TwitterUtil twitterUtil;
	
	@Autowired
	private TextUtils textUtils;

	@Override
	public ServiceResponse execute(ShareGroupToSocialNetworkRequest request) throws ServiceException {

		User user = userDao.findByToken(request.getToken());

		if (user == null) {
			throw createServiceException(ServiceExceptionCode.USER_UNAUTHORIZED);
		}

		Group group = groupDao.findOne(request.getGroupId());
		
		if (group == null) {
			throw createServiceException(ServiceExceptionCode.INVALID_GROUP);
		}
		
		if (!group.getCreator().equals(user)) {
			throw createServiceException(ServiceExceptionCode.INVALID_GROUP_OWNER);
		}

		Social social = socialDao.findByDescription(request.getSocialNetwork());

		if (social == null) {
			throw createServiceException(ServiceExceptionCode.INVALID_SOCIAL_NETWORK);
		}

		switch (social.getDescription()) {

		case Social.FACEBOOK:
			shareOnFacebook(user, group);
			break;
		case Social.TWITTER:
			shareOnTwitter(user, group);
			break;
		default:
			throw createServiceException(ServiceExceptionCode.INVALID_SOCIAL_NETWORK);
		}

		return ServiceResponse.SUCCESS;

	}

	private void shareOnFacebook(User user, Group group) {
		UserSocial fbUserSocial = userSocialDao.findFacebookUser(user.getId());
		String fbToken = fbUserSocial != null ? fbUserSocial.getToken() : null;

		if (fbToken != null) {
			facebookUtil.shareOnFacebook(
					textUtils.createGroupSEOURL(group.getName(), group.getGroupId()),
					group.getName(),
					group.getDescription(),
					fbToken);
		} else {
			throw createServiceException(ServiceExceptionCode.INVALID_USER_SOCIAL);
		}
	}

	private void shareOnTwitter(User user, Group group) {

		UserSocial twitterUserSocial = userSocialDao.findTwitterUser(user
				.getId());
		String twitterToken = twitterUserSocial != null ? twitterUserSocial
				.getToken() : null;
		String tokenSecret = twitterUserSocial != null ? twitterUserSocial
				.getTokenSecret() : null;

		if (twitterToken != null && tokenSecret != null) {
			twitterUtil.shareGroupOnTwitter(twitterToken, tokenSecret,
					textUtils.createGroupSEOURL(group.getName(), group.getId()));
		} else {
			throw createServiceException(ServiceExceptionCode.INVALID_USER_SOCIAL);
		}

	}

}
