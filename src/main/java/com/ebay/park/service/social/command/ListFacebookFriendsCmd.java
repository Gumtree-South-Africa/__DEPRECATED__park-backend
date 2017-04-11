package com.ebay.park.service.social.command;

import static com.ebay.park.service.ServiceException.createServiceException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.ebay.park.db.dao.SocialDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.dao.UserSocialDao;
import com.ebay.park.db.entity.Social;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserSocial;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.social.dto.FacebookFriend;
import com.ebay.park.service.social.dto.ListFacebookFriendsRequest;
import com.ebay.park.service.social.dto.ListFacebookFriendsResponse;
import com.ebay.park.util.FacebookUtil;
import com.ebay.park.util.InternationalizationUtil;

@Component
/**
 * It creates a list of Facebook friends for the logged user.
 * This list includes direct friends (friendship in both Facebook and Park) or indirect friends
 * (friendship in Facebook, but not in Park). The included users need to grant access to the Facebook
 * list of friends in order to be included (or ask for the list).
 *
 * @author Julieta Salvadó
 */
public class ListFacebookFriendsCmd implements
		ServiceCommand<ListFacebookFriendsRequest, ListFacebookFriendsResponse> {

	private static final String EMPTY_LIST_MESSAGE = "emptylist.facebook.friends";

	@Autowired
	private UserDao userDao;

	@Autowired
	private SocialDao socialDao;

	@Autowired
	private UserSocialDao userSocialDao;

	@Autowired
	private FacebookUtil facebookUtil;

	@Autowired
	private InternationalizationUtil i18nUtil;

	@Override
	public ListFacebookFriendsResponse execute(ListFacebookFriendsRequest request)
			throws ServiceException {
		Assert.notNull(request, "Request must be not null.");

		User loggedUser = userDao.findByUsername(request.getUsername());
		if (loggedUser == null) {
			throw createServiceException(ServiceExceptionCode.USER_NOT_FOUND);
		}

		UserSocial userSocial = userSocialDao.findFacebookUser(loggedUser.getId());
		if (userSocial == null) {
			throw createServiceException(ServiceExceptionCode.INVALID_USER_SOCIAL);
		}
		
		String language = getLanguage(request, loggedUser);
		List<String> directFriendsIds = getDirectFriendsId(userSocial.getToken());

		if (CollectionUtils.isEmpty(directFriendsIds)) {
			return getEmptyFriendList(language);
		}

		ListFacebookFriendsResponse response = new ListFacebookFriendsResponse();
		
		List<FacebookFriend> allFriends = getResponseFriends(loggedUser, directFriendsIds, userSocial.getToken());
		
		//double check just in case
		if (CollectionUtils.isEmpty(allFriends)) {
			return getEmptyFriendList(language);
		}
		response.setFriends(allFriends);
		return response;
	}

	private ListFacebookFriendsResponse getEmptyFriendList(String language) {
		ListFacebookFriendsResponse response = new ListFacebookFriendsResponse();
		i18nUtil.internationalizeListedResponse(response, EMPTY_LIST_MESSAGE, language);
		return response;
	}

	/**
	 * It constructs the actual response with the found values.
	 * @param loggedUser
	 * 		current user
	 * @param directFriendsId
	 * 		a set of friend
	 * @param indirectFriends
	 * 		another set of friend
     * @return
     */
	private List<FacebookFriend> getResponseFriends(User loggedUser, List<String> directFriendsId, String socialToken) {
		
		Social facebook = socialDao.findByDescription(Social.FACEBOOK);
		List<FacebookFriend> allFriends = new ArrayList<>();
		if (facebook != null) {
			allFriends.addAll(getIndirectFriends(socialToken, directFriendsId, 
					facebook.getSocialId(), loggedUser));
			List<User> userFriends = userDao.findBySocialIdAndUserSocialIds(facebook.getSocialId(), directFriendsId);
	
			for (User userFriend : userFriends) {
				allFriends.add(new FacebookFriend(loggedUser, userFriend, null));
			}
			Collections.shuffle(allFriends);
		}
		return allFriends;
	}


	/**
	 * Defines the language value.
	 * @param request
	 * 		request with a language value
	 * @param loggedUser
	 * 		user with a language value
     * @return
	 * 		selection of the language
     */
	private String getLanguage(ListFacebookFriendsRequest request, User loggedUser) {
		String language = request.getLanguage();
		if ((language == null) && (loggedUser != null)) {
			language = loggedUser.getIdiom().getCode();
		}
		return language;
	}

	private List<FacebookFriend> getIndirectFriends(String userSocialToken, List<String> directFriendsId, Long socialId, User loggedUser) {
		List<String> indirectFriendsId = new ArrayList<String>();
		List<String> indirectFriends;
		List<FacebookFriend> indirectFriendsResult = new ArrayList<>();
		for (String friend : directFriendsId) {
			User userFriend = userDao.findBySocialIdAndUserSocialId(socialId, friend);
			if (userFriend != null) {
				//get friends of logged user friends
				indirectFriends = facebookUtil.getFriendsOfFriend(userSocialToken, friend);
				if (!CollectionUtils.isEmpty(indirectFriends)) {
					for (String indirectFriend : indirectFriends) {
						//The indirect friend can appear as direct friend
						if (!directFriendsId.contains(indirectFriend) && !indirectFriendsId.contains(indirectFriend)) {
							User friendOfFriend = userDao.findBySocialIdAndUserSocialId(socialId, indirectFriend);
							//The logged user can be appear as a indirect friend
							if (friendOfFriend != null && !loggedUser.equals(friendOfFriend)) {
								indirectFriendsId.add(indirectFriend);
								FacebookFriend facebookFriend = new FacebookFriend(loggedUser, friendOfFriend, userFriend.getUsername());
								indirectFriendsResult.add(facebookFriend);
							}
						}
					}
				}
			}
		}
		return indirectFriendsResult;
	}

	private List<String> getDirectFriendsId(String userSocialToken) {
		return facebookUtil.getFriendIds(userSocialToken);
	}

}
