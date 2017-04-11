-- It creates the new notification types
INSERT INTO notification_config (`not_id`, `not_type`, `not_action`, `not_template_name`) VALUES (68, 'FEED', 'SOLD_AN_ITEM_FOR_INTERESTED_FOLLOWERS', 'notification.msg.sold_an_item');
INSERT INTO notification_config (`not_id`, `not_type`, `not_action`, `not_template_name`) VALUES (69, 'EMAIL', 'SOLD_AN_ITEM_FOR_INTERESTED_FOLLOWERS', 'sold-an-item-for-interested-follower.vm');
INSERT INTO notification_config (`not_id`, `not_type`, `not_action`, `not_template_name`) VALUES (70, 'PUSH', 'SOLD_AN_ITEM_FOR_INTERESTED_FOLLOWERS', 'notification.msg.sold_an_item');

-- It sets all user with the new notifications enabled
INSERT INTO user_notification (us_use_id, us_not_id)
SELECT u.use_id, 68
FROM user u;

INSERT INTO user_notification (us_use_id, us_not_id)
SELECT u.use_id, 69
FROM user u;

INSERT INTO user_notification (us_use_id, us_not_id)
SELECT u.use_id, 70
FROM user u;