package com.ebay.park.util;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * POJO to retrieve Account Kit access token information.
 * Example:
 * {  
   "id":"12345",
   "email":{  
      "address":"nicolas.porpiglia\u0040gmail.com"
   }
  }
 * 
 * @author julieta.salvadó
 * @since 2.0.2
 *
 */
public class AccountKitEmail implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
    
    /** The id. */
    private Long id;

    /** The email. */
    private Email email;
    
    /**
     * Gets the id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }
    
    /**
     * Sets the id.
     *
     * @param id the new id
     */
    public void setId(Long id) {
        this.id = id;
    }

	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	public Email getEmail() {
		return email;
	}

	/**
	 * Sets the email.
	 *
	 * @param email the new email
	 */
	public void setEmail(Email email) {
		this.email = email;
	}
    
}
