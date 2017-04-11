package com.ebay.park.service.user.command.signup;

import com.ebay.park.db.dao.IdiomDao;
import com.ebay.park.db.entity.Access;
import com.ebay.park.db.entity.Idiom;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserStatusDescription;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.notification.NotificationService;
import com.ebay.park.service.user.dto.signup.SignUpRequest;
import com.ebay.park.util.ParkTokenUtil;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import org.joda.time.DateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.util.Date;

import static com.ebay.park.service.ServiceException.createServiceException;

/**
 * Command to perform a sign up operation.
 * @author scalderon
 * @since 2.0.2
 * 
 */
@Component
public class SignUpCommand implements ServiceCommand<SignUpRequest, User> {
	
	@Autowired
	private ParkTokenUtil tokenUtil;
	
	@Autowired
	private IdiomDao idiomDao;
	
	@Resource(name = "NotificationServiceOp")
	private NotificationService notificationService;

	/**
	 * Creates a new user {@link User} 
	 */
	@Override
	public User execute(SignUpRequest param) throws ServiceException {
		User user = new User();
		
		if (!StringUtils.isEmpty(param.getUsername())) {
			user.setUsername(param.getUsername());
		}
		
		//Set location
		String[] locationArr = param.getLocation().split(",");
		user.setLatitude(Double.valueOf(locationArr[0]));
		user.setLongitude(Double.valueOf(locationArr[1]));
		user.setLocationName(param.getLocationName());
		user.setZipCode(param.getZipCode());
		
		//Set creation date

		user.setCreation(DateTime.now().toDate());
		user.setCreation(new Date());

		user.setCreation(DateTime.now().toDate());

		//Set temporary access token
		Access access = user.getAccess();
		access.setTemporaryToken(tokenUtil.createSessionToken());

		//Set Language
		Idiom idiom = idiomDao.findByCode(param.getLang());
		if (idiom == null) {
			throw createServiceException(ServiceExceptionCode.INVALID_LANGUAGE);
		}
		user.setIdiom(idiom);

		//Set status
		user.setStatus(UserStatusDescription.ACTIVE);

		//Set default notification configs
		user.setNotificationConfigs(notificationService
				.getAllApprovedNotificationConfig());
		
		return user;
	}
}
