-- MySQL dump 10.13  Distrib 5.6.17-65.0, for Linux (x86_64)
--
-- Host: localhost    Database: park
-- ------------------------------------------------------
-- Server version       5.6.17-65.0-56-log

DATABASE park;

CREATE DATABASE park;

USE park;

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `banner`
--

DROP TABLE IF EXISTS `banner`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `banner` (
  `banner_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `banner_priority` varchar(45) NOT NULL,
  `banner_type` varchar(45) NOT NULL,
  `banner_msg` varchar(255) NOT NULL,
  `banner_idi_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`banner_id`),
  KEY `fk_banner_idiom1` (`banner_idi_id`),
  CONSTRAINT `fk_banner_idiom1` FOREIGN KEY (`banner_idi_id`) REFERENCES `idiom` (`idi_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `black_list`
--

DROP TABLE IF EXISTS `black_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `black_list` (
  `bk_list_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bk_list_description` varchar(200) NOT NULL,
  PRIMARY KEY (`bk_list_id`)
) ENGINE=InnoDB AUTO_INCREMENT=137 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category` (
  `cat_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cat_key` varchar(45) NOT NULL COMMENT 'Name designed for the categoy. This is not the label',
  `cat_picture` varchar(512) NOT NULL,
  `cat_web_color` char(6) DEFAULT NULL,
  PRIMARY KEY (`cat_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `chat`
--

DROP TABLE IF EXISTS `chat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chat` (
  `cha_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cha_ite_id` bigint(20) NOT NULL,
  `cha_sender_id` bigint(20) NOT NULL,
  `cha_receiver_id` bigint(20) NOT NULL,
  `cha_comment` varchar(512) DEFAULT NULL,
  `cha_posttime` datetime NOT NULL,
  `cha_brandpublish` varchar(45) DEFAULT NULL,
  `cha_versionpublish` varchar(45) DEFAULT NULL,
  `cha_offered_price` decimal(10,2) DEFAULT NULL,
  `cha_conversation_id` bigint(20) NOT NULL,
  `cha_action` varchar(20) NOT NULL,
  `cha_auto` bit(1) DEFAULT b'0',
  PRIMARY KEY (`cha_id`),
  KEY `fk_chat_item1_idx` (`cha_ite_id`),
  KEY `fk_chat_sender_idx` (`cha_sender_id`),
  KEY `fk_chat_receiver_idx` (`cha_receiver_id`),
  KEY `fk_chat_conversation_idx` (`cha_conversation_id`),
  CONSTRAINT `fk_chat_conversation` FOREIGN KEY (`cha_conversation_id`) REFERENCES `conversation` (`con_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_chat_item1` FOREIGN KEY (`cha_ite_id`) REFERENCES `item` (`ite_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_chat_receiver` FOREIGN KEY (`cha_receiver_id`) REFERENCES `user` (`use_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_chat_sender` FOREIGN KEY (`cha_sender_id`) REFERENCES `user` (`use_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1163 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `city`
--

DROP TABLE IF EXISTS `city`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `city` (
  `cit_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cit_sta_id` bigint(20) NOT NULL,
  `cit_description` varchar(45) NOT NULL,
  PRIMARY KEY (`cit_id`),
  KEY `fk_city_state1_idx` (`cit_sta_id`),
  CONSTRAINT `fk_city_state1` FOREIGN KEY (`cit_sta_id`) REFERENCES `state` (`sta_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comment` (
  `com_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `com_ite_id` bigint(20) NOT NULL,
  `com_use_id` bigint(20) NOT NULL,
  `com_comment` varchar(512) NOT NULL COMMENT 'Content of public comment',
  `com_posttime` datetime NOT NULL,
  PRIMARY KEY (`com_id`),
  KEY `fk_item_comment_item1_idx` (`com_ite_id`),
  KEY `fk_comment_user1_idx` (`com_use_id`),
  CONSTRAINT `fk_comment_user1` FOREIGN KEY (`com_use_id`) REFERENCES `user` (`use_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_item_comment_item1` FOREIGN KEY (`com_ite_id`) REFERENCES `item` (`ite_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=530 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `conversation`
--

DROP TABLE IF EXISTS `conversation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `conversation` (
  `con_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `con_ite_id` bigint(20) NOT NULL,
  `con_buyer_id` bigint(20) NOT NULL,
  `con_seller_id` bigint(20) NOT NULL,
  `con_status` char(45) NOT NULL,
  `con_current_price_proposed_by_buyer` decimal(10,2) NOT NULL,
  `con_current_price_proposed_by_seller` decimal(10,2) NOT NULL,
  `con_buyer_bargain_status` varchar(45) DEFAULT NULL,
  `con_seller_bargain_status` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`con_id`),
  KEY `fk_conversation_item_idx` (`con_ite_id`),
  KEY `fk_conversation_buyer_idx` (`con_buyer_id`),
  KEY `fk_conversation_seller_idx` (`con_seller_id`),
  CONSTRAINT `fk_conversation_buyer` FOREIGN KEY (`con_buyer_id`) REFERENCES `user` (`use_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_conversation_item` FOREIGN KEY (`con_ite_id`) REFERENCES `item` (`ite_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_conversation_seller` FOREIGN KEY (`con_seller_id`) REFERENCES `user` (`use_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=551 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `country`
--

DROP TABLE IF EXISTS `country`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `country` (
  `cou_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cou_description` varchar(45) NOT NULL,
  `cou_cur_id` bigint(20) NOT NULL,
  PRIMARY KEY (`cou_id`),
  KEY `fk_country_currency1_idx` (`cou_cur_id`),
  CONSTRAINT `fk_country_currency1` FOREIGN KEY (`cou_cur_id`) REFERENCES `currency` (`cur_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `currency`
--

DROP TABLE IF EXISTS `currency`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `currency` (
  `cur_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cur_description` varchar(45) NOT NULL,
  PRIMARY KEY (`cur_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `device`
--

DROP TABLE IF EXISTS `device`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `device` (
  `dv_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dv_description` varchar(200) NOT NULL,
  `dv_device_id` varchar(200) NOT NULL,
  `dv_creation_date` datetime DEFAULT NULL,
  `dv_use_id` bigint(20) NOT NULL,
  `dv_token` varchar(512) DEFAULT NULL,
  `dv_last_successful_login` datetime NOT NULL,
  PRIMARY KEY (`dv_id`),
  KEY `fk_device_id2` (`dv_use_id`),
  CONSTRAINT `fk_device_id2` FOREIGN KEY (`dv_use_id`) REFERENCES `user` (`use_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=573 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `feed`
--

DROP TABLE IF EXISTS `feed`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `feed` (
  `feed_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `feed_owner_id` bigint(20) NOT NULL,
  `feed_usr_id` bigint(20) DEFAULT NULL,
  `feed_item_id` bigint(20) DEFAULT NULL,
  `feed_creation_date` datetime NOT NULL,
  `feed_read` bit(1) DEFAULT b'0',
  `feed_not_conf_id` bigint(20) NOT NULL,
  PRIMARY KEY (`feed_id`),
  KEY `fk_feed_usr_id2` (`feed_owner_id`),
  KEY `fk_feed_usr_id1` (`feed_usr_id`),
  KEY `fk_feed_item_id1` (`feed_item_id`),
  KEY `fk_feed_config_id1` (`feed_not_conf_id`),
  CONSTRAINT `fk_feed_config_id1` FOREIGN KEY (`feed_not_conf_id`) REFERENCES `notification_config` (`not_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_feed_item_id1` FOREIGN KEY (`feed_item_id`) REFERENCES `item` (`ite_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_feed_usr_id1` FOREIGN KEY (`feed_usr_id`) REFERENCES `user` (`use_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_feed_usr_id2` FOREIGN KEY (`feed_owner_id`) REFERENCES `user` (`use_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=7646 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `feed_properties`
--

DROP TABLE IF EXISTS `feed_properties`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `feed_properties` (
  `fd_prop_feed_id` bigint(20) NOT NULL,
  `fd_prop_key` varchar(200) NOT NULL,
  `fd_prop_value` varchar(200) NOT NULL,
  KEY `fk_feed__id1` (`fd_prop_feed_id`),
  CONSTRAINT `fk_feed__id1` FOREIGN KEY (`fd_prop_feed_id`) REFERENCES `feed` (`feed_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `follower`
--

DROP TABLE IF EXISTS `follower`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `follower` (
  `fol_id` bigint(20) NOT NULL,
  `fol_usu_id` bigint(20) NOT NULL,
  PRIMARY KEY (`fol_id`,`fol_usu_id`),
  KEY `fk_follower_user1_idx` (`fol_usu_id`),
  CONSTRAINT `fk_follower_user1` FOREIGN KEY (`fol_usu_id`) REFERENCES `user` (`use_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `group`
--

DROP TABLE IF EXISTS `group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `group` (
  `grp_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `grp_name` varchar(45) NOT NULL,
  `grp_lat` float DEFAULT NULL,
  `grp_long` float DEFAULT NULL,
  `grp_location_name` varchar(128) DEFAULT NULL,
  `grp_picture` varchar(512) DEFAULT NULL,
  `grp_creation` datetime NOT NULL,
  `grp_last_update` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `grp_creator_id` bigint(20) NOT NULL,
  `grp_zip_code` varchar(45) DEFAULT NULL,
  `grp_description` varchar(300) DEFAULT NULL,
  PRIMARY KEY (`grp_id`)
) ENGINE=InnoDB AUTO_INCREMENT=173 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `idiom`
--

DROP TABLE IF EXISTS `idiom`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `idiom` (
  `idi_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `idi_code` varchar(45) NOT NULL,
  PRIMARY KEY (`idi_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `idiom_category`
--

DROP TABLE IF EXISTS `idiom_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `idiom_category` (
  `ic_idi_id` bigint(20) NOT NULL,
  `ic_cat_id` bigint(20) NOT NULL,
  `ic_lblname` varchar(45) NOT NULL COMMENT 'Label for the category name, depending on the idiom fk',
  `ic_lbldescription` varchar(45) NOT NULL COMMENT 'Label for the category description, depending on the idiom fk',
  PRIMARY KEY (`ic_idi_id`,`ic_cat_id`),
  KEY `fk_idiom_category_category1_idx` (`ic_cat_id`),
  KEY `fk_idiom_category_idiom1_idx` (`ic_idi_id`),
  CONSTRAINT `fk_idiom_category_category1` FOREIGN KEY (`ic_cat_id`) REFERENCES `category` (`cat_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_idiom_category_idiom1` FOREIGN KEY (`ic_idi_id`) REFERENCES `idiom` (`idi_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `item`
--

DROP TABLE IF EXISTS `item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `item` (
  `ite_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ite_stu_id` bigint(20) NOT NULL,
  `ite_cat_id` bigint(20) NOT NULL,
  `ite_name` varchar(45) NOT NULL,
  `ite_description` varchar(255) DEFAULT NULL,
  `ite_price` decimal(10,2) NOT NULL,
  `ite_location` varchar(512) DEFAULT NULL,
  `ite_location_name` varchar(128) DEFAULT NULL,
  `ite_brandpublish` varchar(45) NOT NULL,
  `ite_versionpublish` varchar(45) NOT NULL,
  `ite_lat` float DEFAULT NULL COMMENT 'Latitude value taken from FourSquare',
  `ite_long` float DEFAULT NULL COMMENT 'Long value taken from FourSquare',
  `ite_picture1Url` varchar(255) DEFAULT NULL,
  `ite_picture2Url` varchar(255) DEFAULT NULL,
  `ite_picture3Url` varchar(255) DEFAULT NULL,
  `ite_picture4Url` varchar(255) DEFAULT NULL,
  `ite_publisher_id` bigint(20) DEFAULT NULL,
  `ite_created` datetime DEFAULT NULL,
  `ite_published` datetime DEFAULT NULL,
  `ite_count_reports` int(11) DEFAULT '0',
  `ite_last_update` datetime DEFAULT NULL,
  `ite_deleted` bit(1) NOT NULL DEFAULT b'0',
  `ite_sold_expired_date` datetime DEFAULT NULL,
  `ite_face_share` varchar(20) NOT NULL DEFAULT 'NON_REQUESTED',
  `ite_twitter_share` varchar(20) NOT NULL DEFAULT 'NON_REQUESTED',
  PRIMARY KEY (`ite_id`),
  KEY `fk_item_category1_idx` (`ite_cat_id`),
  KEY `fk_item_status1_idx` (`ite_stu_id`),
  KEY `fk_item_user_publisher` (`ite_publisher_id`),
  CONSTRAINT `fk_item_category1` FOREIGN KEY (`ite_cat_id`) REFERENCES `category` (`cat_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_item_user_publisher` FOREIGN KEY (`ite_publisher_id`) REFERENCES `user` (`use_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=571 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `item_group`
--

DROP TABLE IF EXISTS `item_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `item_group` (
  `ig_ite_id` bigint(20) NOT NULL,
  `ig_grp_id` bigint(20) NOT NULL,
  PRIMARY KEY (`ig_ite_id`,`ig_grp_id`),
  KEY `fk_item_group__group1` (`ig_grp_id`),
  CONSTRAINT `fk_item_group_item1` FOREIGN KEY (`ig_ite_id`) REFERENCES `item` (`ite_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_item_group__group1` FOREIGN KEY (`ig_grp_id`) REFERENCES `group` (`grp_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `item_picture`
--

DROP TABLE IF EXISTS `item_picture`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `item_picture` (
  `ip_item_id` bigint(20) NOT NULL,
  `ip_pic_id` bigint(20) NOT NULL,
  PRIMARY KEY (`ip_item_id`,`ip_pic_id`),
  KEY `fk_item_picture_item1_idx` (`ip_item_id`),
  KEY `fk_item_picture_picture1_idx` (`ip_pic_id`),
  CONSTRAINT `fk_item_picture_item1` FOREIGN KEY (`ip_item_id`) REFERENCES `item` (`ite_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_item_picture_picture1` FOREIGN KEY (`ip_pic_id`) REFERENCES `picture` (`pic_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `notification_config`
--

DROP TABLE IF EXISTS `notification_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notification_config` (
  `not_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `not_type` varchar(45) NOT NULL,
  `not_action` varchar(45) NOT NULL,
  `not_template_name` varchar(200) NOT NULL,
  PRIMARY KEY (`not_id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `notmobile`
--

DROP TABLE IF EXISTS `notmobile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notmobile` (
  `nmo_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nmo_description` varchar(45) NOT NULL,
  PRIMARY KEY (`nmo_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `picture`
--

DROP TABLE IF EXISTS `picture`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `picture` (
  `pic_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `pic_picture` varchar(2048) NOT NULL COMMENT 'URL for the picture stored in S3 drive',
  `pic_saturation` int(11) NOT NULL,
  `pic_brightness` int(11) NOT NULL,
  `pic_contrast` int(11) NOT NULL,
  `pic_sharpening` int(11) NOT NULL,
  `pic_vignetting` int(11) NOT NULL,
  `pic_rotation` int(11) NOT NULL,
  `pic_automadjustment` int(11) NOT NULL,
  PRIMARY KEY (`pic_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rating`
--

DROP TABLE IF EXISTS `rating`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rating` (
  `rat_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `rat_use_id` bigint(20) NOT NULL,
  `rat_ite_id` bigint(20) DEFAULT NULL,
  `rat_comment` varchar(512) DEFAULT NULL,
  `rat_ratedate` datetime DEFAULT NULL,
  `rat_status` varchar(10) DEFAULT NULL,
  `rat_rater_use_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`rat_id`),
  KEY `fk_rating_user1_idx` (`rat_use_id`),
  KEY `fk_rating_rater_idx` (`rat_rater_use_id`),
  CONSTRAINT `fk_rating_rater` FOREIGN KEY (`rat_rater_use_id`) REFERENCES `user` (`use_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_rating_user1` FOREIGN KEY (`rat_use_id`) REFERENCES `user` (`use_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=481 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(45) NOT NULL,
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `role_name_UNIQUE` (`role_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `schema_version`
--

DROP TABLE IF EXISTS `schema_version`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `schema_version` (
  `version_rank` int(11) NOT NULL,
  `installed_rank` int(11) NOT NULL,
  `version` varchar(50) NOT NULL,
  `description` varchar(200) NOT NULL,
  `type` varchar(20) NOT NULL,
  `script` varchar(1000) NOT NULL,
  `checksum` int(11) DEFAULT NULL,
  `installed_by` varchar(100) NOT NULL,
  `installed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `execution_time` int(11) NOT NULL,
  `success` tinyint(1) NOT NULL,
  PRIMARY KEY (`version`),
  KEY `schema_version_vr_idx` (`version_rank`),
  KEY `schema_version_ir_idx` (`installed_rank`),
  KEY `schema_version_s_idx` (`success`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `social`
--

DROP TABLE IF EXISTS `social`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `social` (
  `soc_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `soc_description` varchar(45) NOT NULL,
  PRIMARY KEY (`soc_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `state`
--

DROP TABLE IF EXISTS `state`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `state` (
  `sta_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sta_cou_id` bigint(20) NOT NULL,
  `sta_description` varchar(45) NOT NULL,
  PRIMARY KEY (`sta_id`),
  KEY `fk_state_country1_idx` (`sta_cou_id`),
  CONSTRAINT `fk_state_country1` FOREIGN KEY (`sta_cou_id`) REFERENCES `country` (`cou_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tutorial`
--

DROP TABLE IF EXISTS `tutorial`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tutorial` (
  `tut_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tut_step` bigint(20) NOT NULL,
  `tut_picture` varchar(512) NOT NULL,
  `tut_idi_id` bigint(20) NOT NULL,
  PRIMARY KEY (`tut_id`),
  UNIQUE KEY `tut_UNIQUE` (`tut_step`,`tut_idi_id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `use_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `use_username` varchar(45) NOT NULL,
  `use_password` blob,
  `use_email` varchar(255) NOT NULL,
  `use_creation` datetime NOT NULL,
  `use_token` varchar(512) DEFAULT NULL,
  `use_lat` float NOT NULL,
  `use_long` float NOT NULL,
  `use_location_name` varchar(128) DEFAULT NULL,
  `use_zipcode` varchar(45) DEFAULT NULL,
  `use_name` varchar(45) DEFAULT NULL,
  `use_lastname` varchar(45) DEFAULT NULL,
  `use_picture` varchar(512) DEFAULT NULL,
  `use_mobile` varchar(45) DEFAULT NULL,
  `use_cit_id` bigint(20) DEFAULT NULL,
  `use_nmo_id` bigint(20) DEFAULT NULL,
  `use_idi_id` bigint(20) DEFAULT NULL,
  `use_url` varchar(2084) DEFAULT NULL,
  `use_faillogins` int(11) DEFAULT NULL COMMENT 'Track of failed logins',
  `use_last_forget` datetime DEFAULT NULL,
  `use_last_signin_attempt` datetime DEFAULT NULL,
  `use_failed_signin_attempts` int(11) DEFAULT '0',
  `use_temporary_token` varchar(512) DEFAULT NULL,
  `use_last_successful_login` datetime DEFAULT NULL,
  `use_email_verified` bit(1) DEFAULT b'0',
  `use_status` varchar(45) NOT NULL COMMENT 'User Status',
  `use_zip_code` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`use_id`),
  UNIQUE KEY `use_email_UNIQUE` (`use_email`),
  UNIQUE KEY `use_username_UNIQUE` (`use_username`),
  KEY `use_token_idx` (`use_token`(255)),
  KEY `fk_user_city1_idx` (`use_cit_id`),
  KEY `fk_user_notmobile1_idx` (`use_nmo_id`),
  KEY `fk_user_idiom1_idx` (`use_idi_id`),
  CONSTRAINT `fk_user_city1` FOREIGN KEY (`use_cit_id`) REFERENCES `city` (`cit_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_idiom1` FOREIGN KEY (`use_idi_id`) REFERENCES `idiom` (`idi_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_notmobile1` FOREIGN KEY (`use_nmo_id`) REFERENCES `notmobile` (`nmo_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=394 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `user_admin`
--

DROP TABLE IF EXISTS `user_admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_admin` (
  `admin_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `admin_username` varchar(45) NOT NULL,
  `admin_password` blob,
  `admin_email` varchar(255) NOT NULL,
  `admin_token` varchar(512) DEFAULT NULL,
  `admin_name` varchar(45) DEFAULT NULL,
  `admin_lastname` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`admin_id`),
  UNIQUE KEY `admin_email_UNIQUE` (`admin_email`),
  UNIQUE KEY `admin_username_UNIQUE` (`admin_username`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_admin_role`
--

DROP TABLE IF EXISTS `user_admin_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_admin_role` (
  `uar_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_admin_id` bigint(20) NOT NULL,
  `role_name` varchar(45) NOT NULL,
  PRIMARY KEY (`uar_id`),
  KEY `fk_ua_role_user_admin` (`user_admin_id`),
  CONSTRAINT `fk_ua_role_user_admin` FOREIGN KEY (`user_admin_id`) REFERENCES `user_admin` (`admin_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `user_group`
--

DROP TABLE IF EXISTS `user_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_group` (
  `ug_use_id` bigint(20) NOT NULL,
  `ug_grp_id` bigint(20) NOT NULL,
  PRIMARY KEY (`ug_use_id`,`ug_grp_id`),
  KEY `FK_67_user_group_grp_id_asc` (`ug_grp_id`),
  KEY `fk_user_group_group1_idx` (`ug_use_id`),
  CONSTRAINT `fk_user_group_group1` FOREIGN KEY (`ug_grp_id`) REFERENCES `group` (`grp_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_group_user1` FOREIGN KEY (`ug_use_id`) REFERENCES `user` (`use_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_itemliked`
--

DROP TABLE IF EXISTS `user_itemliked`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_itemliked` (
  `ul_usu_id` bigint(20) NOT NULL,
  `ul_ite_id` bigint(20) NOT NULL,
  `ul_dateliked` datetime NOT NULL,
  PRIMARY KEY (`ul_usu_id`,`ul_ite_id`),
  KEY `fk_user_itemliked_user1_idx` (`ul_usu_id`),
  KEY `fk_user_itemliked_item1_idx` (`ul_ite_id`),
  CONSTRAINT `fk_user_itemliked_item1` FOREIGN KEY (`ul_ite_id`) REFERENCES `item` (`ite_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_itemliked_user1` FOREIGN KEY (`ul_usu_id`) REFERENCES `user` (`use_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `user_itemreport`
--

DROP TABLE IF EXISTS `user_itemreport`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_itemreport` (
  `ur_usu_id` bigint(20) NOT NULL,
  `ur_ite_id` bigint(20) NOT NULL,
  `ur_report_date` datetime NOT NULL,
  `ur_user_comment` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ur_usu_id`,`ur_ite_id`),
  KEY `fk_user_itemreport_user1_idx` (`ur_usu_id`),
  KEY `fk_user_itemreport_item1_idx` (`ur_ite_id`),
  CONSTRAINT `fk_user_itemreport_item1` FOREIGN KEY (`ur_ite_id`) REFERENCES `item` (`ite_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_itemreport_user1` FOREIGN KEY (`ur_usu_id`) REFERENCES `user` (`use_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_notification`
--

DROP TABLE IF EXISTS `user_notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_notification` (
  `us_use_id` bigint(20) NOT NULL,
  `us_not_id` bigint(20) NOT NULL,
  PRIMARY KEY (`us_use_id`,`us_not_id`),
  KEY `fk_notification_id1` (`us_not_id`),
  CONSTRAINT `fk_notification_id1` FOREIGN KEY (`us_not_id`) REFERENCES `notification_config` (`not_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_id1` FOREIGN KEY (`us_use_id`) REFERENCES `user` (`use_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `user_social`
--

DROP TABLE IF EXISTS `user_social`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_social` (
  `us_user_id` bigint(20) NOT NULL,
  `us_social_id` bigint(20) NOT NULL,
  `us_token` varchar(512) NOT NULL,
  `us_token_secret` varchar(255) DEFAULT NULL,
  `us_username` varchar(45) NOT NULL,
  PRIMARY KEY (`us_user_id`,`us_social_id`),
  KEY `fk_user_social_user1_idx` (`us_user_id`),
  KEY `fk_user_social_social1_idx` (`us_social_id`),
  CONSTRAINT `fk_user_social_social1` FOREIGN KEY (`us_social_id`) REFERENCES `social` (`soc_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_social_user1` FOREIGN KEY (`us_user_id`) REFERENCES `user` (`use_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


INSERT INTO `category`(`cat_id`,`cat_key`,`cat_picture`,`cat_web_color`) VALUES
(1, "category.name.sweetSixteenEvents","http://d9de1c2614c54ebad451-c6ae1bfee52f95c9114af051097d89dd.r53.cf5.rackcdn.com/user_files/category/events_2011940506662640826","e74587"),
(2, "category.name.fashionForHim","http://d9de1c2614c54ebad451-c6ae1bfee52f95c9114af051097d89dd.r53.cf5.rackcdn.com/user_files/category/forhim","0066a7"),
(3, "category.name.fashionForHer","http://d9de1c2614c54ebad451-c6ae1bfee52f95c9114af051097d89dd.r53.cf5.rackcdn.com/user_files/category/forher","d82a22"),
(4, "category.name.music","http://d9de1c2614c54ebad451-c6ae1bfee52f95c9114af051097d89dd.r53.cf5.rackcdn.com/user_files/category/music_8085392159752770501","8ebb36"),
(5, "category.name.electronics","http://d9de1c2614c54ebad451-c6ae1bfee52f95c9114af051097d89dd.r53.cf5.rackcdn.com/user_files/category/electronics_-6278308793342704190","23c5cf"),
(6, "category.name.sports","http://d9de1c2614c54ebad451-c6ae1bfee52f95c9114af051097d89dd.r53.cf5.rackcdn.com/user_files/category/sports_-3725985444868629784","ec521b"),
(7, "category.name.design","http://d9de1c2614c54ebad451-c6ae1bfee52f95c9114af051097d89dd.r53.cf5.rackcdn.com/user_files/category/design_-8913302874252430005","873c8a"),
(8, "category.name.cars","http://d9de1c2614c54ebad451-c6ae1bfee52f95c9114af051097d89dd.r53.cf5.rackcdn.com/user_files/category/cars_-3457813049163741035","ffaa00"),
(9, "category.name.services","http://d9de1c2614c54ebad451-c6ae1bfee52f95c9114af051097d89dd.r53.cf5.rackcdn.com/user_files/category/services_-7337489785862622622","009e36"),
(10, "category.name.others","http://d9de1c2614c54ebad451-c6ae1bfee52f95c9114af051097d89dd.r53.cf5.rackcdn.com/user_files/category/others_1337050038103535276","7da7d6");


INSERT INTO `currency`(`cur_id`,`cur_description`)
VALUES (1, "US dollar");


INSERT INTO `country`(`cou_id`,`cou_cur_id`,`cou_description`)
VALUES (1, 1,"United States of America");


INSERT INTO `state`(`sta_id`,`sta_cou_id`,`sta_description`)
VALUES (1, 1, "New York");


INSERT INTO `city`(`cit_id`,`cit_sta_id`,`cit_description`)
VALUES (1, 1,"Manhattan");

INSERT INTO idiom (idi_id, idi_code) VALUES (1, 'en');
INSERT INTO idiom (idi_id, idi_code) VALUES (2, 'es');
INSERT INTO social (soc_id, soc_description) VALUES (1, 'facebook');
INSERT INTO social (soc_id, soc_description) VALUES (2, 'twitter');




INSERT INTO user_admin (admin_id, admin_username, admin_email, admin_password) VALUES (30,'moderator_admin','moderator@mail.com',x'438ad75c982ac2c0f2e28dffef88f7ef2b97623994873e645729826e41406377'); -- Pass: V3RY_$TR0NG
INSERT INTO user_admin_role (uar_id, user_admin_id,role_name) VALUES (1, 30, 'SUPER_ADMIN');
INSERT INTO user_admin_role (uar_id, user_admin_id,role_name) VALUES (2, 30, 'MODERATOR');





-- -----------------------------------------------------
-- Blacklisted Words
-- -----------------------------------------------------
insert into black_list values (1,'viagra');
insert into black_list values (2,'striper');
insert into black_list values (3,'stripper');
insert into black_list values (4,'swinger');
insert into black_list values (5,'verga');
insert into black_list values (6,'puta');
insert into black_list values (7,'erotic');
insert into black_list values (8,'estrippers');
insert into black_list values (9,'estripers');
insert into black_list values (10,'strepeer');
insert into black_list values (11,'strepers');
insert into black_list values (12,'strepper');
insert into black_list values (13,'streeper');
insert into black_list values (14,'streper');
insert into black_list values (15,'colegialas');
insert into black_list values (16,'sin ropa');
insert into black_list values (17,'concha');
insert into black_list values (18,'pito');
insert into black_list values (19,'besandose');
insert into black_list values (20,'bribrador');
insert into black_list values (21,'porno');
insert into black_list values (22,'xxx');
insert into black_list values (23,'potos');
insert into black_list values (24,'poto');
insert into black_list values (25,'bajinal');
insert into black_list values (26,'vajinal');
insert into black_list values (27,'cachonda');
insert into black_list values (28,'prostatico');
insert into black_list values (29,'estibas');
insert into black_list values (30,'morbosa');
insert into black_list values (31,'sumisas');
insert into black_list values (32,'atrevidas');
insert into black_list values (33,'mamada');
insert into black_list values (34,'masajes');
insert into black_list values (35,'enbrel');
insert into black_list values (36,'embrel');
insert into black_list values (37,'etanercept');
insert into black_list values (38,'oxaprost');
insert into black_list values (39,'travesti');
insert into black_list values (40,'pedofilo');
insert into black_list values (41,'pedofila');
insert into black_list values (42,'meizitang');
insert into black_list values (43,'reductil');
insert into black_list values (44,'be slim');
insert into black_list values (45,'lsd');
insert into black_list values (46,'sibutramina');
insert into black_list values (47,'misotrol');
insert into black_list values (48,'mephedrone');
insert into black_list values (49,'mefedrina');
insert into black_list values (50,'cocaina');
insert into black_list values (51,'aborto');
insert into black_list values (52,'abortiva');
insert into black_list values (53,'emprestimo');
insert into black_list values (54,'prestec');
insert into black_list values (55,'wholesale');
insert into black_list values (56,'hackear');
insert into black_list values (57,'sicario');
insert into black_list values (58,'asesino');
insert into black_list values (59,'espiao');
insert into black_list values (60,'intercept');
insert into black_list values (61,'folla');
insert into black_list values (62,'WARRANTY');
insert into black_list values (63,'market_arena');
insert into black_list values (64,'digitalmobilestores');
insert into black_list values (65,'sex');
insert into black_list values (66,'webcam');
insert into black_list values (67,'chippendale');
insert into black_list values (68,'escort');
insert into black_list values (69,'sexual');
insert into black_list values (70,'despedida de soltera');
insert into black_list values (71,'Solo para mujeres');
insert into black_list values (72,'videos de depedidas');
insert into black_list values (73,'chicas sexis');
insert into black_list values (74,'despedidas de solteras');
insert into black_list values (75,'maduras');
insert into black_list values (76,'sexis');
insert into black_list values (77,'sexys');
insert into black_list values (78,'desnudo');
insert into black_list values (79,'adolecentes');
insert into black_list values (80,'anal');
insert into black_list values (90,'vagina');
insert into black_list values (91,'pene');
insert into black_list values (92,'huagpas');
insert into black_list values (93,'mujeres adultas');
insert into black_list values (94,'bañandose');
insert into black_list values (95,'prohibida');
insert into black_list values (96,'consolador');
insert into black_list values (97,'vibrador');
insert into black_list values (98,'penes');
insert into black_list values (99,'vaginal');
insert into black_list values (100,'sexso');
insert into black_list values (101,'prestamo');
insert into black_list values (102,'lacoste');
insert into black_list values (103,'teta');
insert into black_list values (104,'acompañante');
insert into black_list values (105,'follar');
insert into black_list values (106,'videochat');
insert into black_list values (107,'fruta planta');
insert into black_list values (108,'caballo frison');
insert into black_list values (109,'loros');
insert into black_list values (110,'monos');
insert into black_list values (111,'guacamayos');
insert into black_list values (112,'cacatuas');
insert into black_list values (113,'unlocked');
insert into black_list values (114,'telex');
insert into black_list values (115,'telexfree');
insert into black_list values (116,'zeek');
insert into black_list values (117,'ucash');
insert into black_list values (118,'ukash');
insert into black_list values (119,'venta de iva');
insert into black_list values (120,'venta de facturas');
insert into black_list values (121,'desbloqueo');
insert into black_list values (122,'lamiabolsas');
insert into black_list values (123,'junior idols');
insert into black_list values (124,'moecco');
insert into black_list values (125,'sho-boh');
insert into black_list values (126,'chu-boh');
insert into black_list values (127,'peace mook');
insert into black_list values (128,'secso');

-- -----------------------------------------------------
-- email notification config
-- -----------------------------------------------------

INSERT INTO `notification_config` (`not_id`, `not_type`, `not_action`, `not_template_name`) VALUES (1,'EMAIL','CHAT_SENT','chat-sent.vm');
INSERT INTO `notification_config` (`not_id`, `not_type`, `not_action`, `not_template_name`) VALUES (2,'EMAIL','CONVERSATION_ACCEPTED','conversation-accepted.vm');
INSERT INTO `notification_config` (`not_id`, `not_type`, `not_action`, `not_template_name`) VALUES (3,'EMAIL','CONVERSATION_REJECTED','conversation-rejected.vm');
INSERT INTO `notification_config` (`not_id`, `not_type`, `not_action`, `not_template_name`) VALUES (4,'EMAIL','USER_BLOCKED','account-blocked.vm');
INSERT INTO `notification_config` (`not_id`, `not_type`, `not_action`, `not_template_name`) VALUES (5,'EMAIL','ITEM_BANNED','item-banned.vm');
INSERT INTO `notification_config` (`not_id`, `not_type`, `not_action`, `not_template_name`) VALUES (6,'EMAIL','USER_RATED','user-rated.vm');
INSERT INTO `notification_config` (`not_id`, `not_type`, `not_action`, `not_template_name`) VALUES (7,'EMAIL','NEW_COMMENT_ON_ITEM','new-comment-on-item.vm');

-- -----------------------------------------------------
-- push notification config
-- -----------------------------------------------------

INSERT INTO `notification_config` (`not_id`, `not_type`, `not_action`, `not_template_name`) VALUES (10,'PUSH','CHAT_SENT','notification.msg.chat_sent');
INSERT INTO `notification_config` (`not_id`, `not_type`, `not_action`, `not_template_name`) VALUES (11,'PUSH','CONVERSATION_ACCEPTED','notification.msg.conversation_accepted');
INSERT INTO `notification_config` (`not_id`, `not_type`, `not_action`, `not_template_name`) VALUES (12,'PUSH','CONVERSATION_REJECTED','notification.msg.conversation_rejected');
INSERT INTO `notification_config` (`not_id`, `not_type`, `not_action`, `not_template_name`) VALUES (13,'PUSH','ITEM_BANNED','notification.msg.item_banned');
INSERT INTO `notification_config` (`not_id`, `not_type`, `not_action`, `not_template_name`) VALUES (14,'PUSH','NEW_COMMENT_WHEN_SUBSCRIBE','notification.msg.new_comment_when_subscribe');
INSERT INTO `notification_config` (`not_id`, `not_type`, `not_action`, `not_template_name`) VALUES (15,'PUSH','USER_RATED','notification.msg.user_rated');
INSERT INTO `notification_config` (`not_id`, `not_type`, `not_action`, `not_template_name`) VALUES (16,'PUSH','SOLD_AN_ITEM','notification.msg.sold_an_item');
INSERT INTO `notification_config` (`not_id`, `not_type`, `not_action`, `not_template_name`) VALUES (17,'PUSH','NEW_COMMENT_ON_ITEM','notification.msg.new_comment_on_item');
INSERT INTO `notification_config` (`not_id`, `not_type`, `not_action`, `not_template_name`) VALUES (18,'PUSH','FOLLOW_USER','notification.msg.follow_user');
INSERT INTO `notification_config` (`not_id`, `not_type`, `not_action`, `not_template_name`) VALUES (19,'PUSH','UNFOLLOW_USER','notification.msg.unfollow_user');
INSERT INTO `notification_config` (`not_id`, `not_type`, `not_action`, `not_template_name`) VALUES (20,'PUSH','FOLLOW_ITEM','notification.msg.follow_item');

-- -----------------------------------------------------
-- feed notification config
-- -----------------------------------------------------

INSERT INTO `notification_config` (`not_id`, `not_type`, `not_action`, `not_template_name`) VALUES (27,'FEED','CHAT_SENT','notification.msg.chat_sent');
INSERT INTO `notification_config` (`not_id`, `not_type`, `not_action`, `not_template_name`) VALUES (28,'FEED','CONVERSATION_ACCEPTED','notification.msg.conversation_accepted');
INSERT INTO `notification_config` (`not_id`, `not_type`, `not_action`, `not_template_name`) VALUES (29,'FEED','CONVERSATION_REJECTED','notification.msg.conversation_rejected');
INSERT INTO `notification_config` (`not_id`, `not_type`, `not_action`, `not_template_name`) VALUES (30,'FEED','ITEM_BANNED','notification.msg.item_banned');
INSERT INTO `notification_config` (`not_id`, `not_type`, `not_action`, `not_template_name`) VALUES (31,'FEED','NEW_COMMENT_WHEN_SUBSCRIBE','notification.msg.new_comment_when_subscribe');
INSERT INTO `notification_config` (`not_id`, `not_type`, `not_action`, `not_template_name`) VALUES (32,'FEED','USER_RATED','notification.msg.user_rated');
INSERT INTO `notification_config` (`not_id`, `not_type`, `not_action`, `not_template_name`) VALUES (33,'FEED','PENDING_RATE','notification.msg.prending_rate');
INSERT INTO `notification_config` (`not_id`, `not_type`, `not_action`, `not_template_name`) VALUES (34,'FEED','NEW_ITEM','notification.msg.new_item');
INSERT INTO `notification_config` (`not_id`, `not_type`, `not_action`, `not_template_name`) VALUES (35,'FEED','ITEM_APROVED','notification.msg.item_approved');
INSERT INTO `notification_config` (`not_id`, `not_type`, `not_action`, `not_template_name`) VALUES (36,'FEED','ITEM_REJECTED','notification.msg.item.rejected');
INSERT INTO `notification_config` (`not_id`, `not_type`, `not_action`, `not_template_name`) VALUES (37,'FEED','ITEM_EXPIRE','notification.msg.item_expire');
INSERT INTO `notification_config` (`not_id`, `not_type`, `not_action`, `not_template_name`) VALUES (38,'FEED','UPDATE_AN_ITEM','notification.msg.item_updated');
INSERT INTO `notification_config` (`not_id`, `not_type`, `not_action`, `not_template_name`) VALUES (39,'FEED','DELETE_AN_ITEM','notification.msg.delete_an_item');
INSERT INTO `notification_config` (`not_id`, `not_type`, `not_action`, `not_template_name`) VALUES (40,'FEED','SOLD_AN_ITEM','notification.msg.sold_an_item');
INSERT INTO `notification_config` (`not_id`, `not_type`, `not_action`, `not_template_name`) VALUES (41,'FEED','NEW_COMMENT_ON_ITEM','notification.msg.new_comment_on_item');
INSERT INTO `notification_config` (`not_id`, `not_type`, `not_action`, `not_template_name`) VALUES (42,'FEED','ADD_ITEM_TO_GROUP','notification.msg.add_item_to_group');
INSERT INTO `notification_config` (`not_id`, `not_type`, `not_action`, `not_template_name`) VALUES (43,'FEED','FOLLOW_USER','notification.msg.follow_user');
INSERT INTO `notification_config` (`not_id`, `not_type`, `not_action`, `not_template_name`) VALUES (44,'FEED','UNFOLLOW_USER','notification.msg.unfollow_user');
INSERT INTO `notification_config` (`not_id`, `not_type`, `not_action`, `not_template_name`) VALUES (45,'FEED','FOLLOW_ITEM','notification.msg.follow_item');
INSERT INTO `notification_config` (`not_id`, `not_type`, `not_action`, `not_template_name`) VALUES (46,'FEED','EMAIL_NOT_VERIFIED','notification.msg.email_not_verified');

delete from notification_config where not_id in (6, 19, 20, 44, 45, 46);
INSERT INTO notification_config (not_id, not_type, not_action, not_template_name) VALUES (47,'FEED','ITEM_ABOUT_TO_EXPIRE','notification.msg.item_about_to_expire');

INSERT INTO tutorial(tut_step, tut_picture, tut_idi_id) values 
(1,'http://tutorial1en.com',1),
(1,'http://tutorial1es.com',2),
(2,'http://tutorial2en.com',1),
(2,'http://tutorial2es.com',2),
(3,'http://tutorial3en.com',1),
(3,'http://tutorial3es.com',2);




INSERT INTO `banner`(`banner_id`, `banner_priority`, `banner_type`, `banner_msg`,`banner_idi_id`) VALUES (1,"USER_BASED", "TIP", "Remember to verify your email account! You won't be able to publish an item until you do it",1);
INSERT INTO `banner`(`banner_id`, `banner_priority`, `banner_type`, `banner_msg`,`banner_idi_id`) VALUES (2,"USER_BASED", "TIP","Recuerde verificar su cuenta de correo! No podra publicar items hasta que no la verifique",2);
INSERT INTO `banner`(`banner_id`, `banner_priority`, `banner_type`, `banner_msg`,`banner_idi_id`) VALUES (3,"NON_SHOW", "FACEBOOK", "Do you know you can check which of your Facebook friends are using this app? Check that feature in your preferences!",1);
INSERT INTO `banner`(`banner_id`, `banner_priority`, `banner_type`, `banner_msg`,`banner_idi_id`) VALUES (4,"NON_SHOW", "FACEBOOK","Sabias que podes verificar que amigos de facebook estan usando esta aplicacion? Chequealo en tus preferencias!",2);
INSERT INTO `banner`(`banner_id`, `banner_priority`, `banner_type`, `banner_msg`,`banner_idi_id`) VALUES (5,"NON_SHOW", "TWITTER", "Do you know you can check which of your Twitter friends are using this app? Check that feature in your preferences!",1);
INSERT INTO `banner`(`banner_id`, `banner_priority`, `banner_type`, `banner_msg`,`banner_idi_id`) VALUES (6,"NON_SHOW", "TWITTER", "Sabias que podes verificar que amigos de Twitter estan usando esta aplicacion? Chequealo en tus preferencias!",2);
INSERT INTO `banner`(`banner_id`, `banner_priority`, `banner_type`, `banner_msg`,`banner_idi_id`) VALUES (7,"GENERAL_MESSAGES", "TIP", "Do you know you can invite your friends through e-mail? Check that feature in your preferences!",1);
INSERT INTO `banner`(`banner_id`, `banner_priority`, `banner_type`, `banner_msg`,`banner_idi_id`) VALUES (8,"GENERAL_MESSAGES", "TIP", "Sabias que podes invitar a tus amigos por correo? Chequealo en tus preferencias!",2);




