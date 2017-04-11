
INSERT IGNORE user_notification(us_use_id, us_not_id)

(SELECT
  use_id,
  not_id
FROM
  user, notification_config
where
	not_action = "ITEM_ABOUT_TO_EXPIRE")