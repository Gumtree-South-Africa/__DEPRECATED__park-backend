ALTER TABLE user_session MODIFY use_ses_token VARCHAR(255);
ALTER TABLE user_session ADD UNIQUE (use_ses_token);