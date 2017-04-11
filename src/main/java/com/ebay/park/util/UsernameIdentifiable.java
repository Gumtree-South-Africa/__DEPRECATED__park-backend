/*
 * Copyright eBay, 2014
 */
package com.ebay.park.util;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author lucia.masola
 * 
 */
public interface UsernameIdentifiable<T> {

	/**
	 * Returns the current object id
	 * 
	 * @return id
	 */
	@JsonIgnore
	T getUsername();

}
