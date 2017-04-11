
DELETE FROM user_notification
WHERE us_not_id in (SELECT not_id FROM notification_config where not_action = "USER_RATED" and not_type = "EMAIL");


DELETE FROM `notification_config` WHERE not_action = "USER_RATED" and not_type = "EMAIL";
