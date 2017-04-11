/*
 * Copyright eBay, 2014
 */
package com.ebay.park.db.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * The persistent class for the currency database table.
 * 
 * @author juan.pizarro
 * 
 */
@Entity
@Table(name = "currency")
public class Currency extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cur_id")
	private Long id;

	@Column(name = "cur_description")
	private String description;

	// bi-directional many-to-one association to Country
	@OneToMany(mappedBy = "currency")
	private List<Country> countries;

	public Currency() {
	}

	public Long getCurrencyId() {
		return this.id;
	}

	public void setCurrencyId(Long currencyId) {
		this.id = currencyId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Country> getCountries() {
		return this.countries;
	}

	public void setCountries(List<Country> countries) {
		this.countries = countries;
	}

	public Country addCountry(Country country) {
		getCountries().add(country);
		country.setCurrency(this);

		return country;
	}

	public Country removeCountry(Country country) {
		getCountries().remove(country);
		country.setCurrency(null);

		return country;
	}

}