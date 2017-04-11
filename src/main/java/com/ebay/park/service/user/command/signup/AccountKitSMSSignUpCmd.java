package com.ebay.park.service.user.command.signup;

import static com.ebay.park.service.ServiceException.createServiceException;

import java.util.UUID;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserSession;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.user.UserServiceHelper;
import com.ebay.park.service.user.dto.SignUpResponse;
import com.ebay.park.service.user.dto.signin.SignInRequest;
import com.ebay.park.service.user.dto.signup.AccountKitSMSSignUpRequest;

/**
 * Command to perform an Account Kit with phone number
 * sign up operation.
 * @author scalderon
 * @since 2.0.2
 *
 */
@Component
public class AccountKitSMSSignUpCmd implements ServiceCommand<AccountKitSMSSignUpRequest , SignUpResponse>{
	
	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(AccountKitSMSSignUpCmd.class);

	@Autowired
	private SignUpCommand signUpCmd;
	
	@Autowired
	private UserServiceHelper userServiceHelper;
	
	@Autowired
	@Qualifier("createUserSessionCmd")
	private ServiceCommand<SignInRequest, UserSession>  createUserSession;
	
	@Autowired
	private SessionService sessionService;
	
	/**
	 * Creates a new user {@link User} and a user session {@link UserSession}
	 */
	@Override
	public SignUpResponse execute(AccountKitSMSSignUpRequest param) throws ServiceException {
		User user = signUpCmd.execute(param);

		//Set verification value
	    user.setMobileVerified(true);

		//Set mobile phone number
	    if (StringUtils.isEmpty(param.getMobilePhone())) {
            throw createServiceException(ServiceExceptionCode.EMPTY_PHONE_NUMBER);
        }
		user.setMobile(param.getMobilePhone());

		//Set username
		if (StringUtils.isEmpty(user.getUsername())) {
		    user.setUsername(userServiceHelper.createUsernameByMobile(user.getMobile()));
		}

		SignUpResponse response = new SignUpResponse();
		response.setUsername(user.getUsername());

		//persisting user
		userServiceHelper.saveUser(user);
		
		try {
		    //it creates the session using the stored user information
			UserSession userSession = createUserSession.execute(param);
			if (userSession != null) {
				user.addUserSession(userSession);

				//Create Session Cache
				sessionService.createUserSessionCache(userSession);
				response.setToken(UUID.fromString(userSession.getToken()));
			}
		} catch (Exception e) {
            logger.error("Error trying to create a user session for username: {}", user.getUsername(), e);
			throw createServiceException(ServiceExceptionCode.IO_ERROR);
		}

		//persisting user sessions
		userServiceHelper.saveUser(user);

		return response;
	}
}
