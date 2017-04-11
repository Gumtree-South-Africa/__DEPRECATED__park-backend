/*
 * Copyright eBay, 2014
 */
package com.ebay.park.db.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ebay.park.db.entity.User;

/**
 * Repository interface for {@link User} class
 * 
 * @author marcos.lambolay
 */
public interface UserDao extends JpaRepository<User, Long> {

	public User findByUsernameOrEmail(String username, String email);

	public User findByUsername(String username);

	public User findByEmail(String email);

	@Query("select u from User u join u.userSessions session where session.token =:sessionToken ")
	public User findByToken(@Param("sessionToken") String sessionToken);

	public User findById(Long id);
	
	public List<User> findByIdIn(List<Long> usersId);

	@Query("select u from User u join u.userSocials us"
			+ " where us.id.socialId = :socialId and us.userId in (:socialUsersIds)")
	public List<User> findBySocialIdAndUserSocialIds(
			@Param("socialId") Long socialId,
			@Param("socialUsersIds") List<String> socialUsersIds);
	
	@Query(value="select u from User u where u.id in (select f.id.followerId from Follower f where f.id.userId = :userId)")
	public List<User> findFollowers(@Param("userId") Long userId);
	
	@Query(value = "select u from User u join u.groups ug where ug.id.groupId = :groupId")
	public Page<User> findByGroup(@Param("groupId") long groupId, Pageable pageable);

	@Query(value = "select usr from User usr where usr.id >= :userId")
	public List<User> getUsersFromIdRange(@Param("userId") Long userId, Pageable pageable);
	
	public User findByMobile(String phoneNumber);
	
	@Query("select u from User u join u.userSocials us"
			+ " where us.id.socialId = :socialId and us.userId = :socialUsersId")
	public User findBySocialIdAndUserSocialId(
			@Param("socialId") Long socialId,
			@Param("socialUsersId") String socialUsersId);
	
}
