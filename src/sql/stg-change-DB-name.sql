SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema park
-- -----------------------------------------------------

DROP SCHEMA IF EXISTS `park` ;
CREATE SCHEMA IF NOT EXISTS `park` DEFAULT CHARACTER SET utf8 ;


RENAME TABLE park_stg.banner TO `park`.banner;
RENAME TABLE park_stg.black_list TO `park`.black_list;
RENAME TABLE park_stg.category TO `park`.category;
RENAME TABLE park_stg.chat TO `park`.chat;
RENAME TABLE park_stg.city TO `park`.city;
RENAME TABLE park_stg.comment TO `park`.comment;
RENAME TABLE park_stg.conversation TO `park`.conversation;
RENAME TABLE park_stg.country TO `park`.country;
RENAME TABLE park_stg.currency TO `park`.currency;
RENAME TABLE park_stg.device TO `park`.device;
RENAME TABLE park_stg.feed TO `park`.feed;
RENAME TABLE park_stg.feed_properties TO `park`.feed_properties;
RENAME TABLE park_stg.follower TO `park`.follower;
RENAME TABLE park_stg.`group` TO `park`.`group`;
RENAME TABLE park_stg.idiom TO `park`.idiom;
RENAME TABLE park_stg.idiom_category TO `park`.idiom_category;
RENAME TABLE park_stg.item TO `park`.item;
RENAME TABLE park_stg.item_group TO `park`.item_group;
RENAME TABLE park_stg.item_picture TO `park`.item_picture;
RENAME TABLE park_stg.notification_config TO `park`.notification_config;
RENAME TABLE park_stg.notmobile TO `park`.notmobile;
RENAME TABLE park_stg.picture TO `park`.picture;
RENAME TABLE park_stg.rating TO `park`.rating;
RENAME TABLE park_stg.role TO `park`.role;
RENAME TABLE park_stg.social TO `park`.social;
RENAME TABLE park_stg.state TO `park`.state;
RENAME TABLE park_stg.tutorial TO `park`.tutorial;
RENAME TABLE park_stg.`user` TO `park`.`user`;
RENAME TABLE park_stg.user_admin TO `park`.user_admin;
RENAME TABLE park_stg.user_admin_role TO `park`.user_admin_role;
RENAME TABLE park_stg.user_group TO `park`.user_group;
RENAME TABLE park_stg.user_itemliked TO `park`.user_itemliked;
RENAME TABLE park_stg.user_itemreport TO `park`.user_itemreport;
RENAME TABLE park_stg.user_notification TO `park`.user_notification;
RENAME TABLE park_stg.user_social TO `park`.user_social;
RENAME TABLE park_stg.status TO `park`.status;
RENAME TABLE park_stg.notification_action TO `park`.notification_action;
RENAME TABLE park_stg.notification_type TO `park`.notification_type;
RENAME TABLE park_stg.notification_template TO `park`.notification_template;

















