/*
 * Copyright eBay, 2014
 */
package com.ebay.park.db.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * The persistent class for the notmobile database table.
 * 
 * @author juan.pizarro
 * 
 */
@Entity
@Table(name = "notmobile")
public class Notmobile extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nmo_id")
	private Long id;

	@Column(name = "nmo_description")
	private String description;

	// bi-directional many-to-one association to User
	@OneToMany(mappedBy = "notmobile")
	private List<User> users;

	public Notmobile() {
	}

	public Long getNotmobileId() {
		return this.id;
	}

	public void setNotmobileId(Long notmobileId) {
		this.id = notmobileId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<User> getUsers() {
		return this.users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public User addUser(User user) {
		getUsers().add(user);
		user.setNotmobile(this);

		return user;
	}

	public User removeUser(User user) {
		getUsers().remove(user);
		user.setNotmobile(null);

		return user;
	}

}