/**
 * 
 */
package com.ebay.park.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author jppizarro
 * 
 */
@Component
public class ParkTokenUtil {

	public String createSessionToken() {
		return UUID.randomUUID().toString();
	}

}
