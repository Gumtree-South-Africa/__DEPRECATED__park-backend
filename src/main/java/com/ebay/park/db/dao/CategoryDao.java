package com.ebay.park.db.dao;

import com.ebay.park.db.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Data access interface for the {@link Category} entity.
 * @author marcos.lambolay
 */
public interface CategoryDao extends JpaRepository<Category, Long>{

	@Query(value = "select cat from Category cat order by cat.catOrder asc")
	List<Category> getAllCategories();
	
}
