
ALTER TABLE `group` 
	ADD COLUMN grp_creator_id  BIGINT(20)  NULL;

SET @userId = (SELECT use_id FROM user LIMIT 1);

UPDATE `group`
	SET grp_creator_id = @userId
	WHERE grp_creator_id IS NULL;
	
ALTER TABLE `park`.`group` 
	CHANGE COLUMN `grp_creator_id` `grp_creator_id` BIGINT(20) NOT NULL ;
