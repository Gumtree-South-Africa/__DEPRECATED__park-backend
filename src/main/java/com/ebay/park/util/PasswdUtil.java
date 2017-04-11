/*
 * Copyright eBay, 2014
 */
package com.ebay.park.util;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author juan.pizarro
 */
@Component
public class PasswdUtil {

	private static final String ALGORITHM = "SHA-256";
	public static final int DEFAULT_PASSWORD_LENGHT = 6;

	/**
	 * Compute the hash
	 * 
	 * @param password
	 *            Plain text password
	 * @return The hash obtained
	 * @throws PasswdHashException
	 */
	public byte[] hash(String password) throws PasswdHashException {
		try {
			MessageDigest sha256 = MessageDigest.getInstance(ALGORITHM);
			byte[] passBytes = password.getBytes();
			byte[] passHash = sha256.digest(passBytes);
			return passHash;
		} catch (NoSuchAlgorithmException e) {
			throw new PasswdHashException(e);
		}
	}

	/**
	 * Determines if the first password (that is not hashed) represents the same
	 * password as hashedPassword (that is hashed)
	 * 
	 * @param password
	 *            a String
	 * @param hashedPassword
	 *            a SHA-256 hashed String
	 * @return true if both Strings represent the same password
	 */
	public boolean equalsToHashedPassword(String password, byte[] hashedPassword) {
		try {
			MessageDigest sha256 = MessageDigest.getInstance(ALGORITHM);
			return MessageDigest.isEqual(sha256.digest(password.getBytes()), hashedPassword);
		} catch (NoSuchAlgorithmException e) {
			throw new PasswdHashException(e);
		}
	}
}
