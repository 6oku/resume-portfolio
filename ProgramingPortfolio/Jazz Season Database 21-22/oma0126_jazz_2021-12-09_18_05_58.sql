-- MySQL dump 10.19  Distrib 10.3.29-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: 192.168.0.2    Database: oma0126_jazz
-- ------------------------------------------------------
-- Server version	10.3.23-MariaDB-0+deb10u1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Game`
--

DROP TABLE IF EXISTS `Game`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Game` (
  `date` date NOT NULL,
  `homeOrAway` set('Home','Away') DEFAULT NULL,
  `jazzScore` int(10) unsigned DEFAULT NULL,
  `opponentScore` int(10) unsigned DEFAULT NULL,
  `opponentID` char(3) DEFAULT NULL,
  PRIMARY KEY (`date`),
  KEY `Game_ibfk_1` (`opponentID`),
  CONSTRAINT `Game_ibfk_1` FOREIGN KEY (`opponentID`) REFERENCES `Opponent` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Game`
--

LOCK TABLES `Game` WRITE;
/*!40000 ALTER TABLE `Game` DISABLE KEYS */;
INSERT INTO `Game` VALUES ('2021-10-04','Away',85,111,'SAS'),('2021-10-06','Away',101,111,'DAL'),('2021-10-11','Home',127,96,'NOP'),('2021-10-13','Home',124,120,'MIL'),('2021-10-20','Home',107,86,'OKC'),('2021-10-22','Away',110,101,'SAC'),('2021-10-26','Home',122,110,'DEN'),('2021-10-28','Away',122,91,'HOU'),('2021-10-30','Away',91,107,'CHI'),('2021-10-31','Away',107,95,'MIL'),('2021-11-02','Home',119,113,'SAC'),('2021-11-04','Away',116,98,'ATL'),('2021-11-06','Away',115,118,'MIA'),('2021-11-07','Away',100,107,'ORL'),('2021-11-09','Home',110,98,'ATL'),('2021-11-11','Home',100,111,'IND'),('2021-11-13','Home',105,111,'MIA'),('2021-11-16','Home',120,85,'PHI'),('2021-11-18','Home',119,103,'TOR'),('2021-11-20','Away',123,103,'SAC'),('2021-11-22','Home',118,119,'MEM'),('2021-11-24','Away',110,104,'OKC'),('2021-11-26','Home',97,98,'NOP'),('2021-11-27','Home',127,105,'NOP'),('2021-11-29','Home',129,107,'POR'),('2021-12-03','Home',137,130,'BOS'),('2021-12-05','Away',109,108,'CLE'),('2021-12-08','Away',136,104,'MIN'),('2021-12-09','Away',NULL,NULL,'PHI'),('2021-12-11','Away',NULL,NULL,'WSH'),('2021-12-15','Home',NULL,NULL,'LAC'),('2021-12-17','Home',NULL,NULL,'SAS'),('2021-12-18','Home',NULL,NULL,'WSH'),('2021-12-20','Home',NULL,NULL,'CHA'),('2021-12-23','Home',NULL,NULL,'MIN'),('2021-12-25','Home',NULL,NULL,'DAL'),('2021-12-27','Away',NULL,NULL,'SAS'),('2021-12-29','Away',NULL,NULL,'POR'),('2021-12-31','Home',NULL,NULL,'MIN'),('2022-01-01','Home',NULL,NULL,'GSW');
/*!40000 ALTER TABLE `Game` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MostScored`
--

DROP TABLE IF EXISTS `MostScored`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MostScored` (
  `gameDate` date NOT NULL,
  `jerseyNum` varchar(2) NOT NULL,
  `playerPoints` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`gameDate`,`jerseyNum`),
  KEY `MostScored_ibfk_1` (`jerseyNum`),
  CONSTRAINT `MostScored_ibfk_1` FOREIGN KEY (`jerseyNum`) REFERENCES `Player` (`jerseyNum`) ON UPDATE CASCADE,
  CONSTRAINT `MostScored_ibfk_2` FOREIGN KEY (`gameDate`) REFERENCES `Game` (`date`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MostScored`
--

LOCK TABLES `MostScored` WRITE;
/*!40000 ALTER TABLE `MostScored` DISABLE KEYS */;
INSERT INTO `MostScored` VALUES ('2021-10-04','13',16),('2021-10-06','13',22),('2021-10-11','27',19),('2021-11-02','45',36),('2021-11-04','00',30),('2021-11-06','45',37),('2021-11-07','27',21),('2021-11-09','45',27),('2021-11-11','45',26),('2021-11-13','44',26),('2021-11-16','44',27),('2021-11-18','8',20),('2021-11-20','45',26),('2021-11-22','44',24),('2021-11-24','00',20),('2021-11-26','44',23),('2021-11-27','45',21),('2021-11-29','45',30),('2021-12-03','45',34),('2021-12-05','45',39),('2021-12-08','45',36);
/*!40000 ALTER TABLE `MostScored` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Opponent`
--

DROP TABLE IF EXISTS `Opponent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Opponent` (
  `id` char(3) NOT NULL,
  `teamName` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `teamName` (`teamName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Opponent`
--

LOCK TABLES `Opponent` WRITE;
/*!40000 ALTER TABLE `Opponent` DISABLE KEYS */;
INSERT INTO `Opponent` VALUES ('ATL','Atlanta Hawks'),('BOS','Boston Celtics'),('BKN','Brooklyn Nets'),('CHA','Charlotte Hornets'),('CHI','Chicago Bulls'),('CLE','Cleveland Cavaliers'),('DAL','Dallas Mavericks'),('DEN','Denver Nuggets'),('DET','Detroit Pistons'),('GSW','Golden State Warriors'),('HOU','Houston Rockets'),('IND','Indiana Pacers'),('LAC','Los Angeles Clippers'),('LAL','Los Angeles Lakers'),('MEM','Memphis Grizzlies'),('MIA','Miami Heat'),('MIL','Milwaukee Bucks'),('MIN','Minnesota Timberwolves'),('NOP','New Orleans Pelicans'),('NYK','New York Knicks'),('OKC','Oklahoma City Thunder'),('ORL','Orlando Magic'),('PHX','Pheonix Suns'),('PHI','Philadelphia 76ers'),('POR','Portland Trail Blazers'),('SAC','Sacramento Kings'),('SAS','San Antonio Spurs'),('TOR','Toronto Raptors'),('WSH','Washington Wizards');
/*!40000 ALTER TABLE `Opponent` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Player`
--

DROP TABLE IF EXISTS `Player`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Player` (
  `jerseyNum` varchar(2) NOT NULL,
  `name` varchar(80) NOT NULL,
  PRIMARY KEY (`jerseyNum`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Player`
--

LOCK TABLES `Player` WRITE;
/*!40000 ALTER TABLE `Player` DISABLE KEYS */;
INSERT INTO `Player` VALUES ('44','Bojan Bogdanovic'),('45','Donovan Mitchell'),('33','Elijah Hughes'),('0','Eric Paschall'),('21','Hassan Whiteside'),('13','Jared Butler'),('2','Joe Ingles'),('00','Jordan Clarkson'),('24','Malik Fitts'),('11','Mike Conley'),('81','Miye Oni'),('23','Royce ONeale'),('8','Rudy Gay'),('27','Rudy Gobert'),('3','Trent Forest'),('20','Udoka Azubuike');
/*!40000 ALTER TABLE `Player` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-12-10  1:05:58
