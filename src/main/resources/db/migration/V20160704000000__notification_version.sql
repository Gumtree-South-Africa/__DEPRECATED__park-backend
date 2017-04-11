-- new column for endpoint version
ALTER TABLE notification_config ADD COLUMN not_version bigint(20) NOT NULL DEFAULT 3;

-- not_id >= 68 belong to version 4
UPDATE notification_config 
SET not_version = 4
WHERE not_id >= 68;