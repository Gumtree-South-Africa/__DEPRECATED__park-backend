/**
 * 
 */
package com.ebay.park.util;

/**
 * @author jppizarro
 *
 */
public interface ExpirableObject extends PoolableObject {

	/**
	 * Returns if the object is expired.
	 * @return true if the object is expired, false otherwise.
	 */
	public boolean isExpired();
	
}
