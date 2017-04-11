package com.ebay.park.db.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.ebay.park.db.entity.OrphanedDevice;

/**
 * Data access interface for {@link OrphanedDevice} entity.
 * @author Julieta Salvadó
 */
public interface OrphanedDeviceDao extends JpaRepository<OrphanedDevice, Long>{

	@Modifying
	@Transactional
	@Query(value="delete from OrphanedDevice o where o.uniqueDeviceId = :uuid ")
	public void delete(@Param("uuid") String uniqueDeviceId);

	@Query(value="select o from OrphanedDevice o where o.uniqueDeviceId = :uuid ")
	public OrphanedDevice findByUniqueDeviceId(@Param("uuid") String uniqueDeviceId);

}
