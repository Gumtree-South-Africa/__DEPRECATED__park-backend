/*
 * Copyright eBay, 2014
 */
package com.ebay.park.persistence;

/**
 * Exception thrown when publishing is not successfully performed.
 * @author Julieta Salvadó
 *
 */
public class FileSystemPublishException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public FileSystemPublishException(Throwable t) {
		super(t);
	}

}
