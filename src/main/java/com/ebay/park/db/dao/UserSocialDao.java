/*
 * Copyright eBay, 2014
 */
package com.ebay.park.db.dao;

import com.ebay.park.db.entity.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Data access interface for {@link UserSocial} class
 * 
 * @author marcos.lambolay
 */
public interface UserSocialDao extends CrudRepository<UserSocial, UserSocialPK> {
	public UserSocial findByToken(String token);

	@Query(value = "select * from user_social u, social s where u.us_user_id = :userId and u.us_social_id = s.soc_id and s.soc_description = '" + Social.FACEBOOK + "';", nativeQuery = true)
	public UserSocial findFacebookUser(@Param("userId") Long userId);
	
	@Query(value = "select * from user_social u, social s where u.us_user_id = :userId and u.us_social_id = s.soc_id and s.soc_description = '" + Social.TWITTER + "';", nativeQuery = true)
	public UserSocial findTwitterUser(@Param("userId") Long userId);

	public UserSocial findByUserId(String userId);
	
	@Query(value = "SELECT us FROM User us join us.publishedItems it "
			+ "WHERE (SQRT(POW(69.1 * (us.latitude - (:userLatitude)), 2) + POW(69.1 * ((:userLongitude) - us.longitude) * COS(us.latitude / 57.3), 2))) < :radius "
            + "AND us.id != :userId "
			+ "AND it.status = :activeStatus and it.deleted = false "
			+ "GROUP BY us.id " + "ORDER BY COUNT(it.id) DESC")
	public List<User> getRecommendedUsers(
			@Param("userId") Long userId,
			@Param("activeStatus") StatusDescription activeStatus,
			@Param("userLatitude") Double latitude,
			@Param("userLongitude") Double longitude,
			@Param("radius") Double radius, Pageable pageable);
	
	@Query(value = "SELECT us FROM User us join us.publishedItems it "
			+ "WHERE (SQRT(POW(69.1 * (us.latitude - (:userLatitude)), 2) + POW(69.1 * ((:userLongitude) - us.longitude) * COS(us.latitude / 57.3), 2))) < :radius "
			+ "AND it.status = :activeStatus and it.deleted = false "
			+ "GROUP BY us.id " + "ORDER BY COUNT(it.id) DESC")
	public List<User> getPublicRecommendedUsers(
			@Param("activeStatus") StatusDescription activeStatus,
			@Param("userLatitude") Double latitude,
			@Param("userLongitude") Double longitude,
			@Param("radius") Double radius, Pageable pageable);

	@Query(value = "select * from user_social u, social s where u.us_username = :socialUserId and u.us_social_id = s.soc_id and s.soc_description = :socialNetwork", nativeQuery = true)
	public List<UserSocial> findBySocialUserIdAndNetwork(@Param("socialUserId")String socialUserId, @Param("socialNetwork")String socialNetwork);
	
}
