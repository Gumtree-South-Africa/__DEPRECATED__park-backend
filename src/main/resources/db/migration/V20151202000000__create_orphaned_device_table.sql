CREATE TABLE IF NOT EXISTS  `orphaned_device` (
	`orp_dev_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`orp_dev_unique_device_id` VARCHAR(200) NOT NULL,
	`orp_dev_device_id` VARCHAR(200) NULL DEFAULT NULL,
	`orp_dev_device_platform` VARCHAR(10) NULL DEFAULT NULL,
	 PRIMARY KEY (`orp_dev_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;