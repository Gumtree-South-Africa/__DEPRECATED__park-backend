-- It creates the new email notification type
INSERT INTO notification_config (`not_id`, `not_type`, `not_action`, `not_template_name`) VALUES (73, 'EMAIL', 'FB_FRIEND_USING_THE_APP', 'fb_friend-using-the-app.vm');

-- It sets all user with the new notifications enabled
INSERT INTO user_notification (us_use_id, us_not_id)
SELECT u.use_id, 73
FROM user u;
