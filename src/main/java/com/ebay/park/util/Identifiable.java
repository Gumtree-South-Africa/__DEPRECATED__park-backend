/*
 * Copyright eBay, 2014
 */
package com.ebay.park.util;

/**
 * @author lucia.masola
 * 
 */
public interface Identifiable<T> {

	public static enum IdentifiableType {
		TOKEN, USERNAME, ID;
	};

	public IdentifiableType getIdentityField();

	public T getIdentityValue();

}
