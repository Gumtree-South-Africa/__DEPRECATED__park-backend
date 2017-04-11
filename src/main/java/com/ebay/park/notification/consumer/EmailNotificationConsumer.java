/*
 * Copyright eBay, 2014
 */
package com.ebay.park.notification.consumer;

import com.ebay.park.email.Email;
import com.ebay.park.email.MailSender;
import com.ebay.park.notification.dto.MailNotificationMessage;
import com.ebay.park.service.ServiceExceptionCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;

/**
 * This is basically a JMS consumer. It's listening the queue <code>emailQueueDestination</code>
 *
 * @author gervasio.amy
 * @see JmsListener
 */
@Component
public class EmailNotificationConsumer {

    private static final Logger logger = LogManager.getLogger(EmailNotificationConsumer.class);

	@Autowired
	private MailSender mailSender;

	@JmsListener(destination = "emailQueueDestination")
	public void consume(MailNotificationMessage emailNotification) {
	    logger.debug("Email consumer starting");
        try {
            Email email = createEmail(emailNotification);
            mailSender.sendAsync(email);
        } catch (Exception e) {
            logger.error("EmailNotificationConsumer unexpected error");
            throw createServiceException(ServiceExceptionCode.INVALID_NOTIFICATION_PROPERTIES, e);
        }
	}

	private Email createEmail(MailNotificationMessage emailNotification) {
		return emailNotification.convertToEmail();
	}

}
