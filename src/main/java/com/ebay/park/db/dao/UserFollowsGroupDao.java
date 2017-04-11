/**
 * Copyright eBay, 2014
 */
package com.ebay.park.db.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ebay.park.db.entity.UserFollowsGroup;
import com.ebay.park.db.entity.UserFollowsGroupPK;

/**
 * @author diana.gazquez
 */
public interface UserFollowsGroupDao extends JpaRepository<UserFollowsGroup, UserFollowsGroupPK> {

	@Query("select ufg from UserFollowsGroup ufg where ufg.user.id = :userId and ufg.group.id = :groupId")
	public UserFollowsGroup find(
			@Param("groupId") Long groupId, 
			@Param("userId") Long userId);

	@Query("select ufg from UserFollowsGroup ufg where ufg.user.id = :userId")
	public List<UserFollowsGroup> findGroups(
			@Param("userId")Long userId);
	
	@Query("select ufg from UserFollowsGroup ufg where ufg.user.id = :userId and ufg.group.creator.id = :userId")
	public List<UserFollowsGroup> findOwnedGroups(
			@Param("userId")Long userId);
}
