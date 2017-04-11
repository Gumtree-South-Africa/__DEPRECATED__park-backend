CREATE TABLE `user_session` (
  `use_ses_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `use_id` bigint(20) DEFAULT NULL,
  `use_ses_token` varchar(512) DEFAULT NULL,
  `use_ses_last_successful_login` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `use_ses_active` bit(1) DEFAULT b'1',
  PRIMARY KEY (`use_ses_id`),
  KEY `use_ses_token_idx` (`use_ses_token`(255))
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;


CREATE TABLE `session_device` (
  `dev_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dev_session_id` bigint(20) DEFAULT NULL,
  `dev_device_id` varchar(200) NOT NULL,
  `dev_platform` varchar(10) NOT NULL,
  PRIMARY KEY (`dev_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;
