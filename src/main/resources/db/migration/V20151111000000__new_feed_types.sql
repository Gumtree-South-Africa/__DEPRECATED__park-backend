-- New feed types
INSERT INTO notification_config (`not_id`, `not_type`, `not_action`, `not_template_name`) VALUES (59, 'FEED', 'ITEM_DELETED_FROM_MODERATION_DUPLICATED', 'notification.msg.item.rejected_duplicated');
INSERT INTO notification_config (`not_id`, `not_type`, `not_action`, `not_template_name`) VALUES (60, 'FEED', 'ITEM_DELETED_FROM_MODERATION_PICTURES', 'notification.msg.item.rejected_pictures');
INSERT INTO notification_config (`not_id`, `not_type`, `not_action`, `not_template_name`) VALUES (61, 'FEED', 'ITEM_DELETED_FROM_MODERATION_SERVICES', 'notification.msg.item.rejected_services');
INSERT INTO notification_config (`not_id`, `not_type`, `not_action`, `not_template_name`) VALUES (62, 'FEED', 'ITEM_DELETED_FROM_MODERATION_MAKEUP', 'notification.msg.item.rejected_makeup');
INSERT INTO notification_config (`not_id`, `not_type`, `not_action`, `not_template_name`) VALUES (63, 'FEED', 'ITEM_DELETED_FROM_MODERATION_ANIMALS', 'notification.msg.item.rejected_animals');
INSERT INTO notification_config (`not_id`, `not_type`, `not_action`, `not_template_name`) VALUES (64, 'FEED', 'ITEM_DELETED_FROM_MODERATION_COMMISSION', 'notification.msg.item.rejected_commission');
INSERT INTO notification_config (`not_id`, `not_type`, `not_action`, `not_template_name`) VALUES (65, 'FEED', 'ITEM_DELETED_FROM_MODERATION_STYLE', 'notification.msg.item.rejected_style');
INSERT INTO notification_config (`not_id`, `not_type`, `not_action`, `not_template_name`) VALUES (66, 'FEED', 'ITEM_DELETED_FROM_MODERATION_PRICE', 'notification.msg.item.rejected_price');
INSERT INTO notification_config (`not_id`, `not_type`, `not_action`, `not_template_name`) VALUES (67, 'FEED', 'ITEM_DELETED_FROM_MODERATION_FORBIDDEN', 'notification.msg.item.rejected_forbidden');

INSERT INTO user_notification (us_use_id, us_not_id)
SELECT u.use_id, 59
FROM user u;

INSERT INTO user_notification (us_use_id, us_not_id)
SELECT u.use_id, 60
FROM user u;

INSERT INTO user_notification (us_use_id, us_not_id)
SELECT u.use_id, 61
FROM user u;

INSERT INTO user_notification (us_use_id, us_not_id)
SELECT u.use_id, 62
FROM user u;

INSERT INTO user_notification (us_use_id, us_not_id)
SELECT u.use_id, 63
FROM user u;

INSERT INTO user_notification (us_use_id, us_not_id)
SELECT u.use_id, 64
FROM user u;

INSERT INTO user_notification (us_use_id, us_not_id)
SELECT u.use_id, 65
FROM user u;

INSERT INTO user_notification (us_use_id, us_not_id)
SELECT u.use_id, 66
FROM user u;

INSERT INTO user_notification (us_use_id, us_not_id)
SELECT u.use_id, 67
FROM user u;