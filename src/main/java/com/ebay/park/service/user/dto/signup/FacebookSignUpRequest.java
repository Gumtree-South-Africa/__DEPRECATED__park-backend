package com.ebay.park.service.user.dto.signup;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request to support a facebook sign up.
 * @author scalderon
 * @since 2.0.2
 *
 */
public class FacebookSignUpRequest extends SignUpRequest {

	/** The facebook user id. */
	@JsonProperty(value = "fb_user_id")
	private String facebookUserId;

	/** The facebook token. */
	@JsonProperty(value = "fb_token")
	private String facebookToken;
	
	/** The email. */
	private String email;
	

	/**
	 * Gets the facebook user id.
	 *
	 * @return the facebook user id
	 */
	public String getFacebookUserId() {
		return facebookUserId;
	}

	/**
	 * Sets the facebook user id.
	 *
	 * @param facebookUserId the new facebook user id
	 */
	public void setFacebookUserId(String facebookUserId) {
		this.facebookUserId = facebookUserId;
	}

	/**
	 * Gets the facebook token.
	 *
	 * @return the facebook token
	 */
	public String getFacebookToken() {
		return facebookToken;
	}

	/**
	 * Sets the facebook token.
	 *
	 * @param facebookToken the new facebook token
	 */
	public void setFacebookToken(String facebookToken) {
		this.facebookToken = facebookToken;
	}

	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email.
	 *
	 * @param email the new email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("FacebookSignUpRequest [")
                .append("facebookUserId=")
                .append(facebookUserId)
                .append(", facebookToken=")
                .append(facebookToken)
                .append(", email=")
                .append(email)
                .append("]");
        return builder.toString();
    }
}
