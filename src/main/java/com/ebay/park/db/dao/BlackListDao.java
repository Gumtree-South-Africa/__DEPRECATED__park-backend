package com.ebay.park.db.dao;

import com.ebay.park.db.entity.BlackList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Data access interface for the {@link BlackList} entity.
 * 
 * @author lucia.masola
 */
public interface BlackListDao extends JpaRepository<BlackList, Long> {

	@Query(value = "select b.description from BlackList b")
	//@Cacheable("blacklist_descs")
	public List<String> findAllBlackListDescription();

	public BlackList findByDescription(String description);

}
