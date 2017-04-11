/**
 * Copyright eBay, 2014
 */
package com.ebay.park.service.email;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author diana.gazquez
 *
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailVerificationRequired {

	
}
