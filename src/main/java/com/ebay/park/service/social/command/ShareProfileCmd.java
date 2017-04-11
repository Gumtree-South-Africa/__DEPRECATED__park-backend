package com.ebay.park.service.social.command;

import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserSocial;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.social.dto.ShareProfileRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.ebay.park.service.ServiceException.createServiceException;
@Component
public abstract class ShareProfileCmd implements ServiceCommand<ShareProfileRequest, Boolean>{

	@Value("${userProfile.shareUrl}")
	protected String shareUrlUserProfile;
	
	@Autowired
	protected MessageSource messageSource;
	
	@Autowired
	protected UserDao userDao;

	@Override
	public Boolean execute(ShareProfileRequest request) throws ServiceException {
		
		User sharerUser = userDao.findByUsername(request.getSharerUsername());
		if (sharerUser == null) {
			throw createServiceException(ServiceExceptionCode.USER_NOT_FOUND);
		}
		
		User userToShare = userDao.findByUsername(request.getUsernameToShare());
		if (userToShare == null) {
			throw createServiceException(ServiceExceptionCode.USER_NOT_FOUND);
		}
		
		String token = getToken(sharerUser);
		if (StringUtils.isBlank(token)) {
			throw getTokenException();
		}
		
		String tokenSecret = getTokenSecret(sharerUser);
		
		String link = getUserProfileLink(request.getUsernameToShare());

		postLink(link, sharerUser, request, token, tokenSecret);		

		return true;
	}

	protected String getUserProfileLink(String usernameToShare) {
		// TODO change link generation when the url of the user profile is define.
		return shareUrlUserProfile;
	}
	
	private String getToken(User sharerUser) {
		
		List<UserSocial> socials = sharerUser.getUserSocials();
		if (socials != null){
			for(UserSocial userSocial : socials){
				if (getSocial().equals(userSocial.getSocial().getDescription()))
					return userSocial.getToken();
			}
		}
		return null;
	}
	
	private String getTokenSecret(User sharerUser) {

		List<UserSocial> socials = sharerUser.getUserSocials();
		if (socials != null) {
			for (UserSocial userSocial : socials) {
				if (getSocial().equals(userSocial.getSocial().getDescription()))
					return userSocial.getTokenSecret();
			}
		}
		return null;
	}
	
	protected abstract String getSocial();
	
	protected abstract ServiceException getTokenException();
	
	protected abstract void postLink(String link, User sharerUser, ShareProfileRequest request, String token, String tokenSecret);

}
