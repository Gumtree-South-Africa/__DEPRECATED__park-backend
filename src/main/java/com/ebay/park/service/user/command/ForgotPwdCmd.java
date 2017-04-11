package com.ebay.park.service.user.command;

import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserStatusDescription;
import com.ebay.park.email.PasswordEmailSender;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.device.command.RemoveUserSessionsByUserCmd;
import com.ebay.park.service.device.dto.RemoveUserSessionsByUserRequest;
import com.ebay.park.service.user.dto.EmailRequest;
import com.ebay.park.util.Password;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import static com.ebay.park.service.ServiceException.createServiceException;

/**
 * Forget password command.
 *
 * @author marcos.lambolay
 */
@Component
public class ForgotPwdCmd implements ServiceCommand<EmailRequest, Void> {

	@Autowired
	protected UserDao userDao;

	@Autowired
	private ApplicationContext context;

	@Autowired
	private RemoveUserSessionsByUserCmd removeUserSessionsByUserCmd;

	@Override
	public Void execute(EmailRequest request) {
		Assert.notNull(request, "The request cannot be null");
		User user = userDao.findByEmail(request.getEmail());

		//Checks if user is null
		this.validateUserNull(user);

		if (UserStatusDescription.BANNED.equals(user.getStatus())) {
			throw createServiceException(ServiceExceptionCode.ERROR_FORGOT_PASSWORD_WHEN_BANNED);
		}

		//delete user sessions
		removeUserSessionsByUserCmd.execute(new RemoveUserSessionsByUserRequest(user.getId()));

		Password newPassword = new Password();
		user.setPassword(newPassword.getHashedPassword());
		user.getAccess().resetFailedSignInAttempts();
		user.setStatus(UserStatusDescription.ACTIVE);

		// save to track expiration
		userDao.save(user);

		// send notification
		sendNotification(user, newPassword);
		return null;
	}

	private void sendNotification(User user, Password newPassword) {
		PasswordEmailSender mailSender = (PasswordEmailSender) context.getBean("forgotPasswordEmailSender");
		mailSender.sendEmail(user, newPassword.getSimplePassword());
	}

	/**
	 * Validates when a user is null.
	 * For v3 throws a ServiceExceptionCode.USER_NOT_FOUND
	 * @param user
	 */
	protected void validateUserNull(User user) {
		if (user == null) {
			throw createServiceException(ServiceExceptionCode.USER_NOT_FOUND);
		}
	}
}
