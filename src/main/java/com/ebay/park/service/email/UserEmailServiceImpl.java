package com.ebay.park.service.email;

import static com.ebay.park.service.ServiceException.createServiceException;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ebay.park.db.dao.NotificationConfigDao;
import com.ebay.park.db.entity.NotificationConfig;
import com.ebay.park.db.entity.User;
import com.ebay.park.email.Email;
import com.ebay.park.email.MailSender;
import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.NotificationType;
import com.ebay.park.service.ServiceExceptionCode;

/**
 * User Email Sender
 * @author lucia.masola
 *
 */
@Component
public class UserEmailServiceImpl implements UserEmailService {
	
	@Autowired
	private MailSender mailSender;
	
	@Autowired
	private NotificationConfigDao notificationConfigDao;
	
	@Value("${email.template_location}")
	private String templateLocation;

	@Override
	public void sendEmail(User user, NotificationAction action, Map<String, String> params) {

		NotificationConfig config = notificationConfigDao.findNotification(NotificationType.EMAIL, action);
		
		if (config == null || config.getTemplateName() == null) {
			throw createServiceException(ServiceExceptionCode.NOTIFICATION_TEMPLATE_NOT_FOUND);
		}
		
		String subject = getSubject(user.getIdiom().getCode(), config.getTemplateName());
		
		if (params == null){
			params = new HashMap<String, String>();
		}
		params.put("username", user.getUsername());
		params.put("email", user.getEmail());
		//@formatter:off
		Email email = new Email();
		email.setTo(user.getEmail());
		email.setSubject(subject);
		email.setParams(params);
		email.setTemplate(templateLocation + config.getTemplateName());
		//@formatter:on
		mailSender.sendAsync(email);
		
	}

	private String getSubject(String lang, String emailSubject) {
		return "email.subject." + emailSubject;
	}
	
}
