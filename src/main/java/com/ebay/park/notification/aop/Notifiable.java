/*
 * Copyright eBay, 2014
 */
package com.ebay.park.notification.aop;

import com.ebay.park.notification.NotificationAction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Interface used to annotate methods with actions we want to notify.
 * @author jpizarro
 *
 */
@Target({ElementType.METHOD, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Notifiable {

	NotificationAction[] action();
	
}
