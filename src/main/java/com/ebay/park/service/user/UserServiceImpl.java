/**
 * Copyright eBay, 2014
 */
package com.ebay.park.service.user;

import com.ebay.park.service.user.command.signup.AccountKitEmailSignUpCmdV3;
import com.ebay.park.service.user.command.signup.AccountKitEmailSignUpCmdV4;
import com.ebay.park.service.user.command.signup.FacebookSignUpCmdV3;
import com.ebay.park.service.user.command.signup.FacebookSignUpCmdV4;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.device.command.SetSwrveIdToSessionCmd;
import com.ebay.park.service.device.dto.DeviceRequest;
import com.ebay.park.service.device.dto.DeviceSwrveIdRequest;
import com.ebay.park.service.user.command.ChangePwdCmd;
import com.ebay.park.service.user.command.CheckValueCmd;
import com.ebay.park.service.user.command.FacebookSignInCmdImpl;
import com.ebay.park.service.user.command.ForgotPwdCmd;
import com.ebay.park.service.user.command.ForgotPwdV4Cmd;
import com.ebay.park.service.user.command.SendEmailVerificationCmd;
import com.ebay.park.service.user.command.SignInCmd;
import com.ebay.park.service.user.command.SignOutCmd;
import com.ebay.park.service.user.command.VerifyEmailCmd;
import com.ebay.park.service.user.command.signin.SignInV4Cmd;
import com.ebay.park.service.user.dto.ChangePwdRequest;
import com.ebay.park.service.user.dto.ChangePwdResponse;
import com.ebay.park.service.user.dto.CheckValueRequest;
import com.ebay.park.service.user.dto.CheckValueResponse;
import com.ebay.park.service.user.dto.EmailRequest;
import com.ebay.park.service.user.dto.SignInRequest;
import com.ebay.park.service.user.dto.SignInResponse;
import com.ebay.park.service.user.dto.SignUpRequest;
import com.ebay.park.service.user.dto.SignUpResponse;
import com.ebay.park.service.user.dto.VerifyEmailRequest;
import com.ebay.park.service.user.dto.signin.AccountKitEmailSignInRequest;
import com.ebay.park.service.user.dto.signin.AccountKitSMSSignInRequest;
import com.ebay.park.service.user.dto.signin.FacebookSignInRequest;
import com.ebay.park.service.user.dto.signup.AccountKitEmailSignUpRequest;
import com.ebay.park.service.user.dto.signup.AccountKitSMSSignUpRequest;
import com.ebay.park.service.user.dto.signup.FacebookSignUpRequest;
import com.ebay.park.service.user.validator.ChangePwdRequestValidator;
import com.ebay.park.service.user.validator.CheckValueRequestValidator;
import com.ebay.park.service.user.validator.ForgetPwdRequestValidator;
import com.ebay.park.service.user.validator.SignInRequestValidator;
import com.ebay.park.service.user.validator.SignUpRequestValidator;
import com.ebay.park.service.user.validator.VerifyEmailRequestValidator;

