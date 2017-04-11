package com.ebay.park.service.social.command;

import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.dao.UserSocialDao;
import com.ebay.park.db.entity.StatusDescription;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.social.dto.BasicUser;
import com.ebay.park.service.social.dto.DiscoverUsersRequest;
import com.ebay.park.service.social.dto.ListUsersFollowedResponse;
import com.ebay.park.util.InternationalizationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

import static com.ebay.park.service.ServiceException.createServiceException;
@Component
public class DiscoverUsersCmd implements
ServiceCommand<DiscoverUsersRequest, ListUsersFollowedResponse> {

	private static final String NO_RESULTS_MESSAGE = "emptylist.users_search";

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private UserDao userDao;

	@Autowired
	private UserSocialDao userSocialDao;
	
	@Autowired
	private InternationalizationUtil i18nUtil;
	
	@Value("${list.recommended_users_max}")
	private Integer maxUsers;

	@Value("${list.recommended_users_radius_miles}")
	private Double defaultRadius;
	
	@Value("${unlogged.latitude}")
	private Double defaultUnloggedLatitude;

	@Value("${unlogged.longitude}")
	private Double defaultUnloggedLongitude;

	@Override
	public ListUsersFollowedResponse execute(DiscoverUsersRequest request)
			throws ServiceException {

		User user = null;
		if (request.getToken() != null) {
			user = userDao.findByUsername(request.getUsername());
			
			if (user == null) {
				throw createServiceException(ServiceExceptionCode.USER_NOT_FOUND);
			}
		}
		
		setDefaultDistance(request, user);
		
		List<User> users;
		List<BasicUser> result;
		if (user != null) {
			users = userSocialDao.getRecommendedUsers(user.getId(),
					StatusDescription.ACTIVE, request.getLatitude(), request
					.getLongitude(), defaultRadius, new PageRequest(0,
					maxUsers));
			result = new ArrayList<>(users.size());
			for (User u : users) {
				BasicUser basicUser = new BasicUser(u);
				basicUser.setFollowedByUser(u.isFollowedByUser(user));
				result.add(basicUser);
			}	
		} else {
			users = userSocialDao.getPublicRecommendedUsers(
					StatusDescription.ACTIVE, request.getLatitude(), request
					.getLongitude(), defaultRadius, new PageRequest(0,
					maxUsers));
			result = new ArrayList<>(users.size());
			for (User u : users) {
				result.add(new BasicUser(u));
			}	
		}
	
		ListUsersFollowedResponse response = new ListUsersFollowedResponse(result);
		
		//language definition
		String language = request.getLanguage();
		if ((language == null) && (user != null)) {
			language = user.getIdiom().getCode();
		} 

		i18nUtil.internationalizeListedResponse(response, NO_RESULTS_MESSAGE, language);
		
		return response;	
	}
	
    private void setDefaultDistance(DiscoverUsersRequest request, User user) {
        
    	if (request.getLatitude() == null) {
			request.setLatitude(user != null ? user.getLatitude() : defaultUnloggedLatitude);
		}
		
		if (request.getLongitude() == null) {
			request.setLongitude(user != null ? user.getLongitude() : defaultUnloggedLongitude);
		}
    }
}
