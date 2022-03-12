-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema document_management_system
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema document_management_system
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `document_management_system` DEFAULT CHARACTER SET utf8 ;
USE `document_management_system` ;

-- -----------------------------------------------------
-- Table `document_management_system`.`Users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `document_management_system`.`Users` (
  `user_id` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(55) NOT NULL,
  `username` VARCHAR(55) NOT NULL,
  `role` VARCHAR(45) NOT NULL,
  `created_at` DATETIME NOT NULL,
  `active` TINYINT NOT NULL,
  `oidc_iss` VARCHAR(512) NULL,
  `oidc_sub` VARCHAR(255) NULL,
  `can_create` TINYINT NOT NULL,
  `can_read` TINYINT NOT NULL,
  `can_update` TINYINT NOT NULL,
  `can_delete` TINYINT NOT NULL,
  `root_dir_id` INT NULL,
  `access_from_ip` VARCHAR(15) NULL,
  `access_from_domain` VARCHAR(255) NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE,
  UNIQUE INDEX `users_oidc_unique_iss_sub` (`oidc_iss`, `oidc_sub`) VISIBLE,
  CONSTRAINT `users_root_dir_fk`
    FOREIGN KEY (`root_dir_id`)
    REFERENCES `document_management_system`.`Files` (`file_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `document_management_system`.`Files`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `document_management_system`.`Files` (
  `file_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(55) NOT NULL,
  `is_directory` TINYINT NULL,
  `parent_id` INT NOT NULL,
  `discarded` TINYINT NOT NULL,
  `deleted` TINYINT NOT NULL,
  `num_of_versions` SMALLINT DEFAULT 0,
  PRIMARY KEY (`file_id`),
  INDEX `parent_id_idx` (`parent_id` ASC) VISIBLE,
  CONSTRAINT `parent_id`
    FOREIGN KEY (`parent_id`)
    REFERENCES `document_management_system`.`Files` (`file_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `document_management_system`.`file_versions`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `document_management_system`.`file_versions` (
  `file_id` INT NOT NULL,
  `version` SMALLINT NOT NULL,
  PRIMARY KEY (`file_id`, `version`),
  CONSTRAINT `file_versions_file_id_fk`
    FOREIGN KEY (`file_id`)
    REFERENCES `document_management_system`.`files` (`file_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `document_management_system`.`File_logs`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `document_management_system`.`File_logs` (
  `log_id` BIGINT NOT NULL,
  `description` VARCHAR(255) NOT NULL,
  `file_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  `timestamp` DATETIME NOT NULL,
  `operation` CHAR(2) NOT NULL,
  PRIMARY KEY (`log_id`),
  INDEX `file_id_idx` (`file_id` ASC) VISIBLE,
  INDEX `user_id_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `file_id`
    FOREIGN KEY (`file_id`)
    REFERENCES `document_management_system`.`Files` (`file_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `File_logs_user_id_fk`
    FOREIGN KEY (`user_id`)
    REFERENCES `document_management_system`.`Users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


USE `document_management_system`;

DELIMITER $$
USE `document_management_system`$$
CREATE DEFINER = CURRENT_USER TRIGGER `document_management_system`.`file_versions_AFTER_INSERT` AFTER INSERT ON `file_versions` FOR EACH ROW
BEGIN
	UPDATE files SET num_of_versions=num_of_versions+1 WHERE file_id=NEW.file_id;
END$$


DELIMITER ;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
