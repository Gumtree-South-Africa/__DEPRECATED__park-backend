package com.ebay.park.service.moderation.command;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.User;
import com.ebay.park.email.Email;
import com.ebay.park.email.MailSender;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.moderation.dto.ContactPublisherRequest;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;

@Component
public class ContactPublisherCmdImpl implements ContactPublisherCmd {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ContactPublisherCmdImpl.class);

	@Autowired
	private ItemDao itemDao;

	@Autowired
	private MailSender mailSender;

	@Override
	public ServiceResponse execute(ContactPublisherRequest request)
			throws ServiceException {

		Item item = itemDao.findOne(request.getItemId());

		if (item == null) {
			throw createServiceException(ServiceExceptionCode.ITEM_NOT_FOUND);
		}

		User publisher = item.getPublishedBy();

		if (StringUtils.isEmpty(publisher.getEmail())) {
			throw createServiceException(ServiceExceptionCode.EMAIL_PUBLISHER_EMPTY_EMAIL);
		}

		try {
			// @formatter:off			
			Email email = new Email(publisher.getEmail(), null, null, request.getSubject());
			email.setRawBody(request.getBody());
			email.setHtmlFormat(false);
			// @formatter:on

			mailSender.sendAsync(email);

			if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Send email to publisher of item: {} successful", request.getItemId());
			}
			return ServiceResponse.SUCCESS;
		} catch (Exception e) {
            LOGGER.error("Error sending email to publisher of item  [{}]", request.getItemId(), e);
			throw createServiceException(ServiceExceptionCode.EMAIL_SEND_ERROR, e);
		}
	}
}
