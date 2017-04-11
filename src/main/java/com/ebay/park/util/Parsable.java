/*
 * Copyright eBay, 2014
 */
package com.ebay.park.util;


/**
 * @author jpizarro
 * 
 */
public interface Parsable<T> {

	/**
	 * Creates a notification from an existing object.
	 * 
	 * @return A notification instance.
	 */
	public T parse();

}
