/*
 * Copyright eBay, 2014
 */
package com.ebay.park.db.dao;

import com.ebay.park.db.entity.Social;
import org.springframework.data.repository.CrudRepository;

/**
 * Data access interface for {@link Social} class
 * 
 * @author lucia.masola
 */
public interface SocialDao extends CrudRepository<Social, Long> {

	public Social findByDescription(String description);

}
