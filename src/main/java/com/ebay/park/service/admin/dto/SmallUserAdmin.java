package com.ebay.park.service.admin.dto;

import com.ebay.park.db.entity.UserAdmin;

public class SmallUserAdmin {

	private Long id;
	private String username;
	private String email;
	private String name;
	private String lastname;

	public SmallUserAdmin() {
		super();
	}

	public SmallUserAdmin(UserAdmin userAdmin) {
		super();
		setId(userAdmin.getId());
		setUsername(userAdmin.getUsername());
		setName(userAdmin.getName());
		setLastname(userAdmin.getLastname());
		setEmail(userAdmin.getEmail());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
}
