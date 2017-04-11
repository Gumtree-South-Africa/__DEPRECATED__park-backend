/*
 * Copyright eBay, 2014
 */
package com.ebay.park.util;

import java.security.NoSuchAlgorithmException;

/**
 * @author juan.pizarro
 */
public class PasswdHashException extends RuntimeException {

	private static final long serialVersionUID = 2041937280768425485L;

	public PasswdHashException(NoSuchAlgorithmException e) {
		super(e);
	}

}
