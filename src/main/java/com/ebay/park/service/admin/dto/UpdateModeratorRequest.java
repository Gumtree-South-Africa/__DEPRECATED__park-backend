package com.ebay.park.service.admin.dto;

import com.ebay.park.service.ParkRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class UpdateModeratorRequest extends ParkRequest {

	@JsonIgnore
	private Long id;

	private String username;

	private String password;

	private String email;

	private String name;

	private String lastname;

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UpdateModeratorRequest [username=")
			.append(this.username).append(", email=")
			.append(this.email).append(", name=")
			.append(this.name).append(", lastname=")
			.append(this.lastname).append("]");
		
		return builder.toString();
	}
}
