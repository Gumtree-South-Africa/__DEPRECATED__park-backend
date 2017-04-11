/*
 * Copyright eBay, 2014
 */
package com.ebay.park.db.entity;

import com.ebay.park.util.ParkConstants;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * The persistent class for the idiom database table.
 * 
 * @author juan.pizarro
 * 
 */
@Entity
@Table(name = "idiom")
public class Idiom extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idi_id")
	private Long id;

	@Column(name = "idi_code")
	private String code;

	// bi-directional many-to-one association to IdiomCategory
	@OneToMany(mappedBy = "idiom")
	private List<IdiomCategory> idiomCategories;

	// bi-directional many-to-one association to User
	@OneToMany(mappedBy = "idiom")
	private List<User> users;
	
	// bi-directional many-to-one association to User
	@OneToMany(mappedBy = "idiom")
	private List<Banner> banners;

	public Idiom() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long idiomId) {
		this.id = idiomId;
	}

	public String getCode() {
		//return this.code;
		
		return ParkConstants.DEFAULT_LANGUAGE; //TODO: Remove this if "es" is not the default language.
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<IdiomCategory> getIdiomCategories() {
		return this.idiomCategories;
	}

	public void setIdiomCategories(List<IdiomCategory> idiomCategories) {
		this.idiomCategories = idiomCategories;
	}

	public IdiomCategory addIdiomCategory(IdiomCategory idiomCategory) {
		getIdiomCategories().add(idiomCategory);
		idiomCategory.setIdiom(this);

		return idiomCategory;
	}

	public IdiomCategory removeIdiomCategory(IdiomCategory idiomCategory) {
		getIdiomCategories().remove(idiomCategory);
		idiomCategory.setIdiom(null);

		return idiomCategory;
	}

	public List<User> getUsers() {
		return this.users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public User addUser(User user) {
		getUsers().add(user);
		user.setIdiom(this);

		return user;
	}

	public User removeUser(User user) {
		getUsers().remove(user);
		user.setIdiom(null);

		return user;
	}

}