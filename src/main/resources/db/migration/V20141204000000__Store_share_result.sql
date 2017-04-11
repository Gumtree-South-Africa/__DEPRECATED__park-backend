ALTER TABLE `park`.`item` 
ADD COLUMN `ite_face_share` VARCHAR(20) NOT NULL DEFAULT 'NON_REQUESTED' AFTER `ite_sold_expired_date`,
ADD COLUMN `ite_twitter_share` VARCHAR(20) NOT NULL DEFAULT 'NON_REQUESTED' AFTER `ite_face_share`;
