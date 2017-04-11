/*
 * Copyright eBay, 2014
 */
package com.ebay.park.db.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ebay.park.db.entity.DeviceType;
import com.ebay.park.db.entity.UserSession;

/**
 * Repository interface for {@link com.ebay.park.db.entity.UserSession} class
 * 
 * @author gabriel.sideri
 */
public interface UserSessionDao extends JpaRepository<UserSession, Long> {

	@Query(value = "select s from UserSession s where s.uniqueDeviceId = :uniqueDeviceId")
	public List<UserSession> findUserSessionsByUniqueDeviceId(@Param("uniqueDeviceId") String uniqueDeviceId);

	@Query(value = "select s from UserSession s where s.device.deviceId=:deviceId and s.device.platform =:platform")
	public List<UserSession> findUserSessionsByDeviceId(@Param("deviceId") String deviceId,
			@Param("platform") DeviceType deviceType);

	@Query(value = "select s from UserSession s where s.user.id = :userId ")
	public List<UserSession> findUserSessionsByUser(@Param("userId") Long id);

	@Query(value = "select s from UserSession s where s.token =:currentToken ")
	public UserSession findUserSessionByToken(@Param("currentToken") String currentToken);

	@Query(value = "select s from UserSession s where not exists (from Device d where s.device.id = d.id) and "
			+ "s.lastSuccessfulLogin <= :limit")
	public List<UserSession> findExpiredWebUserSessionsByUser(@Param("limit") Date limitDate);
	
	 /**
     * Returns swrve user IDs with items without contact during a number of days
     * @param numDays
     *
     * @return
     * <ul>
     *     <li>result[0] -> use_ses_swrve_id
     *     <li>result[1] -> ite_publisher_id
     *     <li>result[2] -> dev_platform
     * </ul>
     */
    @Query(value = "select distinct us.use_ses_swrve_id, "
            + "ite.ite_publisher_id, dev.dev_platform "
            + "from item ite, user_session us, session_device dev "
            + "where ite.ite_publisher_id = us.use_id "
            + "and us.use_ses_swrve_id is not null "
            + "and us.use_ses_active = 1 "
            + "and dev.dev_session_id = us.use_ses_id "
            + "and ite.ite_deleted = false and ite.ite_stu_id = 1 "
            + "and (ite.ite_id not in (select distinct ul.ul_ite_id from park.user_itemliked ul) "
            + "and ite.ite_id not in (select distinct con.con_ite_id from park.conversation con)) "
            + "and date(ite.ite_published) = date(ite.ite_last_update) "
            + "and date(ite.ite_published) = date_sub(curdate(), interval :numDays day);", nativeQuery = true)
    List<Object[]> findSwrveIdsWithItemsWithoutContact(@Param("numDays") int numDays);

}