/**
 * @author jppizarro
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private SignUpRequestValidator signUpReqValidator;

	@Autowired
	private SignInRequestValidator signInReqValidator;
	
	@Autowired
	private ForgetPwdRequestValidator forgetPwdReqValidator;

	@Autowired
	private CheckValueRequestValidator checkValueReqValidator;

	@Autowired
	private ChangePwdRequestValidator changePwdReqValidator;

	@Autowired
	private FacebookSignInCmdImpl fbSignInCmd;

	@Autowired
	private ServiceCommand<SignUpRequest, SignUpResponse> signUpCmd;

	@Autowired
	@Qualifier("signInCmd")
	private SignInCmd signInCmd;
	
	@Autowired
	private SignInV4Cmd signInV4Cmd;

	@Autowired
	private CheckValueCmd checkValueCmd;

	@Autowired
	@Qualifier("forgotPwdCmd")
	private ForgotPwdCmd forgetPwdCmd;
	
	@Autowired
	private ForgotPwdV4Cmd forgetPwdV4Cmd;

	@Autowired
	private SignOutCmd signOutCmd;

	@Autowired
	private ChangePwdCmd changePwdCmd;

	@Autowired
	private VerifyEmailRequestValidator verifyEmailRequestValidator;

	@Autowired
	private VerifyEmailCmd verifyEmailCmd;

	@Autowired
	private SendEmailVerificationCmd sendEmailVerificationCmd;

	@Autowired
	private ServiceValidator<AccountKitSMSSignInRequest> accountKitSMSSignInReqValidator;

	@Autowired
    private ServiceValidator<AccountKitEmailSignInRequest>  accountKitEmailSignInReqValidator;

	@Autowired
	private ServiceCommand<AccountKitSMSSignInRequest, SignInResponse> accountKitSMSSignInCmd;

	@Autowired
    private ServiceCommand<AccountKitEmailSignInRequest, SignInResponse> accountKitEmailSignInCmd;

	@Autowired
	private ServiceValidator<AccountKitSMSSignUpRequest> accountKitSMSSignUpReqValidator;

	@Autowired
    private ServiceValidator<AccountKitEmailSignUpRequest> accountKitEmailSignUpReqValidator;

	@Autowired
	private ServiceCommand<AccountKitSMSSignUpRequest, SignUpResponse> accountKitSMSSignUpCmd;

	@Autowired
    private AccountKitEmailSignUpCmdV3 accountKitEmailSignUpCmdV3;

	@Autowired
	private AccountKitEmailSignUpCmdV4 accountKitEmailSignUpCmdV4;

	@Autowired
	private  ServiceValidator<FacebookSignInRequest> facebookSignInReqValidator;

	@Autowired
	private ServiceCommand<FacebookSignInRequest, SignInResponse> facebookSignInCmd;

	@Autowired
	private ServiceValidator<FacebookSignUpRequest> facebookSignUpReqValidator;

	@Autowired
	private FacebookSignUpCmdV3 facebookSignUpCmdV3;

	@Autowired
	private FacebookSignUpCmdV4 facebookSignUpCmdV4;
	
	@Autowired
	private SetSwrveIdToSessionCmd setSwrveIdToSessionCmd;
	
	/**
	 * Legacy Sign In implementation.
	 */
	@Override
	public SignInResponse signIn(SignInRequest request) throws ServiceException {
		signInReqValidator.validate(request);
		if (StringUtils.isNotBlank(request.getFbToken())) {
			return fbSignInCmd.execute(request);
		} else {
			return signInCmd.execute(request);
		}
	}
	
	/**
	 * Sign In V4.
	 */
	@Override
	public SignInResponse signInV4(SignInRequest request) throws ServiceException {
		signInReqValidator.validate(request);
		return signInV4Cmd.execute(request);
	}

	
	/**
	 * Signs in a user by Account Kit SMS with mobile phone number.
	 */
	@Override
	public SignInResponse accountKitSMSSignIn(AccountKitSMSSignInRequest request) throws ServiceException {
	    accountKitSMSSignInReqValidator.validate(request);
		return accountKitSMSSignInCmd.execute(request);
	}

	
	/**
	 * Signs in a user by Facebook.
	 */
	@Override
	public SignInResponse facebookSignIn(FacebookSignInRequest request) throws ServiceException {
		facebookSignInReqValidator.validate(request);
		return facebookSignInCmd.execute(request);
	}
	
	
	/**
	 * Signs in a user by Account Kit with email.
	 */
	@Override
	public SignInResponse accountKitEmailSignIn(AccountKitEmailSignInRequest request) throws ServiceException {
		accountKitEmailSignInReqValidator.validate(request);
		return accountKitEmailSignInCmd.execute(request);
	}

	@Override
	public SignUpResponse signUp(SignUpRequest request) throws ServiceException {
		signUpReqValidator.validate(request);
		return signUpCmd.execute(request);
	}
	
	
	/**
	 * Signs up a new user by Account Kit SMS with mobile phone number.
	 */
	@Override
	public SignUpResponse accountKitSMSSignUp(AccountKitSMSSignUpRequest request) throws ServiceException {
		accountKitSMSSignUpReqValidator.validate(request);
		return accountKitSMSSignUpCmd.execute(request);
	}

	@Override
    public SignUpResponse accountKitEmailSignUpV3(AccountKitEmailSignUpRequest request) throws ServiceException {
        accountKitEmailSignUpReqValidator.validate(request);
        return accountKitEmailSignUpCmdV3.execute(request);
    }

	@Override
	public SignUpResponse accountKitEmailSignUpV4(AccountKitEmailSignUpRequest request) {
		accountKitEmailSignUpReqValidator.validate(request);
		return accountKitEmailSignUpCmdV4.execute(request);
	}

	@Override
	public SignUpResponse facebookSignUpV3(FacebookSignUpRequest request) throws ServiceException {
		facebookSignUpReqValidator.validate(request);
		return facebookSignUpCmdV3.execute(request);
	}

	@Override
	public SignUpResponse facebookSignUpV4(FacebookSignUpRequest request) throws ServiceException {
		facebookSignUpReqValidator.validate(request);
		return facebookSignUpCmdV4.execute(request);
	}

	@Override
	public CheckValueResponse checkValue(CheckValueRequest request)
			throws ServiceException {
		checkValueReqValidator.validate(request);
		return checkValueCmd.execute(request);
	}

	@Override
	public ServiceResponse forgetPwdV3(EmailRequest request)
			throws ServiceException {
		forgetPwdReqValidator.validate(request);
		forgetPwdCmd.execute(request);
		return ServiceResponse.SUCCESS;
	}
	
	@Override
	public ServiceResponse forgetPwdV4(EmailRequest request)
			throws ServiceException {
		forgetPwdReqValidator.validate(request);
		forgetPwdV4Cmd.execute(request);
		return ServiceResponse.SUCCESS;
	}


	@Override
	public ServiceResponse signOut(DeviceRequest deviceRequest) throws ServiceException {
		signOutCmd.execute(deviceRequest);
		return ServiceResponse.SUCCESS;
	}

	@Override
	public ChangePwdResponse changePwd(ChangePwdRequest request)
			throws ServiceException {
		changePwdReqValidator.validate(request);
		return changePwdCmd.execute(request);
	}

	@Override
	public String verifyEmail(VerifyEmailRequest request)
			throws ServiceException {
		verifyEmailRequestValidator.validate(request);
		return verifyEmailCmd.execute(request);
	}

	@Override
	public ServiceResponse sendVerificationEmail(String parkToken)
			throws ServiceException {
		sendEmailVerificationCmd.execute(parkToken);
		return ServiceResponse.SUCCESS;
	}

	@Override
	public ServiceResponse setSwrveIdToSession(DeviceSwrveIdRequest request) {
		Assert.notNull(request, "DeviceSwrveIdRequest cannot be null");
		return setSwrveIdToSessionCmd.execute(request);
	}

}
