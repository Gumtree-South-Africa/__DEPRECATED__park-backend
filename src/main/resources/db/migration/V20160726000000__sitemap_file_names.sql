DROP TABLE IF EXISTS `sitemap_file` ;

CREATE TABLE IF NOT EXISTS `sitemap_file` (
  `sit_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `sit_name` VARCHAR(45) NOT NULL COMMENT 'Name for a sitemap urlset file',
  PRIMARY KEY (`sit_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;