package com.ebay.park.service.moderation.command;

import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.User;
import com.ebay.park.email.Email;
import com.ebay.park.email.MailSender;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.moderation.dto.ContactUserRequest;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;
@Component
public class ContactUserCmdImpl implements ContactUserCmd {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ContactUserCmdImpl.class);

	@Autowired
	private UserDao userDao;

	@Autowired
	private MailSender mailSender;

	@Override
	public ServiceResponse execute(ContactUserRequest request)
			throws ServiceException {

		User user = userDao.findOne(request.getUserId());
		if (user == null) {
			throw createServiceException(ServiceExceptionCode.USER_NOT_FOUND);
		}

		if (StringUtils.isEmpty(user.getEmail())) {
			throw createServiceException(ServiceExceptionCode.EMAIL_USER_EMPTY_EMAIL);
		}

		try {
			// @formatter:off			
			Email email = new Email(user.getEmail(), null, null, request.getSubject());
			email.setRawBody(request.getBody());
			email.setHtmlFormat(false);
			// @formatter:on

			mailSender.sendAsync(email);

			if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Send email to user id: {}successfull", request.getUserId());
			}
			return ServiceResponse.SUCCESS;
		} catch (Exception e) {
            LOGGER.error("Error sending email to user id  [{}]", request.getUserId(), e);
			throw createServiceException(ServiceExceptionCode.EMAIL_SEND_ERROR);
		}
	}
}
