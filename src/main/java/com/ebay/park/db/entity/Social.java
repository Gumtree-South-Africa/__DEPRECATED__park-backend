/*
 * Copyright eBay, 2014
 */
package com.ebay.park.db.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * The persistent class for the social database table.
 * 
 * @author juan.pizarro
 * 
 */
@Entity
@Table(name = "social")
public class Social extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String FACEBOOK = "facebook";
	public static final String TWITTER = "twitter";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "soc_id")
	private Long id;

	@Column(name = "soc_description")
	private String description;

	// bi-directional many-to-one association to UserSocial
	@OneToMany(mappedBy = "social")
	private List<UserSocial> userSocials;

	public Social() {
	}

	public long getSocialId() {
		return this.id;
	}

	public void setSocialId(long socialId) {
		this.id = socialId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<UserSocial> getUserSocials() {
		return this.userSocials;
	}

	public void setUserSocials(List<UserSocial> userSocials) {
		this.userSocials = userSocials;
	}

	public UserSocial addUserSocial(UserSocial userSocial) {
		getUserSocials().add(userSocial);
		userSocial.setSocial(this);

		return userSocial;
	}

	public UserSocial removeUserSocial(UserSocial userSocial) {
		getUserSocials().remove(userSocial);
		userSocial.setSocial(null);

		return userSocial;
	}

}