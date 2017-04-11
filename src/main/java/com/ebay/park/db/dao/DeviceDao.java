/*
 * Copyright eBay, 2014
 */
package com.ebay.park.db.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ebay.park.db.entity.Device;

/**
 * Repository interface for {@link Device} class
 * 
 * @author marcos.lambolay
 */
public interface DeviceDao extends JpaRepository<Device, Long> {
	/**
	 * It finds the devices linked to a user.
	 * @param userId
	 * 		user to find devices linked to
	 * @param signedIn
	 * 		true for signed-in devices; otherwise, false.
	 * @return
	 * 		list of devices
	 */
	@Query(value="select d from Device d join d.userSession sessions where sessions.user.id =:userId and sessions.sessionActive = :signedIn" )
	public List<Device> findDevicesByUser(@Param("userId") Long userId, @Param("signedIn") boolean signedIn);

	List<Device> findByDeviceId(String deviceId);
}
