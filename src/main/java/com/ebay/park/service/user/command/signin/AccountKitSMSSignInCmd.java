package com.ebay.park.service.user.command.signin;

import static com.ebay.park.service.ServiceException.createServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ebay.park.db.dao.UserSessionDao;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserSession;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.user.UserServiceHelper;
import com.ebay.park.service.user.dto.SignInResponse;
import com.ebay.park.service.user.dto.signin.AccountKitSMSSignInRequest;
import com.ebay.park.service.user.dto.signin.SignInRequest;

/**
 * Command to perform a Sign in by Account Kit SMS with mobile phone number.
 * @author scalderon
 * @since 2.0.2
 *
 */
@Component
public class AccountKitSMSSignInCmd implements ServiceCommand<AccountKitSMSSignInRequest, SignInResponse> {
	
	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(AccountKitSMSSignInCmd.class);

	@Autowired
	private UserServiceHelper userServiceHelper;
	
	@Autowired
	private SignInCommand signInReqCmd;
	
	@Autowired
	@Qualifier("createUserSessionCmd")
	private ServiceCommand<SignInRequest, UserSession>  createUserSession;
	
	@Autowired
	private SessionService sessionService;

	@Autowired
	private UserSessionDao userSessionDao;
	
	@Override
	public SignInResponse execute(AccountKitSMSSignInRequest param) throws ServiceException {
	    //TODO refactor. See AccountKitEmailSignInCmd
		User user = userServiceHelper.findUserByPhoneNumber(param.getMobilePhone());
	    if (user == null) {
            throw createServiceException(ServiceExceptionCode.USER_NOT_FOUND);
        }
		SignInResponse response = signInReqCmd.execute(user);
		try {
			UserSession userSession = createUserSession.execute(param);
			
			if (userSession != null) {
				user.addUserSession(userSession);

				//Create Session Cache
				sessionService.createUserSessionCache(userSession);
				response.setToken(userSession.getToken());

				userSessionDao.save(userSession);
			}
		} catch (Exception e) {
            logger.error("Error trying to create a user session for userID: {}", user.getId(), e);
			throw createServiceException(ServiceExceptionCode.IO_ERROR);
		}

		return response;
	}

}
