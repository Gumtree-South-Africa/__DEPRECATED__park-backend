
USE `park_stg` ;

ALTER TABLE item 
	DROP FOREIGN KEY `fk_item_status1`;

DROP TABLE IF EXISTS `status`;



-- ----------------------------------------------------------------
-- NOTIFICATIONS REFACTOR
-- ----------------------------------------------------------------

ALTER TABLE notification_config 
	ADD COLUMN not_type VARCHAR(45),
	ADD COLUMN not_action VARCHAR(45),
	ADD COLUMN not_template_name VARCHAR(200);

-- -----------------------------------------------------
-- Table `notification_template`
-- -----------------------------------------------------
UPDATE 	notification_config 
	SET not_template_name = (SELECT ntf_tem_description 
							 FROM ntf_config_temp join notification_template on (notification_template.ntf_tem_id = ntf_config_temp.ntf_conf_ntf_tem_id) 
							 WHERE ntf_conf_id = not_id);
	
-- -----------------------------------------------------
-- Table `notification_type`
-- -----------------------------------------------------

UPDATE 	notification_config 
	SET not_type = (SELECT not_type_description FROM notification_type WHERE not_not_type_id = not_type_id);

-- -----------------------------------------------------
-- Table `notification_action`
-- -----------------------------------------------------

UPDATE 	notification_config 
	SET not_action = (SELECT not_act_description FROM notification_action WHERE not_not_act_id = not_act_id);

	
	
ALTER TABLE `notification_config` 
	DROP FOREIGN KEY `fk_notification_type1`,
	DROP FOREIGN KEY `fk_notification_action1`;

ALTER TABLE `notification_config` 
	DROP COLUMN `not_not_act_id`,
	DROP COLUMN `not_not_type_id`,
	CHANGE COLUMN `not_type` `not_type` VARCHAR(45) NOT NULL ,
	CHANGE COLUMN `not_action` `not_action` VARCHAR(45) NOT NULL ,
	CHANGE COLUMN `not_template_name` `not_template_name` VARCHAR(200) NOT NULL ,
	DROP INDEX `fk_notification_action1` ,
	DROP INDEX `fk_notification_type1` ;

ALTER TABLE `ntf_config_temp` 
DROP FOREIGN KEY `fk_ntf_conf_tem_id1`;

ALTER TABLE `ntf_config_temp` 
	DROP COLUMN `ntf_conf_ntf_tem_id`,
	DROP INDEX `fk_ntf_conf_tem_id1` ;
	
DROP TABLE IF EXISTS `notification_action` ;
DROP TABLE IF EXISTS `notification_type` ;
DROP TABLE IF EXISTS `notification_template` ;

-- -------------------------------------------------------------------
ALTER TABLE `feed` 
  ADD COLUMN `feed_not_conf_id` BIGINT(20);
  
UPDATE 	feed 
	SET feed_not_conf_id = (SELECT ntf_conf_id FROM ntf_config_temp WHERE ntf_conf_temp_id=feed_not_conf_temp_id);  

	
ALTER TABLE `feed` 
	DROP FOREIGN KEY `fk_feed_act_id1`,
	DROP INDEX `fk_feed_act_id1` ,
	DROP COLUMN `feed_not_conf_temp_id`,
 	CHANGE COLUMN   `feed_not_conf_id` `feed_not_conf_id` BIGINT(20) NOT NULL,
	ADD   CONSTRAINT `fk_feed_config_id1` FOREIGN KEY (`feed_not_conf_id`)
        REFERENCES `notification_config` (`not_id`)
        ON DELETE NO ACTION ON UPDATE NO ACTION;
        
DROP TABLE IF EXISTS `ntf_config_temp` ;