-- it activates email notification for those users created after AK deployment that already verified his email
INSERT INTO user_notification 
            (us_use_id, 
             us_not_id)
SELECT use_id,
       not_id
FROM   user,
       notification_config 
WHERE  use_id NOT IN(SELECT us_use_id 
                     FROM   user_notification, 
                            notification_config 
                     WHERE  us_not_id = not_id
                            AND not_type = "email")
       AND use_email_verified IS TRUE
       AND use_creation >= "2016-06-27"
       AND not_id IN (SELECT not_id
                      FROM   notification_config 
                      WHERE  not_type = "email"); 