/*
 * Copyright eBay, 2014
 */
package com.ebay.park.util;

import java.util.Map;

/**
 * Define the to map behavior given key and value types.
 * 
 * @author jpizarro
 * 
 */
public interface Mappable<K, V> {

	/**
	 * Convert the object to map<K, V>
	 * 
	 * @return Hashmap<K,V> instance.
	 */
	public Map<K, V> toMap();

}
