/*
 * Copyright eBay, 2014
 */
package com.ebay.park.db.dao;

import com.ebay.park.db.entity.Idiom;
import org.springframework.data.repository.CrudRepository;

/**
 * @author jpizarro
 * 
 */
public interface IdiomDao extends CrudRepository<Idiom, Long> {

	public Idiom findByCode(String code);

}
