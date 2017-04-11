/**
 * 
 */
package com.ebay.park.db.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ebay.park.db.entity.Country;

/**
 * @author jppizarro
 *
 */
public interface CountryDao extends JpaRepository<Country, Long> {
	
}
