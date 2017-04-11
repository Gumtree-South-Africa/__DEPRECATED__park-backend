package com.ebay.park.service.user.dto.signup;

/**
 * Request for signup via Account Kit with email address.
 * @author Julieta Salvadó
 *
 */
public class AccountKitEmailSignUpRequest extends SignUpRequest {
    /** The email. */
    private String email;

    /** The account kit token. */
    private String accountKitToken;

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
        builder.append("AccountKitEmailSignUpRequest [email=")
                .append(email)
                .append(", accountKitToken=")
                .append(accountKitToken)
                .append(", device=")
                .append(device == null ? "null" : this.device.toString())
                .append(super.toString())
                .append("]");
        return builder.toString();
    }

    /**
     * Gets the email address.
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets de email address.
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
