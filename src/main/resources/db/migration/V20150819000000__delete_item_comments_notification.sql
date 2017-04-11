-- Deletes old feed notifications about item's comment
DELETE FROM feed_properties WHERE fd_prop_feed_id IN (SELECT feed_id FROM feed WHERE feed_not_conf_id IN (41,17,7,31,14));
DELETE FROM feed WHERE feed_not_conf_id IN (41,17,7,31,14);

-- Deletes Notification config
DELETE FROM user_notification WHERE us_not_id IN (41,17,7,31,14);
DELETE FROM notification_config WHERE not_id IN (41,17,7,31,14);

-- Drop table comment
DROP TABLE comment;