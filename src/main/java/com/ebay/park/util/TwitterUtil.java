/*
 * Copyright eBay, 2014
 */
package com.ebay.park.util;

import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.social.dto.SocialConnectRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.social.DuplicateStatusException;
import org.springframework.social.NotAuthorizedException;
import org.springframework.social.RateLimitExceededException;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

import java.net.HttpRetryException;
import java.util.Locale;

import static com.ebay.park.service.ServiceException.createServiceException;

/**
 * @author jpizarro
 *
 */
@Component
public class TwitterUtil {

	@Autowired
	private MessageSource messageSource;
	
	private static Logger logger = LoggerFactory.getLogger(TwitterUtil.class);

	@Value("${social.twitter.apiKey}")
	private String apiKey;

	@Value("${social.twitter.apiSecret}")
	private String apiSecret;
	
	private static final String SHARE_MESSAGE_FOR_ITEM_KEY = "social.twitter.shareMessageForItem";
	
	private static final String SHARE_MESSAGE_FOR_GROUP_KEY = "social.twitter.shareMessageForGroup";

	public String getUserId(SocialConnectRequest request){
		return null;
	}

	public TwitterTemplate createTwitterTemplate(String token, String tokenSecret) {
		return new TwitterTemplate(apiKey, apiSecret, token, tokenSecret);
	}
	
	/**
	 * Determines if token is valid. For that, it queries twitter for the user
	 * id.
	 *
	 * @return true if the token is valid
	 * @throws {@link ServiceException} if token is invalid or expired.
	 */
	public void tokenIsValid(String token, String tokenSecret) {
		try {
			createTwitterTemplate(token, tokenSecret).userOperations().getUserProfile().getId();
		} catch (NotAuthorizedException e1) {
			throw createServiceException(ServiceExceptionCode.INVALID_TWITTER_TOKEN);
		} catch (RateLimitExceededException e2) {
            logger.error("Twitter Rate Limit Exceeded with token: {} and token secret: {}", token, tokenSecret);
		}
	}

	public void shareItemOnTwitter(String token, String tokenSecret, String url) {
		this.prepareInformationAndPost(url, SHARE_MESSAGE_FOR_ITEM_KEY, token, tokenSecret);		
	}
	
	public void shareGroupOnTwitter(String token, String tokenSecret, String url) {
		this.prepareInformationAndPost(url, SHARE_MESSAGE_FOR_GROUP_KEY, token, tokenSecret);		
	}
	
	private void prepareInformationAndPost(String urlTopost,
			String textMessage, String userToken, String userSecretToken) {
		
		Locale locale = LocaleContextHolder.getLocale();
		String shareMessageForItem = messageSource.getMessage(textMessage,
				new Object[0], locale);

		this.shareOnTwitter(userToken, userSecretToken, shareMessageForItem, urlTopost);

	}
	
	
	/**
	 * Shares a link into the user's twitter status. The user is the one related with the given <code>token</code>.
	 * @param token a String representing the twitter token.
	 * @param message a String representing a message to display.
	 * @param link a String representing the link we would like to display.
	 */
	public void shareOnTwitter(String token, String tokenSecret, String message, String link) {
			try {
				TwitterTemplate tt = createTwitterTemplate(token, tokenSecret);
				tt.timelineOperations().updateStatus(message + " " + link);
			} catch (ResourceAccessException e) {
				if (e.getCause() instanceof HttpRetryException) {
					HttpRetryException httpExcep = (HttpRetryException)e.getCause();
					throw createServiceException(ServiceExceptionCode.UNAVAILABLE_TWITTER_SERVICE, new String[]{Integer.toString(httpExcep.responseCode())});
				} 
			} catch (DuplicateStatusException e) {
				throw createServiceException(ServiceExceptionCode.STATUS_DUPLICATED);
			}
			
	}


}
