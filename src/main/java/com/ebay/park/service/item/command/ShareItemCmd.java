package com.ebay.park.service.item.command;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.dao.SocialDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.dao.UserSocialDao;
import com.ebay.park.db.entity.*;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.item.dto.ShareItemRequest;
import com.ebay.park.util.FacebookUtil;
import com.ebay.park.util.InternationalizationUtil;
import com.ebay.park.util.TextUtils;
import com.ebay.park.util.TwitterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;

@Component
public class ShareItemCmd implements ServiceCommand<ShareItemRequest, Void> {

	@Autowired
	private ItemDao itemDao;

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

	@Autowired
	private InternationalizationUtil i18nUtil;

	@Override
	public Void execute(ShareItemRequest request) throws ServiceException {

		User user = userDao.findByToken(request.getToken());

		if (user == null) {
			throw createServiceException(ServiceExceptionCode.USER_UNAUTHORIZED);
		}

		Item item = itemDao.findOne(request.getItemId());

		if (item == null) {
			throw createServiceException(ServiceExceptionCode.ITEM_NOT_FOUND);
		}

		if (!item.is(StatusDescription.PENDING)) {
			i18nUtil.internationalize(item.getCategory(), request.getLanguage() != null ? request.getLanguage() : user.getIdiom().getCode());

			if (item.is(StatusDescription.EXPIRED)) {
				throw createServiceException(ServiceExceptionCode.ITEM_EXPIRED);
			}


			Social social = socialDao.findByDescription(request.getSocialNetwork());

			if (social == null) {
				throw createServiceException(ServiceExceptionCode.INVALID_SOCIAL_NETWORK);
			}

			switch (social.getDescription()) {

				case Social.FACEBOOK:
					shareOnFacebook(user, item);
					break;
				case Social.TWITTER:
					shareOnTwitter(user, item);
					break;
				default:
					throw createServiceException(ServiceExceptionCode.INVALID_SOCIAL_NETWORK);
			}
		}
		return null;
	}

	protected void shareOnFacebook(User user, Item item) {

		UserSocial fbUserSocial = userSocialDao.findFacebookUser(user.getId());
		String fbToken = fbUserSocial != null ? fbUserSocial.getToken() : null;

		if (fbToken != null) {
			facebookUtil.tokenIsValid(fbToken, fbUserSocial.getUserId());
			facebookUtil.shareOnFacebook(
					textUtils.createItemSEOURL(item.getCategory().getName(), item.getName(), item.getId()),
					item.getName(),
					item.getDescription(),
					fbToken);
		} else {
			throw createServiceException(ServiceExceptionCode.INVALID_USER_SOCIAL);
		}
	}

	protected void shareOnTwitter(User user, Item item) {

		UserSocial twitterUserSocial = userSocialDao.findTwitterUser(user
				.getId());
		String twitterToken = twitterUserSocial != null ? twitterUserSocial
				.getToken() : null;
		String tokenSecret = twitterUserSocial != null ? twitterUserSocial
				.getTokenSecret() : null;

		if (twitterToken != null && tokenSecret != null) {
			twitterUtil.shareItemOnTwitter(twitterToken, tokenSecret,
					textUtils.createItemSEOURL(item.getCategory().getName(), item.getName(), item.getId()));
		} else {
			throw createServiceException(ServiceExceptionCode.INVALID_USER_SOCIAL);
		}

	}

}
