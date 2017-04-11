/*
 * Copyright eBay, 2014
 */
package com.ebay.park.notification;


import java.util.Arrays;

/**
 * Enumeration that represents the different ways in which the user will be notified.
 * @author jpizarro
 *
 */
public enum NotificationType {
	EMAIL("EMAIL", 1),
	PUSH("PUSH", 2),
	FEED("FEED", 0);
	
	private String value;
	private int order;
	
	NotificationType(String value, int order){
		this.value = value;
		this.order = order;
	}
	
	public static NotificationType fromValue(String value) {  
		   if (value != null) {  
		     for (NotificationType type : values()) {  
		       if (type.value.equals(value)) {  
		         return type;  
		       }  
		     }  
		   }  

		   return getDefault();   
		}  
			  
	public String toValue() {  
		return value;  
	}  
		
	public static NotificationType getDefault() {  
		return EMAIL;  
	}

	public int getOrder() {
		return order;
	}
}
