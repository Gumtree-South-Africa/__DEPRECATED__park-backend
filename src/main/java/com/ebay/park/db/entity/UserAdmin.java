package com.ebay.park.db.entity;

import com.ebay.park.service.session.dto.UserSessionCache;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "user_admin")
public class UserAdmin extends AbstractEntity implements BasicUser, Serializable {

	private static final long serialVersionUID = -6065309734496277920L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "admin_id")
	private Long id;

	@Column(name = "admin_username")
	private String username;

	@Column(name = "admin_email")
	private String email;

	@Column(name = "admin_name")
	private String name;

	@Column(name = "admin_lastname")
	private String lastname;

	@Column(name = "admin_password")
	private byte[] password;

	@Column(name = "admin_token")
	private String token;

	@Enumerated(EnumType.STRING)
	@ElementCollection(targetClass = UserRole.class, fetch = FetchType.EAGER)
	@CollectionTable(name = "user_admin_role", joinColumns = @JoinColumn(name = "user_admin_id"))
	@Column(name = "role_name", nullable = false)
	private Set<UserRole> roles;

	@Override
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
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

	public byte[] getPassword() {
		return password;
	}

	public void setPassword(byte[] password) {
		this.password = password;
	}

	@Override
    public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Set<UserRole> getRoles() {
		return roles;
	}

	public void setRoles(Set<UserRole> roles) {
		this.roles = roles;
	}


	@Override
	public void populateSession(UserSessionCache userSessionCache) {
		userSessionCache.setUsername(this.getUsername());
		userSessionCache.setEmail(this.getEmail());
		userSessionCache.setUserId(this.getId());
		userSessionCache.setRoles(this.getRoles());
	}
}
