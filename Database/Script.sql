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
  `password` VARCHAR(255) NULL,
  `role` VARCHAR(45) NOT NULL,
  `created_at` DATETIME NOT NULL,
  `active` TINYINT NULL,
  `oidc_iss` varchar(512) NULL,
  `oidc_sub` varchar(255) NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `document_management_system`.`Files`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `document_management_system`.`Files` (
  `file_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(55) NOT NULL,
  `is_directory` TINYINT NULL,
  `parent_id` INT NOT NULL,
  `created_at` DATETIME NOT NULL,
  `created_by` INT NOT NULL,
  `discarded` TINYINT NULL,
  `deleted` TINYINT NULL,
  PRIMARY KEY (`file_id`),
  INDEX `created_by_idx` (`created_by` ASC) VISIBLE,
  INDEX `parent_id_idx` (`parent_id` ASC) VISIBLE,
  CONSTRAINT `parent_id`
    FOREIGN KEY (`parent_id`)
    REFERENCES `document_management_system`.`Files` (`file_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `created_by`
    FOREIGN KEY (`created_by`)
    REFERENCES `document_management_system`.`Users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `document_management_system`.`Directory_administrators`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `document_management_system`.`Directory_administrators` (
  `dir_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  `created_at` DATETIME NOT NULL,
  `appointed_by` INT NOT NULL,
  `create` TINYINT NOT NULL,
  `read` TINYINT NOT NULL,
  `update` TINYINT NOT NULL,
  `delete` TINYINT NOT NULL,
  PRIMARY KEY (`dir_id`, `user_id`),
  INDEX `user_id_idx` (`user_id` ASC) VISIBLE,
  INDEX `created_by_idx` (`appointed_by` ASC) VISIBLE,
  CONSTRAINT `dir_id`
    FOREIGN KEY (`dir_id`)
    REFERENCES `document_management_system`.`Files` (`file_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `Directory_administrators_user_id_fk`
    FOREIGN KEY (`user_id`)
    REFERENCES `document_management_system`.`Users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `appointed_by`
    FOREIGN KEY (`appointed_by`)
    REFERENCES `document_management_system`.`Users` (`user_id`)
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
  `operation` CHAR(1) NOT NULL,
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


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
