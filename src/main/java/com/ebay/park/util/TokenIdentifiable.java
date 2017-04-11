/*
 * Copyright eBay, 2014
 */
package com.ebay.park.util;

/**
 * @author jpizarro
 * 
 */
public interface TokenIdentifiable<T> {

	/**
	 * Returns the current object id
	 * 
	 * @return id
	 */
	T getToken();

}
