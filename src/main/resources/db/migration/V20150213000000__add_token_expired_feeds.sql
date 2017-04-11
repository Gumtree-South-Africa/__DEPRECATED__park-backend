INSERT INTO notification_config (not_id, not_type, not_action, not_template_name) VALUES (48, 'FEED', 'TW_TOKEN_EXPIRED', 'notification.msg.tw_token_expired');
INSERT INTO notification_config (not_id, not_type, not_action, not_template_name) VALUES (49, 'FEED', 'FB_TOKEN_EXPIRED', 'notification.msg.fb_token_expired');
insert into user_notification (us_use_id, us_not_id) (select distinct(us_use_id),48 from user_notification);
insert into user_notification (us_use_id, us_not_id) (select distinct(us_use_id),49 from user_notification);