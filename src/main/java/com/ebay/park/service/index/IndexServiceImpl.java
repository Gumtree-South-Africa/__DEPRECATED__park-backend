/**
 * Copyright eBay, 2014
 */
package com.ebay.park.service.index;

import com.ebay.park.service.index.commmand.ReindexCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author diana.gazquez
 *
 */
@Service
public class IndexServiceImpl implements IndexService {

	@Autowired
	ReindexCmd  reindexCmd;
	
	/* (non-Javadoc)
	 * @see com.ebay.park.service.index.IndexService#search()
	 */
	@Override
	public boolean reindex() {
		return reindexCmd.execute(null);
	}

	



}
