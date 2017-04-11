/*
 * Copyright eBay, 2014
 */
package com.ebay.park.email;

import com.ebay.park.notification.dto.NotifiableServiceResult;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;
import com.ebay.park.util.LanguageUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.util.StringUtils;

import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.ebay.park.service.ServiceException.createServiceException;

/**
 * The implementation of {@link MailSender} based on Spring framework utility {@link JavaMailSender}
 *
 * @author jpizarro
 */
@Component
public class MailSenderImpl implements MailSender {

	private static final Logger logger = LogManager.getLogger(MailSenderImpl.class);

	@Autowired
	private VelocityEngineFactoryBean velocityEngineFactory;

	@Autowired
	private JavaMailSender mailSender;

	@Value("${mail.from}")
	private String defaultFrom;

	@Value("${mail.fromName}")
	private String defaultFromName;

	@Value("${park.web.url}")
	private String parkWebUrl;

	@Value("${createItem.shareUrlForItem}")
	private String itemUrl;

	@Value("${mail.logo.url}")
	private String logo;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private SessionService sessionService;

	@Override
	public void send(final Email email) {

		if (StringUtils.isEmpty(email.getFrom())) {
			email.setFrom(defaultFrom);
		}

		if (StringUtils.isEmpty(email.getFromName())) {
			email.setFromName(defaultFromName);
		}

		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				logger.debug("About to prepare email");
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
				message.setTo(email.getTo());
				message.setFrom(email.getFrom(), email.getFromName());

				Locale locale = getLocale(email.getParams().get("token"));
				String subject = messageSource.getMessage(email.getSubject(), null, locale);
				//Following line was added in order to replace the placeholder for "Facebook friend using the app" email notification
				//see "email.subject.fb_friend-using-the-app.vm" property
				subject = StringUtils.replace(subject, "${friendUsername}", email.getParams().get(NotifiableServiceResult.USER_NAME));
				message.setSubject(subject);

				Map<String, Object> model = new HashMap<String, Object>();
				model.put("messages", messageSource); // i18n purpose
				model.put("locale", locale);
				model.put("params", email.getParams());
				model.put("webUrl", parkWebUrl);
				model.put("itemUrl", itemUrl);
				model.put("logoUrl", logo);

				String text;
				if (!StringUtils.isEmpty(email.getTemplate())) {
					//@formatter:off
					text = VelocityEngineUtils.mergeTemplateIntoString(
							velocityEngineFactory.createVelocityEngine()
							, email.getTemplate()
							, "UTF-8"
							, model);
					//@formatter:on
				} else {
					text = email.getRawBody();
				}
				message.setText(text, (email.isHtmlFormat() != null) ? email.isHtmlFormat() : true);
			}
		};

		try {
			logger.debug("About to send email");
			this.mailSender.send(preparator);
		} catch (Exception ex) {
			logger.error("Error sending Email");
			throw createServiceException(ServiceExceptionCode.EMAIL_SEND_ERROR, ex);
		}

	}

	private Locale getLocale(String token) {
		String lang = null;
		if (token != null) {
			UserSessionCache userSession = sessionService.getUserSession(token);
			lang = userSession.getLang();
		}

		return new Locale(LanguageUtil.getValidLanguage(lang));

	}

	@Override
	public void sendAsync(final Email email) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				send(email);
			}
		}).start();
	}

}