INSERT INTO notification_config (not_id, not_type, not_action, not_template_name) VALUES (50, 'FEED', 'FEED_FROM_MODERATION', 'notification.msg.from_moderation');
INSERT INTO user_notification (us_use_id, us_not_id) (SELECT DISTINCT(us_use_id),50 FROM user_notification);