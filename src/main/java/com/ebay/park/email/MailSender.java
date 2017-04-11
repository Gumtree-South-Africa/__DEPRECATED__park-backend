/*
 * Copyright eBay, 2014
 */
package com.ebay.park.email;

/**
 * Interface to be implemented by any email sender
 *
 * @author jpizarro
 */
public interface MailSender {

	void send(Email email);

	/**
	 * Send an email in a new {@link Thread}
	 * @param email the email to be sent
	 */
	void sendAsync(Email email);

}
