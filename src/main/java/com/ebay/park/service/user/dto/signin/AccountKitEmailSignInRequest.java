package com.ebay.park.service.user.dto.signin;

/**
 * Request to support SignIn without password by Account Kit 
 * with user email.
 * @author scalderon
 * @since 2.0.2
 */
public class AccountKitEmailSignInRequest extends SignInRequest {
	
	/** The email. */
	private String email;
	
	/** The account kit access token. */
	private String accountKitToken;

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

	/**
	 * Gets the account kit token.
	 *
	 * @return the account kit token
	 */
	public String getAccountKitToken() {
		return accountKitToken;
	}

	/**
	 * Sets the account kit token.
	 *
	 * @param accountKitToken the new account kit token
	 */
	public void setAccountKitToken(String accountKitToken) {
		this.accountKitToken = accountKitToken;
	}

	@Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("AccountKitEmailSignInRequest [email=")
                .append(email)
                .append(", accountKitToken=")
                .append(accountKitToken)
                .append(", device=")
                .append(device == null ? "null" : this.device.toString())
                .append("]");
        return builder.toString();
    }

}
