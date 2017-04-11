package com.ebay.park.util;

import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;

import org.apache.commons.lang.StringUtils;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.ebay.park.service.ServiceException.createServiceException;

/**
 * A set of validations and utilities common to all process
 * 
 * @author cbirge
 * 
 */
public class DataCommonUtil {

	/*
	 * since location comes as "latitude,longitude" and longitude -> ##.##### ,
	 * longitude ###.###### \\d{1,2}: one or two numbers, (\\.\\d+)? optional
	 * "." and infinite numbers, -? optional
	 */
	public static final String LOCATION_FORMAT = "-?\\d{1,2}(\\.\\d+)?,-?\\d{1,3}(\\.\\d+)?";

	public static final List<String> VALID_GENDER = Arrays.asList(new String[] {
			"M", "F" });

	/**
	 * Validates Location
	 * 
	 * @param location
	 *            a single String representing the location.
	 */
	public static void validateLocation(final String location) {

		if (StringUtils.isNotBlank(location) && !location.matches(LOCATION_FORMAT)) {
			throw createServiceException(ServiceExceptionCode.INVALID_LOCATION);
		}

	}

	/**
	 * Build location given the latitude and longitude
	 * 
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	public static String buildLocation(final Double latitude,
			final Double longitude) {
		return latitude + "," + longitude;
	}

	/**
	 * Parse a String date in ISO Format yyyy-MM-dd to date
	 * 
	 * @param date
	 *            string to parse
	 * @return date parsed
	 * @throws ParseException
	 */
	public static Date parseISODate(final String date) {
		DateTimeFormatter formatter = ISODateTimeFormat.date();
		return formatter.parseDateTime(date).toDate();
	}
	
	/**
	 * Parse a String date in ISO Format YYYY-MM-DDThh:mm:ssTZD to date
	 * 
	 * @param date
	 *            string to parse
	 * @return date parsed
	 * @throws ParseException
	 */
	public static Date parseISODateItem(String date) {
		DateTimeFormatter formatter = ISODateTimeFormat.dateTimeNoMillis();
		return formatter.parseDateTime(date).toDate();
	}

	/**
	 * Parses a date with the format yyyyMMdd'T'HHmmssZ from the joda time framework
	 * @param date a String depicting a date in the yyyyMMdd'T'HHmmss format
	 * @return the java Date equivalent of the date string parameter
	 */
	public static Date parseISOCompleteLocalDate(final String date) {
		DateTimeFormatter formatter = ISODateTimeFormat.basicDateTimeNoMillis();
		return formatter.parseDateTime(date).toDate();
	}

	/**
	 * Parses a string date in the unix time format.
	 *
	 * @param date a string depicting a date in unix time format
	 * @return the date equivalent of the string passed as a parameter
	 */
	public static Date parseUnixTime(final String date) {
	    Calendar calendar = Calendar.getInstance();
	    if (date.length() > 10) {
	        calendar.setTimeInMillis(Long.parseLong(date));
	    } else {
    		// multiplied by 1000 because it has to be milliseconds
    		calendar.setTimeInMillis(Long.parseLong(date) * 1000);
	    }
	    return calendar.getTime();
	}

	/**
	 * Print a Date in ISO Format yyyy-MM-dd
	 * 
	 * @param dateIso
	 * @return string in ISO format
	 * @throws ParseException
	 */
	public static String getDateAsISO(final Date dateIso) {
		DateTimeFormatter formatter = ISODateTimeFormat.date();
		return formatter.print(dateIso.getTime());
	}

	/**
	 * Print a DateTime in ISO Format YYYY-MM-DDThh:mm:ssTZD
	 * 
	 * @param dateIso
	 * @return string in ISO format
	 * @throws ParseException
	 */
	public static String getDateTimeAsISO(final Date dateIso) {
		DateTimeFormatter formatter = ISODateTimeFormat.dateTimeNoMillis();
		return formatter.print(dateIso.getTime());
	}

	/**
	 * Get the string in unix time format equivalent to the date passed as parameter
	 * @param date Date object to be parsed
	 * @return a String depicting the equivalent date to that passed as a parameter
	 */
	public static String getDateTimeAsUnixFormat(final Date date) {
		return Long.toString(date.getTime() / 1000);
	}

	/**
	 * Validates Date against ISO format.
	 * 
	 * @param date
	 *            a single String representing the date.
	 */
	public static boolean isISODateValid(String date) {
		try {
			parseISODate(date);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}


	/**
	 * Validates that the password obeys the rules that are applied in this method
	 * @param password
	 * @throws ServiceException with codes:
	 *   PWD_TOO_SHORT if password under 6 chars long
	 */
	/* //Validation currently disabled, but I keep the code because they may came back
	 * PWD_MUST_HAVE_SPEC_CHAR if it does not matches the current password
	 * pattern PWD_MUST_HAVE_DIGIT if the password does not have any numeric
	 * digit PWD_MUST_HAVE_MIXED_CASE if password does not have mayus and minus
	 * letters
	 */
	public static void validatePassword(String password) {

		if (password.length() < 6) {
			throw createServiceException(ServiceExceptionCode.PWD_TOO_SHORT);
		}

		if (password.length() > 15) {
			throw createServiceException(ServiceExceptionCode.PWD_TOO_LONG);
		}
		/*
		// ~ @ ^ $ & * ( ) - _ + = [ ] { } | \ , . ? !

		if (!password
				.matches(".*[~@#\\^\\$&\\*\\(\\)\\-\\_\\+=\\[\\]\\{\\}\\|\\\\,\\.\\?\\!]+.*")) {
			throw createServiceException(ServiceExceptionCode.PWD_MUST_HAVE_SPEC_CHAR);
		}

		if (!password.matches(".*[\\d]+.*")) {
			throw createServiceException(ServiceExceptionCode.PWD_MUST_HAVE_DIGIT);
		}

		if (!password.matches("(.*[a-z].*[A-Z].*)|(.*[A-Z].*[a-z].*)")) {
			throw createServiceException(ServiceExceptionCode.PWD_MUST_HAVE_MIXED_CASE);
		}
		 */
	}

	/**
	 * Adds days to a date.
	 * @param date original date
	 * @param plusDays amount of days to be added
	 * @return resulted date
	 */
	public static Date addDays(Date date, int plusDays) {
		return new Date(date.getTime() + plusDays * (1000 * 60 * 60 * 24));
	}

	public static Date getCurrentTime() {
		return new Date(System.currentTimeMillis());
	}

	public static boolean areDifferentDays(Date date1, Date date2) {
		if (date1 != null && date2!= null) { 
			return !(new SimpleDateFormat("d").format(date1).equals(new SimpleDateFormat("d").format(date2))); 
		}
		return false;
	}

	/**
	 * It returns the current date. Format: yyyy-MM-dd.
	 * @return a string date
     */
    public static String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }
}
