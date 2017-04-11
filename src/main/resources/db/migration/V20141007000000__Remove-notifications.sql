

DELETE FROM user_notification
WHERE us_not_id in (SELECT not_id FROM notification_config where not_action = "UNFOLLOW_USER");

DELETE FROM feed_properties
WHERE fd_prop_feed_id in 
	(SELECT feed_id 
	FROM notification_config join feed  on (feed_not_conf_id = not_id) 
	where not_action = "UNFOLLOW_USER");
	
DELETE FROM feed
WHERE feed_not_conf_id in (SELECT not_id FROM notification_config where not_action = "UNFOLLOW_USER");


DELETE FROM `notification_config` WHERE not_action = "UNFOLLOW_USER";

-- ------------------------------------------------------------------------------------------------- 

DELETE FROM user_notification
WHERE us_not_id in (SELECT not_id FROM notification_config where not_action = "FOLLOW_ITEM");

DELETE FROM feed_properties
WHERE fd_prop_feed_id in 
	(SELECT feed_id 
	FROM notification_config join feed  on (feed_not_conf_id = not_id) 
	where not_action = "FOLLOW_ITEM");
	
DELETE FROM feed
WHERE feed_not_conf_id in (SELECT not_id FROM notification_config where not_action = "FOLLOW_ITEM");

DELETE FROM `notification_config` WHERE not_action = "FOLLOW_ITEM";


-- ------------------------------------------------------------------------------------------------- 


DELETE FROM user_notification
WHERE us_not_id in (SELECT not_id FROM notification_config where not_action = "EMAIL_NOT_VERIFIED");

DELETE FROM feed_properties
WHERE fd_prop_feed_id in 
	(SELECT feed_id 
	FROM notification_config join feed  on (feed_not_conf_id = not_id) 
	where not_action = "EMAIL_NOT_VERIFIED");

DELETE FROM feed
WHERE feed_not_conf_id in (SELECT not_id FROM notification_config where not_action = "EMAIL_NOT_VERIFIED");

DELETE FROM `notification_config` WHERE not_action = "EMAIL_NOT_VERIFIED";