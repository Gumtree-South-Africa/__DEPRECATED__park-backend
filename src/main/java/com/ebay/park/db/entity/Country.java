/*
 * Copyright eBay, 2014
 */
package com.ebay.park.db.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * The persistent class for the country database table.
 * 
 * @author juan.pizarro
 * 
 */
@Entity
@Table(name = "country")
public class Country extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cou_id")
	private Long id;

	@Column(name = "cou_description")
	private String description;

	// bi-directional many-to-one association to Currency
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cou_cur_id")
	private Currency currency;

	// bi-directional many-to-one association to State
	@OneToMany(mappedBy = "country")
	private List<State> states;

	public Country() {
	}

	public Long getCountryId() {
		return this.id;
	}

	public void setCountryId(Long countryId) {
		this.id = countryId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Currency getCurrency() {
		return this.currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public List<State> getStates() {
		return this.states;
	}

	public void setStates(List<State> states) {
		this.states = states;
	}

	public State addState(State state) {
		getStates().add(state);
		state.setCountry(this);

		return state;
	}

	public State removeState(State state) {
		getStates().remove(state);
		state.setCountry(null);

		return state;
	}

}