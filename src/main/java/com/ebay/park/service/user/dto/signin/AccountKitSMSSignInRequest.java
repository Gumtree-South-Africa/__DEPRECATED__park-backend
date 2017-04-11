package com.ebay.park.service.user.dto.signin;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Request to support SignIn without password by Account Kit SMS 
 * with mobile phone number.
 * @author scalderon
 * @since 2.0.2
 */
public class AccountKitSMSSignInRequest extends SignInRequest {
	
	
	/** The user mobile phone. */
	private String mobilePhone;
	
	/** The account kit access token. */
	private String accountKitToken;

	/**
	 * Instantiates a new phone number sign in request.
	 */
	public AccountKitSMSSignInRequest() {
	}

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
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
	
	@Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        //@formatter:off
        builder.append("AccountKitSMSSignInRequest [mobilePhone=")
               .append(mobilePhone)
               .append(", accountKitToken=")
               .append(accountKitToken);
                
        if (device != null) {
        	builder.append(", device=")
        		   .append(device.toString());
        }
        builder.append("]");
        //@formatter:on
        return builder.toString();
	}
}
