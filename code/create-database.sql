-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema bookstore
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema bookstore
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `bookstore` DEFAULT CHARACTER SET utf8 ;
USE `bookstore` ;

-- -----------------------------------------------------
-- Table `bookstore`.`book`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bookstore`.`book` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `Title` TEXT NOT NULL,
  `Author` TEXT NOT NULL,
  `ISBN` VARCHAR(255) NOT NULL,
  `Price` DOUBLE NOT NULL,
  `In_Stock` INT(11) NOT NULL,
  `Category` TEXT NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `ISBN_UNIQUE` (`ISBN` ASC))
ENGINE = InnoDB
AUTO_INCREMENT = 6
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `bookstore`.`customer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bookstore`.`customer` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `First_Name` TEXT NOT NULL,
  `Last_Name` TEXT NOT NULL,
  `Email` VARCHAR(255) NOT NULL,
  `Phone_Number` TEXT NOT NULL,
  `Address` TEXT NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `Email_UNIQUE` (`Email` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `bookstore`.`order`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bookstore`.`order` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `Customer_ID` INT(11) NOT NULL,
  `Discount` INT(11) NOT NULL,
  `Status` TEXT NOT NULL,
  PRIMARY KEY (`ID`, `Customer_ID`),
  INDEX `fkIdx_23` (`Customer_ID` ASC),
  CONSTRAINT `FK_23`
    FOREIGN KEY (`Customer_ID`)
    REFERENCES `bookstore`.`customer` (`ID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `bookstore`.`order_item`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bookstore`.`order_item` (
  `Order_ID` INT(11) NOT NULL,
  `Customer_ID` INT(11) NOT NULL,
  `Book_ID` INT(11) NOT NULL,
  `Quantity` INT(11) NOT NULL,
  PRIMARY KEY (`Order_ID`, `Customer_ID`, `Book_ID`),
  INDEX `fkIdx_38` (`Order_ID` ASC, `Customer_ID` ASC),
  INDEX `fkIdx_44` (`Book_ID` ASC),
  CONSTRAINT `FK_38`
    FOREIGN KEY (`Order_ID` , `Customer_ID`)
    REFERENCES `bookstore`.`order` (`ID` , `Customer_ID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `FK_44`
    FOREIGN KEY (`Book_ID`)
    REFERENCES `bookstore`.`book` (`ID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
