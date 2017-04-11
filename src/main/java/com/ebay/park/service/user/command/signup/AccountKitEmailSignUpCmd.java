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
import com.ebay.park.service.user.dto.signup.AccountKitEmailSignUpRequest;
import com.ebay.park.util.EmailVerificationUtil;

/**
 * Command to perform an Account Kit with email
 * sign up operation.
 * @author scalderon
 * @since 2.0.2
 *
 */
@Component
public class AccountKitEmailSignUpCmd implements ServiceCommand<AccountKitEmailSignUpRequest , SignUpResponse> {

    /** The logger. */
	private static Logger logger = LoggerFactory.getLogger(AccountKitEmailSignUpCmd.class);

	@Autowired
	private SignUpCommand signUpCmd;
	
	@Autowired
	private UserServiceHelper userServiceHelper;
	
	@Autowired
	@Qualifier("createUserSessionCmd")
	private ServiceCommand<SignInRequest, UserSession>  createUserSession;
	
	@Autowired
	private SessionService sessionService;

	@Autowired
	private EmailVerificationUtil emailVerificationUtil;
	

	@Override
	public SignUpResponse execute(AccountKitEmailSignUpRequest param) throws ServiceException {
		User user = signUpCmd.execute(param);
		
		//Set email
		if (StringUtils.isEmpty(param.getEmail())) {
            throw createServiceException(ServiceExceptionCode.EMAIL_USER_EMPTY_EMAIL);
        }
		user.setEmail(param.getEmail().toLowerCase());
		
		//Set email verified
		emailVerificationUtil.verify(user);
		
		//Set username
		if (StringUtils.isEmpty(user.getUsername())) {
		    user.setUsername(userServiceHelper.createUsernameByEmail(user.getEmail()));
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

