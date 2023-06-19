CREATE DATABASE farm_inventory;
USE farm_inventory;

DROP TABLE IF EXISTS `animal`;
CREATE TABLE `animal` (
  `animalID` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `dateOfBirth` date NOT NULL,
  `gender` varchar(6) NOT NULL,
  `altered` varchar(3) NOT NULL,
  `breed` varchar(45) NOT NULL,
  `color` varchar(45) NOT NULL,
  `type` varchar(45) NOT NULL,
  PRIMARY KEY (`animalID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `item`;
CREATE TABLE `item` (
  `itemID` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `animalFor` varchar(45) NOT NULL,
  `dateOfPurchase` date NOT NULL,
  `type` varchar(45) NOT NULL,
  `cost` decimal(6,2) NOT NULL,
  `reason` varchar(100) NOT NULL,
  `year` varchar(4) NOT NULL,
  PRIMARY KEY (`itemID`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `medication`;
CREATE TABLE `medication` (
  `medID` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `animalFor` varchar(45) NOT NULL,
  `dateOfPurchase` date NOT NULL,
  `cost` decimal(6,2) NOT NULL,
  `emergency` varchar(5) NOT NULL,
  `reason` varchar(100) NOT NULL,
  `year` varchar(4) NOT NULL,
  PRIMARY KEY (`medID`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `visit`;
CREATE TABLE `visit` (
  `visitID` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `animalFor` varchar(45) NOT NULL,
  `dateOfService` date NOT NULL,
  `type` varchar(45) NOT NULL,
  `cost` decimal(6,2) NOT NULL,
  `emergency` varchar(5) NOT NULL,
  `reason` varchar(100) NOT NULL,
  `year` varchar(4) DEFAULT NULL,
  PRIMARY KEY (`visitID`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `userID` int NOT NULL AUTO_INCREMENT,
  `userName` varchar(45) NOT NULL,
  `uesrPassword` varchar(45) NOT NULL,
  PRIMARY KEY (`userID`),
  UNIQUE KEY `iduser_UNIQUE` (`userID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;