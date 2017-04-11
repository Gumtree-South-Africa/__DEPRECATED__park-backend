package com.ebay.park.db.dao;

import com.ebay.park.db.entity.Tutorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 
 * @author federico.jaite
 */
public interface TutorialDao extends JpaRepository<Tutorial, Long> {

	@Query("from Tutorial tut join tut.idiom idi where idi.code = :lang and tut.step = :step")
	Tutorial findByStepAndLang(@Param("step") Integer step,
			@Param("lang") String lang);

}
