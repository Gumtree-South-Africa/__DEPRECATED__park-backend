/*
 * Copyright eBay, 2014
 */
package com.ebay.park.db.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * The persistent class for the user_social database table.
 * 
 * @author juan.pizarro
 * 
 */
@Entity
@Table(name = "user_social")
public class UserSocial extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private UserSocialPK id;

	@Column(name = "us_token")
	private String token;
	
	@Column(name = "us_token_secret")
	private String tokenSecret;

	@Column(name = "us_username")
	private String userId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "us_social_id", insertable = false, updatable = false)
	private Social social;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "us_user_id", insertable = false, updatable = false)
	private User user;

	public UserSocial() {
	}

	public UserSocial(UserSocialPK id) {
		this.id = id;
	}
	
	public UserSocialPK getId() {
		return this.id;
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public String getTokenSecret() {
		return tokenSecret;
	}
	
	public void setTokenSecret(String tokenSecret) {
		this.tokenSecret = tokenSecret;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Social getSocial() {
		return this.social;
	}

	public void setSocial(Social social) {
		this.social = social;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}