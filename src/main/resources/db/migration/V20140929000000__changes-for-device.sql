

-- kill all the sessions
UPDATE user
	set use_token = null;
	

-- Clear old device information
DELETE from device;

ALTER TABLE device
ADD COLUMN 
   `dv_token` VARCHAR(512) NULL DEFAULT NULL,
ADD COLUMN 
   `dv_last_successful_login` DATETIME NOT NULL;


