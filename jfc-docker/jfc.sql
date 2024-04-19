-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               11.3.2-MariaDB-1:11.3.2+maria~ubu2204 - mariadb.org binary distribution
-- Server OS:                    debian-linux-gnu
-- HeidiSQL Version:             12.5.0.6677
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- Dumping structure for function jfc.BIN_TO_UUID
DROP FUNCTION IF EXISTS `BIN_TO_UUID`;
DELIMITER //
CREATE DEFINER=`jfc`@`%` FUNCTION `BIN_TO_UUID`(bin BINARY(16)) RETURNS char(36) CHARSET utf8mb4 COLLATE utf8mb4_general_ci
    DETERMINISTIC
BEGIN
  DECLARE hex CHAR(32);
  SET hex = HEX(bin);
  RETURN LOWER(CONCAT(LEFT(hex, 8), '-', MID(hex, 9, 4), '-', MID(hex, 13, 4), '-', MID(hex, 17, 4), '-', RIGHT(hex, 12)));
END//
DELIMITER ;

-- Dumping structure for table jfc.contained
DROP TABLE IF EXISTS `contained`;
CREATE TABLE IF NOT EXISTS `contained` (
  `id` binary(16) NOT NULL,
  `label` varchar(255) DEFAULT NULL,
  `parentId` binary(16) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_contained_contained` (`parentId`),
  CONSTRAINT `FK_contained_contained` FOREIGN KEY (`parentId`) REFERENCES `contained` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table jfc.contained: ~1 rows (approximately)
DELETE FROM `contained`;
INSERT INTO `contained` (`id`, `label`, `parentId`) VALUES
	(_binary 0xbf71b47b42114b118251e9b16be02cf3, 'Hello World 1713461719261', NULL);

-- Dumping structure for function jfc.UUID_TO_BIN
DROP FUNCTION IF EXISTS `UUID_TO_BIN`;
DELIMITER //
CREATE DEFINER=`jfc`@`%` FUNCTION `UUID_TO_BIN`(uuid CHAR(36)) RETURNS binary(16)
    DETERMINISTIC
BEGIN
  RETURN UNHEX(CONCAT(REPLACE(uuid, '-', '')));
END//
DELIMITER ;

-- Dumping structure for view jfc.view_contained
DROP VIEW IF EXISTS `view_contained`;
-- Creating temporary table to overcome VIEW dependency errors
CREATE TABLE `view_contained` (
	`id` BINARY(16) NOT NULL,
	`str_id` CHAR(36) NULL COLLATE 'utf8mb4_general_ci',
	`label` VARCHAR(255) NULL COLLATE 'utf8mb4_general_ci',
	`parentId` BINARY(16) NULL,
	`str_parentId` CHAR(36) NULL COLLATE 'utf8mb4_general_ci',
	`parentLabel` VARCHAR(255) NULL COLLATE 'utf8mb4_general_ci'
) ENGINE=MyISAM;

-- Dumping structure for view jfc.view_contained
DROP VIEW IF EXISTS `view_contained`;
-- Removing temporary table and create final VIEW structure
DROP TABLE IF EXISTS `view_contained`;
CREATE ALGORITHM=UNDEFINED DEFINER=`jfc`@`%` SQL SECURITY DEFINER VIEW `view_contained` AS select `c`.`id` AS `id`,`BIN_TO_UUID`(`c`.`id`) AS `str_id`,`c`.`label` AS `label`,`p`.`id` AS `parentId`,`BIN_TO_UUID`(`p`.`id`) AS `str_parentId`,`p`.`label` AS `parentLabel` from (`contained` `c` left join `contained` `p` on(`c`.`parentId` = `c`.`id`));

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
