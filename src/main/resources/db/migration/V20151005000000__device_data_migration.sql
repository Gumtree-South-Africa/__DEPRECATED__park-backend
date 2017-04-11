-- Delete of multiple sessions on the same device
DELETE FROM device WHERE dv_id IN (SELECT dv_id FROM (
	select d.dv_id, d.dv_last_successful_login 
	from device d having d.dv_last_successful_login < (
		select max(x.dv_last_successful_login) 
		from device x
		where x.dv_device_id= d.dv_device_id and x.dv_description = d.dv_description)) 
AS t);

-- Migration of logged user sessions
INSERT INTO user_session (use_id, use_ses_token, use_ses_last_successful_login, use_ses_active)
	SELECT d.dv_use_id, u.use_token, d.dv_last_successful_login, TRUE
	FROM device d
	JOIN user u ON (d.dv_use_id = u.use_id)
	WHERE u.use_token IS NOT NULL;

-- Migration of not-logged user sessions
INSERT INTO user_session (use_id, use_ses_token, use_ses_last_successful_login, use_ses_active)
	SELECT d.dv_use_id, u.use_token, d.dv_last_successful_login, FALSE
	FROM device d
	JOIN user u ON (d.dv_use_id = u.use_id)
	WHERE u.use_token IS NULL;

-- Migration of devices
INSERT INTO session_device (dev_session_id, dev_device_id, dev_platform)
	SELECT u.use_ses_id, d.dv_device_id, d.dv_description
	FROM device d
	JOIN user_session u ON ( u.use_id = d.dv_use_id);