/*
 * Copyright eBay, 2014
 */
package com.ebay.park.util;

import com.ebay.park.service.ServiceExceptionCode;
import org.apache.commons.lang.StringUtils;

import static com.ebay.park.service.ServiceException.createServiceException;

/**
 * @author jpizarro
 * 
 */
public class LocationUtil implements ParkConstants {

	private LocationUtil() {

	}

	/**
	 * Validates Location Field.
	 * 
	 * @param location
	 *            a single String representing the location.
	 */
	public static void validateLocation(String location) {

		if (StringUtils.isBlank(location)) {
			throw createServiceException(ServiceExceptionCode.EMPTY_LOCATION);
		}

		if (!location.matches(LOCATION_FORMAT)) {
			throw createServiceException(ServiceExceptionCode.INVALID_LOCATION);
		}

	}

	/**
	 * Convert Miles To Kilometers
	 * 
	 * @param miles
	 * @return
	 */
	public static double convertMilesToKm(double miles) {
		if (miles > 0) {
			return miles * 1.609344;
		}
		return 0;
	}

	public static boolean validLatitude(Double latitude) {
		return latitude == null ? false : latitude <= 90L && latitude>=-90;
	}

	public static boolean validLongitude(Double latitude) {
		return latitude == null ? false : latitude <= 180L && latitude>=-180;
	}
}
