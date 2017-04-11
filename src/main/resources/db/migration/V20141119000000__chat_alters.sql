ALTER TABLE `park`.`chat` 
CHANGE COLUMN `cha_comment` `cha_comment` VARCHAR(512) NULL ;

UPDATE `park`.`chat` set `cha_comment` = NULL
WHERE cha_action = "ACCEPTED" or cha_action = "CANCELLED";

ALTER TABLE `park`.`chat` 
ADD COLUMN `cha_auto` bit(1) DEFAULT b'0';


