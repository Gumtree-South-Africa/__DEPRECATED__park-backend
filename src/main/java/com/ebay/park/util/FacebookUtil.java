package com.ebay.park.util;

import static com.ebay.park.service.ServiceException.createServiceException;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.api.FacebookLink;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Component;

import com.ebay.park.controller.exceptionhandling.SpringSocialExceptionControllerAdvice;
import com.ebay.park.eps.EPSClient;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;

/**
 * This util handles the Facebook API communication.
 * Exception handling {@link SpringSocialExceptionControllerAdvice} 
 * @throws 
 * 		org.springframework.social.ApiException - if there is an error while communicating with Facebook.
 *      org.springframework.social.InvalidAuthorizationException - Exception indicating that the authorization used 
 *      	during an operation invocation is invalid. 
 *      org.springframework.social.ExpiredAuthorizationException - Exception indicating that the authorization presented 
 *      	to the server has expired. 
 *      org.springframework.social.DuplicateStatusException - Exception thrown when a duplicate status is posted.
 *      org.springframework.social.RateLimitExceededException - Exception thrown when API calls are being rate-limited.
 *		org.springframework.social.InsufficientPermissionException - if the user has not granted "read_friendlists" permission.
 *		org.springframework.social.MissingAuthorizationException - if FacebookTemplate was not created with an access token.
 *		org.springframework.social.RevokedAuthorizationException - if the authorization used during an operation has been revoked. 
 *			This can happen when the user removes the application authorization on the provider or if the provider itself revokes the authorization.
 */
@Component
public class FacebookUtil {

	private static final String FACEBOOK = "facebook";
	
	@Autowired
	private EPSUtils ePSUtils;

	@Autowired
	private EPSClient ePSClient;

	@Autowired
	private TextUtils textUtils;

	private static Logger logger = LoggerFactory.getLogger(FacebookUtil.class);

    /**
     * Determines if token is valid. For that, it queries facebook for the user
     * id, it is one of the best, if not the best, ways to know is the token is
     * either expired or invalid.
     * @param token
     *      the Facebook token under analysis
     * @param givenUserId
     *      the user claiming ownership of the token
	 * @throws ServiceException with code INVALID_FB_TOKEN when the given token is invalid
     */
	public void tokenIsValid(String token, String givenUserId) {
		String userId = createFacebookTemplate(token).userOperations().getUserProfile().getId();
		if (userId == null || !userId.equals(givenUserId)) {
			throw createServiceException(ServiceExceptionCode.INVALID_FB_TOKEN);
		}
	}

	/**
	 * Gets the email for the token's user
	 * 
	 * @param token
     *      Facebook token for the user
	 * @return an email address for the token's user
	 */
	public String getEmail(String token) {
		return createFacebookTemplate(token).userOperations().getUserProfile().getEmail();
	}

	/**
	 * Return the facebook user id associated with the given token
	 * <code>token</code>
	 * 
	 * @param token
	 *            a single String representing the facebook token
	 * @return a single String representing the facebook user id.

	 */
	public String getUserIdAssociatedWithToken(String token) {
		return createFacebookTemplate(token).userOperations().getUserProfile().getId();
	}

	/**
	 * Creates a new FacebookTemplate able to perform unauthenticated operations 
	 * against Facebook's Graph API
	 * @param token
	 *          An access token given by Facebook after a successful OAuth 2 authentication
	 * @return FacebookTemplate
	 */
	private FacebookTemplate createFacebookTemplate(String token) {
		return new FacebookTemplate(token);
	}

	/**
	 * Posts a link into the user's facebook wall. The user is the one related
	 * with the given <code>token</code>. If the post could not be perform, it
	 * throws the appropriate exception explaining the error.
	 * 
	 * @param token
	 *            a String representing the facebook token.
	 * @param message
	 *            a String representing a message to send with the link.
	 * @param strLink
	 *            a String representing the link's URL
	 * @param name
	 *            a String representing the name of the link
	 * @param caption
	 *            a String representing a caption to be displayed with the link
	 * @param description
	 *            a String representing the description of the link
	 */
	public void postLink(String token, String message, String strLink, String name, String caption, String description) {
		FacebookLink link = new FacebookLink(strLink, name, caption, description);
        logger.info("posting link start. StrLink: {}", strLink);
		FacebookTemplate ft =  createFacebookTemplate(token);
		ft.feedOperations().postLink(message, link);
		logger.info("posting link end.");
	}

	/**
	 * Format messages and post information to Facebook
	 * @param urlTopost
	 * @param name
	 * @param description
	 * @param userToken
	 */
	public void shareOnFacebook(String urlTopost, String name, String description, String userToken){
		String caption = urlTopost.replace("http://", "");
		postLink(userToken, null, urlTopost, name, caption, description);
	}
	
	/**
	 * Retrieves user Facebook friend Ids
	 * @param token
	 * @return user facebook friend ids
	 * 	
	 */
	public List<String> getFriendIds(String token) {
		return createFacebookTemplate(token).friendOperations().getFriendIds();
	}
	
	/**
	 * Returns the Facebook username given a token.
	 * @param token Facebook token with permissions to be get the Facebook username
	 * @return the Facebook username
	 */
	public String getFacebookUsername(String token) {
		return createFacebookTemplate(token).userOperations().getUserProfile().getName();
	}
	
	/**
	 * Returns Facebook friends of user friend
	 * @param token
	 * 		the user token
	 * @param friendId
	 * 		the user friend id
	 * @return the Facebook ids of friends of user friend
	 */
	public List<String> getFriendsOfFriend(String token, String friendId) {
		return new ArrayList<>(createFacebookTemplate(token).friendOperations().getFriendIds(friendId));
	}

	/**
	 * It asks Facebook for the user picture and returns an URL.
	 * @param facebookToken
	 * 		the Facebook token with permissions to get the picture from
	 * @param userId
	 * 		the Facebook user id for the current user
     * @return picture url if the picture could be obtained; null, otherwise.
     */
    public String getUserPicture(String facebookToken, String userId) {
		byte[] img = null;
		try {
			img = createFacebookTemplate(facebookToken).userOperations().getUserProfileImage();
			String fileName = FileUtils.createFileName(ePSUtils.getPrefix(), FACEBOOK, userId);
			if (fileName != null && img != null) {
				return ePSClient.publish(fileName, img);
			}
		} catch (Exception e) {
			logger.error("Facebook user picture could not be obtained and uploaded", e);
		}
		return null;
	}

	/**
	 * It asks Facebook for the user picture and returns an HTML escaped URL.
	 * @param facebookToken
	 * 		the Facebook token with permissions to get the picture from Facebook
	 * @param userId
	 * 		the Facebook user id for the current user
	 * @return picture url if the picture could be obtained; null, otherwise.
	 */
	public String getUserPictureEscaped(String facebookToken, String userId) {
		String url = getUserPicture(facebookToken, userId);
		return textUtils.escapeCharactersForHTML(url);
	}

}
