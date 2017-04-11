package com.ebay.park.service.user.dto.signin;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request to support a Facebook sign in.
 * @author scalderon
 * @since 2.0.2
 *
 */
public class FacebookSignInRequest extends SignInRequest {
	
	/** The fb token. */
	@JsonProperty(value = "fb_token")
	private String fbToken;

	/** The fb user id. */
	@JsonProperty(value = "fb_user_id")
	private String fbUserId;
	
	/** The email. */
	private String email;

	/**
	 * Gets the fb token.
	 *
	 * @return the fb token
	 */
	public String getFbToken() {
		return fbToken;
	}

	/**
	 * Sets the fb token.
	 *
	 * @param fbToken the new fb token
	 */
	public void setFbToken(String fbToken) {
		this.fbToken = fbToken;
	}

	/**
	 * Gets the fb user id.
	 *
	 * @return the fb user id
	 */
	public String getFbUserId() {
		return fbUserId;
	}

	/**
	 * Sets the fb user id.
	 *
	 * @param fbUserId the new fb user id
	 */
	public void setFbUserId(String fbUserId) {
		this.fbUserId = fbUserId;
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
        builder.append("FacebookSignInRequest [")
                .append("fbToken=")
                .append(fbToken)
                .append(", fbUserId=")
                .append(fbUserId)
                .append(", email=")
                .append(email)
                .append("]");
        return builder.toString();
    }

}
