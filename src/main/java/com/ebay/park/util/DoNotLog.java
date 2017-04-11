package com.ebay.park.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/**
 * 
 * @author diana.gazquez
 *
 * Annotation to indicate that a parameter from a call should not be logged.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DoNotLog {

}
