-- -----------------------------------------------------
-- Default Super Admin
-- -----------------------------------------------------
INSERT IGNORE INTO user_admin (admin_username, admin_email, admin_password) VALUES ("moderator_admin","moderator@mail.com",0x438AD75C982AC2C0F2E28DFFEF88F7EF2B97623994873E645729826E41406377); 
-- Pass: V3RY_$TR0NG

SET @mod_id = (SELECT admin_id FROM user_admin WHERE admin_username="moderator_admin");
INSERT IGNORE INTO user_admin_role (user_admin_id,role_name) VALUES (@mod_id, "SUPER_ADMIN");
INSERT IGNORE INTO user_admin_role (user_admin_id,role_name) VALUES (@mod_id, "MODERATOR");


