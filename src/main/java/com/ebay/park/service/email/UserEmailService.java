package com.ebay.park.service.email;

import com.ebay.park.db.entity.User;
import com.ebay.park.notification.NotificationAction;

import java.util.Map;

/**
 * User email retriever.
 * @author lucia.masola
 *
 */
public interface UserEmailService {
	
	/**
	 * Sends the <code>user</code> an email for the given action.
	 */
	
	/**
	 * Sends the <code>user</code> an email for the given action.
	 * @param user the user to which the email is sent
	 * @param action the NotificationMessage Action representing the type of email to be send
	 * @param params a Map with the parameters to complete inside the email
	 */
	public void sendEmail(User user, NotificationAction action, Map<String, String> params);
}
