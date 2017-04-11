/**
 * Copyright eBay, 2014
 */
package com.ebay.park.service.email;

import static com.ebay.park.service.ServiceException.createServiceException;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.util.ParkConstants;

/**
 * @author diana.gazquez
 *
 */
@Aspect
@Configuration
public class EmailVerificationAspect implements  ParkConstants {

	private static Logger logger = LoggerFactory.getLogger(EmailVerificationAspect.class);

	@Autowired
	private UserDao userDao;

	@Before(value = "@annotation(EmailVerificationRequired) && args(parkToken,..)")
	public void preHandle(String parkToken) throws Exception {
		// Get the Auth Token
		User user = null;
		// Look for the user
		try{
			user = userDao.findByToken(parkToken);
		} catch (Exception e) {
            logger.error("User not found for the given token: {}", parkToken);
			throw createServiceException(ServiceExceptionCode.USER_UNAUTHORIZED);
		}
		if (user == null){
			throw createServiceException(ServiceExceptionCode.USER_UNAUTHORIZED);
		}

		// validates if email is verified
		if (!user.isEmailVerified()){
			throw createServiceException(ServiceExceptionCode.ACCOUNT_NOT_VERIFIED);
		}

	}

}
