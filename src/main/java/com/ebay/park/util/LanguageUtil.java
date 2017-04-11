/*
 * Copyright eBay, 2014
 */
package com.ebay.park.util;

import com.ebay.park.db.entity.User;
import com.ebay.park.service.session.dto.UserSessionCache;

/**
 * @author lucia.masola
 */
public class LanguageUtil implements ParkConstants {

	/**
	 * It converts the language definition into a valid one, if needed.
	 * Rules:
	 * <ul>
	 * <li>if null, default language is returned</li>
	 * <li>if valid language, then this language is returned</li>
	 * <li>otherwise, the default language is chosen</li>
	 * </ul>
	 * @param lang the language. It can be null.
	 * @return a valid language
	 */
	public static String getValidLanguage(String lang) {
		if (lang == null) {
			return DEFAULT_LANGUAGE;
		}

		if (VALID_LANGUAGES.contains(lang)) {
			return lang;
		}

		return DEFAULT_LANGUAGE;
	}

	/**
	 * It decides which language should be used.
	 * @param user user to get the language from
	 * @param requestLanguage language in the request
	 * @return the resulted language.
	 */
	public static String getLanguageForUserRequest(User user, String requestLanguage) {
		if ((requestLanguage == null) && (user != null)) {
			return user.getIdiom().getCode();
		}
		return getValidLanguage(requestLanguage);
	}

	/**
	 * It decides which language should be used.
	 * @param session session to get the language from
	 * @param requestLanguage language in the request
	 * @return the resulted language.
	 */
	public static String getLanguageForUserRequest(UserSessionCache session, String requestLanguage) {
		if ((requestLanguage == null) && (session != null)) {
			return getValidLanguage(session.getLang());
		}
		return getValidLanguage(requestLanguage);
	}
}
