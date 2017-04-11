package com.ebay.park.db.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ebay.park.db.entity.City;

/**
 * Data access interface for {@link City} entity.
 * @author marcos.lambolay
 */
public interface CityDao extends JpaRepository<City, Long> {

	@Query(value = "select cit.zipCode from City cit join cit.state st where st.stateCode = :stateCode")
	public List<String> getCityZipCodesByStateCode(@Param("stateCode") String stateCode);
	
	@Query(value= "select distinct cit.description from City cit join cit.state st where st.stateCode = :stateCode")
	public List<String> getCityDescriptionsByState(@Param("stateCode") String stateCode);

	@Query(value = "select cit.zipCode from City cit join cit.state st "
			+ "where st.stateCode = :stateCode and cit.description = :cityName")
	public List<String> getZipCodesByCityAndState(@Param("cityName") String cityName, @Param("stateCode") String stateCode);
}
