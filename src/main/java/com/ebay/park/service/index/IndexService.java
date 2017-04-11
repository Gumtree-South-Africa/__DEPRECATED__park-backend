/**
 * Copyright eBay, 2014
 */
package com.ebay.park.service.index;

/**
 * @author diana.gazquez
 *
 */
public interface IndexService {

	/**
	 * Full re index , to be used when the structure of the index (fields) changed. This operation should run always in maintenance mode.
	 * 
	 */
	boolean reindex();

}
