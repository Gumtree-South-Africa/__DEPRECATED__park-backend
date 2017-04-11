package com.ebay.park.db.dao;

import com.ebay.park.db.entity.Follower;
import com.ebay.park.db.entity.FollowerPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Dao to query about followers.
 * @author lucia.masola
 *
 */
public interface FollowerDao extends JpaRepository<Follower, FollowerPK> {

	@Query(value="select f from Follower f where f.id.followerId = :userId")
	public List<Follower> findFollowings(@Param("userId") Long userId);

	@Query(value="select f from Follower f where f.id.followerId = :userId and f.id.userId = :userFollowedId" )
	public Follower findFollower(@Param("userId") Long userFollowerId, @Param("userFollowedId") Long userFollowedId);

}
