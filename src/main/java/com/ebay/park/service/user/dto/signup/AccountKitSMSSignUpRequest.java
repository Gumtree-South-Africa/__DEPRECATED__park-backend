package com.ebay.park.service.user.dto.signup;

/**
 * Request to support Phone Number SMS Account kit registration.
 * @author scalderon
 * @since 2.0.2
 *
 */
public class AccountKitSMSSignUpRequest extends SignUpRequest{

	/** The mobile phone. */
	private String mobilePhone;
	
	/** The account kit token. */
	private String accountKitToken;
	
	/**
	 * Gets the mobile phone.
	 *
	 * @return the mobile phone
	 */
	public String getMobilePhone() {
		return mobilePhone;
	}
	
	/**
	 * Sets the mobile phone.
	 *
	 * @param mobilePhone the new mobile phone
	 */
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
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
        builder.append("AccountKitSMSSignUpRequest [mobilePhone=")
                .append(mobilePhone)
                .append(", accountKitToken=")
                .append(accountKitToken)
                .append(", device=")
                .append(this.device == null ? "null" : this.device.toString())
                .append("]");
        return builder.toString();
    }
}
