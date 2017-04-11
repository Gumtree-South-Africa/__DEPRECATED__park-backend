-- It creates the new notifications type
INSERT INTO notification_config (`not_id`, `not_type`, `not_action`, `not_template_name`, `not_version`) VALUES (71, 'FEED', 'FB_FRIEND_USING_THE_APP', 'notification.msg.fb_friend_using_the_app', 5);
INSERT INTO notification_config (`not_id`, `not_type`, `not_action`, `not_template_name`, `not_version`) VALUES (72, 'PUSH', 'FB_FRIEND_USING_THE_APP', 'notification.msg.fb_friend_using_the_app', 5);


-- It sets all user with the new notifications enabled
INSERT INTO user_notification (us_use_id, us_not_id)
SELECT u.use_id, 71
FROM user u;

INSERT INTO user_notification (us_use_id, us_not_id)
SELECT u.use_id, 72
FROM user u;