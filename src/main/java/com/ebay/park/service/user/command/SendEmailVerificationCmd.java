package com.ebay.park.service.user.command;

import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.User;
import com.ebay.park.email.Email;
import com.ebay.park.email.MailSender;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.email.EmailVerificationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.ebay.park.service.ServiceException.createServiceException;

/**
 * @author federico.jaite
 * 
 */
@Component
public class SendEmailVerificationCmd implements ServiceCommand<String, Void> {

	@Autowired
	private MailSender mailSender;

	@Autowired
	private UserDao userDao;

	@Value("${email.welcome.subject}")
	private String emailWelcomeSubject;

	@Value("${email.welcome.template}")
	private String emailWelcomeTemplate;

	@Value("${park.url}")
	private String parkUrl;

	@Autowired
	private EmailVerificationHelper emailVerificationHelper;

	@Override
	public Void execute(String parkToken) throws ServiceException {
		User user = userDao.findByToken(parkToken);

		if (user == null) {
			throw createServiceException(ServiceExceptionCode.USER_UNAUTHORIZED);
		}

		// Checks facebook's account because a user signed up with facebook is a verified user.
		if (user.isEmailVerified() || user.hasFacebookAccountLinked() || user.isMobileVerified()) {
			throw createServiceException(ServiceExceptionCode.EMAIL_ALREADY_VERIFIED);
		}

		sendConfirmationEmail(user);

		return null;
	}

	private void sendConfirmationEmail(User user) {
		//@formatter:off
		Map<String, String> params = user.toMap();
		params.put("park.url", parkUrl);
		params.put("user.tempToken", emailVerificationHelper.encrypt(user.getAccess().getTemporaryToken()));
		params.put("user.emailEncrypt", emailVerificationHelper.encrypt(user.getEmail()));

		Email email = new Email(user.getEmail(), null, null, emailWelcomeSubject);
		email.setParams(params);
		email.setTemplate(emailWelcomeTemplate);
		//@formatter:on
		mailSender.sendAsync(email);

	}

}
