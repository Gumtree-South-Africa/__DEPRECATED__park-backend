/*
 * Copyright eBay, 2014
 */
package com.ebay.park.notification;

import com.ebay.park.notification.dto.FeedNotificationMessage;
import com.ebay.park.notification.dto.FeedNotificationMessage.FeedNotificationBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author lucia.masola
 * 
 */
public class FeedNotificationMessageTest {

	@Test
	public void testFeedNofificationBuilderSuccessfully(){
		// give		
		final String EXTRA_PROP = "extraProp";

		NotificationAction expectedNotificationAction = NotificationAction.USER_BLOCKED;
		NotificationType expectedNotificationType = NotificationType.EMAIL;
		String expectedFeedProp = "feedProp";
		Long expectedUser = 1l;
		String expectedExtraProp = "extraPropValue";
		
		// when
		//@formatter:off
		FeedNotificationMessage actualFeedNot
			= new FeedNotificationBuilder(expectedNotificationAction, 
										expectedNotificationType, 
										expectedFeedProp,  
										expectedUser)
			.append(EXTRA_PROP, expectedExtraProp)
			.build();
		//@formatter:on

		// then
		assertNotNull("actual notification must not be null", actualFeedNot);
		assertEquals(expectedFeedProp, actualFeedNot.getFeedProperties());
		assertEquals(expectedUser, actualFeedNot.getUserToNotify());
		assertEquals(expectedExtraProp, actualFeedNot.get(EXTRA_PROP));
		assertEquals(expectedNotificationType, actualFeedNot.getType());
		assertEquals(expectedNotificationAction, actualFeedNot.getAction());
	}
	
	
	
}
