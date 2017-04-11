package com.ebay.park.service.support.command;

import com.ebay.park.email.Email;
import com.ebay.park.email.MailSender;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;
import com.ebay.park.service.support.dto.SendUserFeedbackRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Component
public class SendUserFeedbackCmd implements	ServiceCommand<SendUserFeedbackRequest, Void> {

	@Autowired
	private MailSender mailSender;
	
	@Value("${email.support_address}")
	private String supportEmailAddress;
	
	@Value("${email.park_address}")
	private String parkEmailAddress;
	
	@Autowired
	private SessionService sessionService;
	
	private Properties properties;

	private static final String VERSION_PROPERTY = "version";
	private static final String VERSION_FILE = "version.properties";
	private static final String SUBJECT = "email.support_subject";
	
	@Override
	public Void execute(SendUserFeedbackRequest request) throws ServiceException {
		UserSessionCache session = sessionService.getUserSession(request.getToken());
		
		Properties properties = getProperties();
		
		Email email = new Email();
		email.setFrom(parkEmailAddress);
		email.setHtmlFormat(false);
		Map<String, String> params = new HashMap<String, String>();
		params.put("subject", SUBJECT);
		params.put("token", request.getToken());
		email.setParams(params);
		email.setTemplate(null);
		email.setTo(supportEmailAddress);
		
		StringBuilder body = new StringBuilder();
		body.append("message: ").append(request.getMessage()).append(";\n");
		body.append("deviceModel: ").append(request.getDeviceModel()).append(";\n");
		body.append("appVersion: ").append(request.getAppVersion()).append(";\n");
		body.append("countryCode: ").append(request.getCountryCode()).append(";\n");
		body.append("buildVersion: ").append(properties.getProperty(VERSION_PROPERTY)).append(";\n");
		body.append("userEmail: ").append(session.getEmail()).append(";\n");
		body.append("userName: ").append(session.getUsername()).append(";\n");

		email.setRawBody(body.toString());
		mailSender.sendAsync(email);
		return null;
	}

	Properties getProperties() {
		if(this.properties == null) {
			Properties props = new Properties();
			try {
				props.load(this.getClass().getClassLoader().getResourceAsStream(VERSION_FILE));
				properties = props;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

}
