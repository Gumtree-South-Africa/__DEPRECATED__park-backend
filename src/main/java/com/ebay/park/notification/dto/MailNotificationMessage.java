/*
 * Copyright eBay, 2014
 */
package com.ebay.park.notification.dto;

import com.ebay.park.email.Email;
import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.NotificationDispatcher;
import com.ebay.park.notification.NotificationType;

import java.util.HashMap;
import java.util.Map;

/**
 * Mail message
 *
 * @author lucia.masola
 * @author gervasio.amy
 */
public class MailNotificationMessage extends NotificationMessage {

	private MailNotificationMessage(MailNotificationBuilder builder){
		super(builder.action, builder.type, builder.props);
	}

	public String getTemplate(){
		return props.get("mailTemplate");
	}
	
	public String getTo(){
		return props.get("to");
	}
	
	public String getSubject(){
		return props.get("subject");
	}

    /**
     * To be used from {@link NotificationDispatcher} in the "double dispatching" implementation of deciding where to queue a
     * {@link NotificationMessage}
     *
     * @param notificationDispatcher
     * @return
     */
	@Override
	public NotificationMessage doDispatch(NotificationDispatcher notificationDispatcher) {
		return notificationDispatcher.dispatchMailNotification(this);
	}

    public Email convertToEmail() {
        // FIXME - split this out from here and user an object mapper as dozer :)
        Email email = new Email();
        email.setTo(this.getTo());
        email.setSubject(this.getSubject());
        email.setTemplate(this.getTemplate());
        email.setParams(this.props);
        return email;
    }

	public static class MailNotificationBuilder{
		
		private Map<String, String> props;
		private final NotificationAction action;
		private final NotificationType type;
		
		public MailNotificationBuilder(NotificationAction action, NotificationType type, 
										String to, String subject, String template,
										String username){
			this.action = action;
			this.type = type;
			this.props = new HashMap<String, String>();
			props.put("to", to);
			props.put("subject", subject);
			props.put("mailTemplate", template);
			props.put("username", username);
		}
		
		public MailNotificationBuilder addProperty(String prop, String value){
			props.put(prop, value);
			return this;
		}
		
		public MailNotificationBuilder append(String prop, String value){
			props.put(prop, value);
			return this;
		}
		
		public MailNotificationBuilder append(Map<String, String> propsToAdd){
			props.putAll(propsToAdd);
			return this;
		}
		
		public MailNotificationMessage build(){
			return new MailNotificationMessage(this);
		}
		
	}
}
