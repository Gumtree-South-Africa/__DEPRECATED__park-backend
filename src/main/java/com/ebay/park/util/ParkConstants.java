/*
 * Copyright eBay, 2014
 */
package com.ebay.park.util;

import java.util.Arrays;
import java.util.List;

/**
 * Park Constants
 * 
 * @author lucia.masola
 * 
 */
public interface ParkConstants {

	String PARK_TOKEN_HEADER = "token";
    String LANG_ENG = "en";
    String LANG_SPA = "es";
    String LANGUAGE_HEADER = "Accept-Language";
    List<String> VALID_LANGUAGES = Arrays.asList(new String[] { LANG_ENG, LANG_SPA });
    String DEFAULT_LANGUAGE = LANG_SPA; // Current default app locale switch to "es". Previously was "en"
    String API_VERSION_HEADER = "apiVersion";
	String PLATFORM_HEADER = "platform";
	
	
	/*
	 * since location comes as "latitude,longitude" and longitude -> ##.##### ,
	 * longitude ###.###### \\d{1,2}: one or two numbers, (\\.\\d+)? optional
	 * "." and infinite numbers, -? optional
	 */
	String LOCATION_FORMAT = "-?\\d{1,2}(\\.\\d+)?,-?\\d{1,3}(\\.\\d+)?";
	
	int MAX_INTEGER_DIGITS_FOR_PRICES = 10;
	int MAX_DECIMAL_DIGITS_FOR_PRICES = 2;

	String SCHEDULED_PROFILE = "scheduled";
}
