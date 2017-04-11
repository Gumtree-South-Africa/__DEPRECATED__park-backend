/*
 * Copyright eBay, 2014
 */
package com.ebay.park.db.dao;

import com.ebay.park.db.entity.Feed;
import com.ebay.park.db.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Repository interface for {@link Feed} class
 * 
 * @author lucia.masola
 */
public interface FeedDao extends JpaRepository<Feed, Long> {

	@Query(value="select f from Feed f where f.owner.id = :ownerId order by f.id DESC" )
	public List<Feed> findOrderedFeeds(@Param("ownerId") Long ownerId);

	public Long countByOwnerAndRead(User owner, boolean read);

	public Page<Feed> findByCreationDateBeforeAndRead(Date date, boolean read, Pageable pageable);
	
	@Query(value="select f from Feed f join f.notificationConfig notConfig"
			+ " where notConfig.id = :notificationId and f.owner.id = :ownerId"
			+ " and f.user.id = :userId")
	public List<Feed> findFeedToUpdate(@Param("notificationId") Long notificationId,
			@Param("ownerId") Long ownerId, @Param("userId") Long userId);

    public Feed findById(Long FeedId);
}
