/*
 * Copyright eBay, 2014
 */
package com.ebay.park.notification.aop;

import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.NotificationDispatcher;
import com.ebay.park.notification.dto.NotifiableServiceResult;
import com.ebay.park.notification.dto.NotificationMessage;
import com.ebay.park.notification.factory.NotificationContext;
import com.ebay.park.notification.factory.NotificationFactory;
import com.ebay.park.service.notification.NotificationHelper;
import com.ebay.park.service.social.SocialService;
import com.ebay.park.service.user.finder.UserInfoUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Aspect in charge of handling notifications
 * @author jpizarro
 * 
 */
@Aspect
@Configuration
public class NotificationAdvice {

	private static final Logger logger = LogManager.getLogger(NotificationAdvice.class);
	
	@Autowired
	private NotificationDispatcher notificationDispatcher;
	
	@Autowired
	private UserInfoUtil userInfoUtil;
	
	@Autowired
	private NotificationHelper notificationHelper;
	
	@Autowired
	private SocialService socialService;

	/** 
	 * Method executed after the execution of methods annotated with {@link Notifiable} that returns a {@link NotifiableServiceResult} 
	 * @param joinPoint JoinPoint
	 * @param notifiable instance of Notifiable
	 * @param result a NotifiableServiceResult
	 */
	@AfterReturning(pointcut = "@annotation(notifiable))", returning = "result")
	public void allServiceMethodsPointcut(JoinPoint joinPoint, Notifiable notifiable, NotifiableServiceResult result) {
        logger.info("ENTERED POINTCUT = {} , result.toString:{}", notifiable.action()[0], result.toString());

		for (NotificationAction action : notifiable.action()) {
			try {
				dispatchNotification(result, action);
			} catch (Exception e) {
                logger.error("error trying to send a notification for action: {} NotificationResult: {}", action, result, e);
			}
		}
	}

	private void dispatchNotification(NotifiableServiceResult result, NotificationAction action) {
		NotificationContext context = new NotificationContext(result, action, userInfoUtil, notificationHelper, socialService);
		NotificationFactory notifFactory = action.notificationFactory(context);
		List<NotificationMessage> notificationMessages = notifFactory.createNotifications();
		notificationDispatcher.dispatch(notificationMessages);
        logger.info("Notifications for action {} were dispatched", action);
	}

}
