package com.ebay.park.service.admin.dto;

import com.ebay.park.service.ParkRequest;

public class AddModeratorRequest extends ParkRequest {

	private String username;

	private String email;

	private String name;

	private String lastname;

	private String password;

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AddModeratorRequest [username=")
			.append(this.username).append(", email=")
			.append(this.email).append(", name=")
			.append(this.name).append(", lastname=")
			.append(this.lastname).append("]");
		
		return builder.toString();
	}
}
