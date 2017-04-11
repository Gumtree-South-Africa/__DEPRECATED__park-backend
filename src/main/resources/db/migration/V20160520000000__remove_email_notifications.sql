DELETE from park.user_notification where us_use_id in (SELECT use_id FROM park.user where use_email_verified=0) and us_not_id in (1,2,3,5);
