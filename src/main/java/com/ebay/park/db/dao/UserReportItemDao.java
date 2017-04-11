package com.ebay.park.db.dao;

import com.ebay.park.db.entity.UserReportItem;
import com.ebay.park.db.entity.UserReportItemPK;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 
 * @author cbirge
 * 
 */
public interface UserReportItemDao extends
		JpaRepository<UserReportItem, UserReportItemPK> {

	@Query(value = "select r from UserReportItem r where r.id.userReporterId = :userId and r.id.itemReportedId = :itemId")
	public UserReportItem findUserReportForItem(
			@Param("userId") Long userReporterId,
			@Param("itemId") Long itemReportedId);

	@Modifying
	@Query(value = "DELETE FROM UserReportItem r WHERE r.itemReported.id =:itemId")
	public void deleteByItem(@Param("itemId") Long itemId);
}
