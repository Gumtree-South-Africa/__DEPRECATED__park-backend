/*
 * Copyright eBay, 2014
 */
package com.ebay.park.service.user;

import com.ebay.park.db.dao.UserAdminDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Access;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserAdmin;
import com.ebay.park.db.entity.UserStatusDescription;
import com.ebay.park.notification.NotificationAction;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.email.UserEmailService;
import com.ebay.park.util.ParkTokenUtil;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.text.Normalizer;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static com.ebay.park.service.ServiceException.createServiceException;


/**
 * @author jpizarro
 * 
 */
@Component
public class UserServiceHelper {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceHelper.class);

	@Autowired
	private UserDao userDao;

	@Autowired
	private UserAdminDao userAdminDao;

	@Value("${signin.maxAttempts}")
	private Integer signInMaxAttempts;
	
	@Value("${signin.maxAttempts.minutes}")
	private Integer signInMaxAttemptsMinutes;

	@Value("${username.max.length}")
    private int usernameMaxLength;

	/**
	 * Number of random numbers in username generation.
	 */
	private static final int RANDOM_NUMBERS = 2;

	/**
	 * Number of digits from the mobile phone that will be part of the
	 * username generation.
	 */
    private static final int MOBILE_DIGITS = 6;
	
	@Autowired
	private UserEmailService userEmailService;

	@Autowired
	private ParkTokenUtil tokenUtil;
	
	public boolean shouldBlockAccount(Access access) {
		return access.getFailedSignInAttempts() >= signInMaxAttempts;
	}

	public void resetAccessByUser(User user) {
		Access access = user.getAccess();
		access.resetFailedSignInAttempts();
	}

	public User saveUser(User user) {
		try {
			user = userDao.save(user);
		} catch (Exception e){
			throw createServiceException(ServiceExceptionCode.DUPLICATED_SIGNUP_DATA);
		}
		return user;
	}

	public String updateApplicationToken(UserAdmin user) {
		String sessionToken = tokenUtil.createSessionToken();
		logger.debug("Session token created: {}", sessionToken);
		user.setToken(sessionToken);
		userAdminDao.saveAndFlush(user);
		logger.debug("UserAdmin object with the new token was saved in the DB: [username:{}]", user.getUsername());
		return sessionToken;
	}

	public void sendAccountBlockedEmail(User user) {
		userEmailService.sendEmail(user, NotificationAction.USER_BLOCKED, null);
	}


	/**
	 * This makes their best effort to locate the user
	 * 
	 * @param username: The user has this username OR EMAIL !!!
	 * @param email: The user has this email
	 * @return
	 */
	public User findUserByUsernameOrEmail(String username, String email) {
	    return StringUtils.isEmpty(email) ? userDao.findByUsernameOrEmail(username, username) :
	        userDao.findByEmail(email);
	}

	public User findUserById(Long userId) {
		return userDao.findOne(userId);
	}

	/**
	 * Given a Park token, it searches for the related user.
	 * @param token Park token
	 * @return the related user
	 * @throws ServiceException with code USER_NOT_FOUND when the user cannot be found
     */
	public User findUserByToken(String token) {
		User user = userDao.findByToken(token);
		assertUserNotNull(user);
		return user;
	}

	/**
	 * Given a Park token, it searches for the related user.
	 * @param token Park token
	 * @return the related user
	 * @throws ServiceException with code USER_UNAUTHORIZED when the user cannot be found
	 */
	public User findAuthorizedUserByToken(String token) {
		User user = userDao.findByToken(token);
		assertUserAuthorized(user);
		return user;
	}

	/**
	 * It verifies the user is not null. Otherwise, it throws an exception
	 * @param user the user to verified
	 * @throws ServiceException with code USER_UNAUTHORIZED when the user is null
	 */
	public void assertUserAuthorized(User user) {
		if (user == null) {
			throw createServiceException(ServiceExceptionCode.USER_UNAUTHORIZED);
		}
	}

	public User findUserByEmail(String email) {
		return userDao.findByEmail(email);
	}

	/**
	 * It verifies the user is not null. Otherwise, it throws an exception
	 * @param user the user to verified
	 * @throws ServiceException with code USER_NOT_FOUND when the user is null
     */
	public void assertUserNotNull(User user) {
		if (user == null) {
			throw createServiceException(ServiceExceptionCode.USER_NOT_FOUND);
		}
	}

	public String createSessionToken() {
		return tokenUtil.createSessionToken();
	}


	/**
	 * @param access
	 * @return
	 */
	public boolean oneToBlockAccount(Access access) {
		return access.getFailedSignInAttempts() + 1 == signInMaxAttempts;
	}
	
	public boolean canUserBeUnlocked(User user) {
		Access access = user.getAccess();
		
		if (Minutes.minutesBetween(new DateTime(access.getLastSignInAttempt()),
				new DateTime()).isGreaterThan(Minutes.minutes(signInMaxAttemptsMinutes))) {
			user.setStatus(UserStatusDescription.ACTIVE);
			access.resetFailedSignInAttempts();
			userDao.save(user);
			return true;
		}

		return false;
	}
	
	/**
	 * Find user by mobile phone number.
	 *
	 * @param phoneNumber the phone number
	 * @return if it exists, the user is returned; otherwise, null is returned.
	 */
	public User findUserByPhoneNumber (String phoneNumber) {
		return userDao.findByMobile(phoneNumber);
	}
	
	/**
	 * Find user by username.
	 *
	 * @param username the username
	 * @return the user
	 */
	public User findUserByUsername (String username) {
		return userDao.findByUsername(username);
	}

	/**
	 * It generates an username by using the user mobile phone.
	 * This generated username does not belong to any other existing user.
	 * Rules: usuario + 2 random numbers.
	 * @param userMobilePhone
	 *         the user phone number
	 * @return
	 *     the generated username.
	 */
	public String createUsernameByMobile(String userMobilePhone) {
        String lastSixDigitsPhone = userMobilePhone.substring(userMobilePhone.length() - MOBILE_DIGITS);
        String username = "usuario" + lastSixDigitsPhone;
        
        while (findUserByUsername(username) != null) {
            //A user with this username already exist
            //Approach: adding 2 random numbers.
            username = addRandomNumbers(username, RANDOM_NUMBERS);
        }
        return username;
	}

	public String createUsernameByEmail(String email) {
        String username = email.substring(0, email.indexOf("@"));
        username = Normalizer.normalize(username.toLowerCase(), Normalizer.Form.NFD) //lowercase and normalization
                .replaceAll("[^0-9a-zA-Z-]", ""); //character deletion
        if (username.length() >= usernameMaxLength) {
            username = username.substring(0, usernameMaxLength);
        }

        while (userDao.findByUsername(username) != null) {
            //A user with this username already exist
            //Approach: adding 2 random numbers.
            if (username.length() >= usernameMaxLength) {
                username = username.substring(0, usernameMaxLength - RANDOM_NUMBERS);
            }
            username = addRandomNumbers(username, RANDOM_NUMBERS);
        }
        return username;
	}

    private String addRandomNumbers(String username, int randomNumbers) {
        Random random = new Random();
        List<Integer> randomBetween00And99 = random.ints(0, 9)
                .limit(randomNumbers)
                .boxed()
                .collect(Collectors.toList());
        for (Integer i : randomBetween00And99) {
            username += i.toString();
        }
        return username;
    }
    
    /**
     * Returns a list of Park users given a social Id
     * @param socialId
     * @param socialUsersIds
     * @return park user list
     */
    public List<User> findBySocialIdAndUserSocialIds(Long socialId, List<String> socialUsersIds) {
    	return userDao.findBySocialIdAndUserSocialIds(socialId, socialUsersIds);
    }

	/**
	 * It sets the email to the user. The user is NOT saved.
	 * @param user	the user must be not null
	 * @param email	the email
	 * @throws ServiceException with code EMAIL_USER_EMPTY_EMAIL when the email parameter is empty
	 * @throws IllegalArgumentException when the user is null
     */
	public void setUserEmail(User user, String email) {
		Assert.notNull(user, "User must be not null");
		if (StringUtils.isEmpty(email)) {
			throw createServiceException(ServiceExceptionCode.EMAIL_USER_EMPTY_EMAIL);
		}
		user.setEmail(email.toLowerCase());
	}

	/**
	 * It deletes the user email.
	 * @param user the user. It must be not null.
	 * @throws IllegalArgumentException when the user is null
     */
	public void deleteUserEmail(User user) {
		Assert.notNull(user, "User must be not null");
		user.setEmail(null);
		saveUser(user);
	}

}

