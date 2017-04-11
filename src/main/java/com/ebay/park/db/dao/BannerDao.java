/*
 * Copyright eBay, 2014
 */
package com.ebay.park.db.dao;

import com.ebay.park.db.entity.Banner;
import com.ebay.park.db.entity.Idiom;
import com.ebay.park.service.banner.dto.BannerPriority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for {@link Banner} class
 * 
 * @author lucia.masola
 */
public interface BannerDao extends JpaRepository<Banner, Long> {

	List<Banner> findByPriorityAndIdiom(BannerPriority system, Idiom idiom);


}
