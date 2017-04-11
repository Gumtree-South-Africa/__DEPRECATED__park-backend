package com.ebay.park.service.social.command;

import com.ebay.park.db.entity.Social;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.social.dto.ShareProfileRequest;
import com.ebay.park.util.TwitterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Locale;

import static com.ebay.park.service.ServiceException.createServiceException;
@Component
public class ShareProfileOnTwitterCmd extends ShareProfileCmd{

	private static final String SHARE_PROFILE_MESSAGE = "social.twitter.share_profile.message";

	@Autowired
	private TwitterUtil twitterUtil;
	
	@Override
	protected String getSocial() {
		return Social.TWITTER;
	}

	@Override
	protected ServiceException getTokenException() {
		return createServiceException(ServiceExceptionCode.INVALID_TWITTER_TOKEN);
	}

	@Override
	protected void postLink(String link, User sharerUser, ShareProfileRequest request, String token, String tokenSecret) {
		Locale locale = new Locale(sharerUser.getIdiom().getCode());
		String[] usersName = new String[]{request.getSharerUsername(), request.getUsernameToShare()};
		String shareMessage = messageSource.getMessage(SHARE_PROFILE_MESSAGE, usersName, locale);
		
		twitterUtil.shareOnTwitter(token, tokenSecret, shareMessage, link);

	}

}
