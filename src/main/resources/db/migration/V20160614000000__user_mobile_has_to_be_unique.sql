UPDATE user SET use_mobile = NULL where use_mobile = '';

ALTER TABLE user
ADD UNIQUE (use_mobile);