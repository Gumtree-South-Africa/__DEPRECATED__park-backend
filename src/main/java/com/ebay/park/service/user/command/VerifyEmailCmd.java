package com.ebay.park.service.user.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.email.EmailVerificationHelper;
import com.ebay.park.service.user.dto.VerifyEmailRequest;
import com.ebay.park.util.EmailVerificationUtil;

/**
 * @author federico.jaite
 * 
 */
@Component
public class VerifyEmailCmd implements
		ServiceCommand<VerifyEmailRequest, String> {

	@Autowired
	private UserDao userDao;

	@Autowired
	private EmailVerificationHelper emailVerificationHelper;

	@Autowired
	private EmailVerificationUtil emailVerificationUtil;

	@Value("${emailVerification.url.success}")
	private String emailVerificationSuccessUrl;

	@Value("${emailVerification.url.error}")
	private String emailVerificationErrorUrl;
	
	@Override
	public String execute(VerifyEmailRequest request) throws ServiceException {

		String email = emailVerificationHelper.decrypt(request.getEmail());

		User user = userDao.findByEmail(email);

		if (user == null) {
			return emailVerificationErrorUrl;
		}

		String tempToken = emailVerificationHelper.decrypt(request
				.getTemporaryToken());

		if (!tempToken.equals(user.getAccess().getTemporaryToken())) {
			return emailVerificationErrorUrl;
		}

		emailVerificationUtil.verify(user);
		userDao.save(user);

		return emailVerificationSuccessUrl;
	}
		
}
