/*
 * Copyright eBay, 2014
 */
package com.ebay.park.db.entity;

import javax.persistence.*;

import java.io.Serializable;
import java.util.List;

/**
 * The persistent class for the state database table.
 * 
 * @author juan.pizarro
 * 
 */
@Entity
@Table(name = "state")
public class State extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sta_id")
	private Long id;

	@Column(name = "sta_description")
	private String description;

	// bi-directional many-to-one association to City
	@OneToMany(mappedBy = "state")
	private List<City> cities;

	// bi-directional many-to-one association to Country
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sta_cou_id")
	private Country country;
	
	@Column(name = "sta_code")
	private String stateCode;

	public State() {
	}

	public Long getStatusId() {
		return this.id;
	}

	public void setStatusId(Long statusId) {
		this.id = statusId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<City> getCities() {
		return this.cities;
	}

	public void setCities(List<City> cities) {
		this.cities = cities;
	}

	public City addCity(City city) {
		getCities().add(city);
		city.setState(this);

		return city;
	}

	public City removeCity(City city) {
		getCities().remove(city);
		city.setState(null);

		return city;
	}

	public Country getCountry() {
		return this.country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

}