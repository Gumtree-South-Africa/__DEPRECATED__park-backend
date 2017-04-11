DELETE FROM user_notification WHERE us_not_id = 42;
DELETE FROM feed_properties WHERE fd_prop_feed_id IN (SELECT feed_id FROM feed WHERE feed_not_conf_id = 42);
DELETE FROM feed WHERE feed_not_conf_id = 42;
DELETE FROM notification_config WHERE not_id = 42;