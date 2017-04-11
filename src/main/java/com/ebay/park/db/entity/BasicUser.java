/**
 * Copyright eBay, 2014
 */
package com.ebay.park.db.entity;

import com.ebay.park.service.session.dto.UserSessionCache;

/**
 * @author diana.gazquez
 *
 */
public interface BasicUser {

	String getUsername();
	String getEmail();
	Long getId();
	String getToken();
	
	void populateSession(UserSessionCache userSessionCache);
}
