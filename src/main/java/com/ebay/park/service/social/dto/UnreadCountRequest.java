package com.ebay.park.service.social.dto;

import com.ebay.park.service.ParkRequest;

/**
 * 
 * @author Julieta Salvadó
 *
 */
public class UnreadCountRequest extends ParkRequest {

	public UnreadCountRequest(String token, String lang) {
		super(token, lang);
	}
}
