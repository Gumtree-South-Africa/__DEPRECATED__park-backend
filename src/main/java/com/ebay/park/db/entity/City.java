/*
 * Copyright eBay, 2014
 */
package com.ebay.park.db.entity;

import javax.persistence.*;

import java.io.Serializable;
import java.util.List;

/**
 * The persistent class for the city database table.
 * 
 * @author juan.pizarro
 * 
 */
@Entity
@Table(name = "city")
public class City extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cit_id")
	private Long id;

	@Column(name = "cit_description")
	private String description;

	// bi-directional many-to-one association to State
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cit_sta_id")
	private State state;
	
	@Column(name = "cit_zip_code")
	private String zipCode;

	// bi-directional many-to-one association to User
	@OneToMany(mappedBy = "city")
	private List<User> users;

	public City() {
	}

	public Long getCityId() {
		return this.id;
	}

	public void setCityId(Long cityId) {
		this.id = cityId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public State getState() {
		return this.state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public List<User> getUsers() {
		return this.users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public User addUser(User user) {
		getUsers().add(user);
		user.setCity(this);

		return user;
	}

	public User removeUser(User user) {
		getUsers().remove(user);
		user.setCity(null);

		return user;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

}