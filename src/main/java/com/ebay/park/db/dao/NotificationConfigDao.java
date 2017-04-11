package com.ebay.park.db.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ebay.park.db.entity.NotificationConfig;
import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.NotificationType;

/**
 * Repository interface for {@link NotificationConfig} objects.
 * 
 * @author lucia.masola
 */
public interface NotificationConfigDao extends CrudRepository<NotificationConfig, Long> {

	@Query(value = "select n from NotificationConfig n  where n.notificationType = :type "
												+ "and n.notificationAction = :action")
	public NotificationConfig findNotification(@Param("type") NotificationType type, 
										@Param("action") NotificationAction action);

	@Query(value = "select n from NotificationConfig n  where n.version <=:version")
	public List<NotificationConfig> findAll(@Param("version") Long version);
	
	@Query(value = "select n from NotificationConfig n  where n.notificationType != :notType")
	public Iterable<NotificationConfig> findAllExceptEmailNofitications(@Param("notType") NotificationType notType);
	
	@Query(value = "select n from NotificationConfig n  where n.notificationType = :notType")
	public Iterable<NotificationConfig> findAllEmailNotifications(@Param("notType") NotificationType notType);
	
}