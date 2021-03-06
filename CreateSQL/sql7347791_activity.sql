-- MySQL dump 10.13  Distrib 8.0.20, for Win64 (x86_64)
--
-- Host: sql7.freemysqlhosting.net    Database: sql7347791
-- ------------------------------------------------------
-- Server version	5.5.62-0ubuntu0.14.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `activity`
--

DROP TABLE IF EXISTS `activity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activity` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `id_conto` int(11) DEFAULT NULL,
  `total` double DEFAULT NULL,
  `recipient` varchar(20) DEFAULT NULL,
  `description` varchar(20) DEFAULT NULL,
  `operation_type` varchar(40) DEFAULT NULL,
  `create_at` date DEFAULT NULL,
  `validated_at` date DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `currency` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ID_CONTO_idx` (`id_conto`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity`
--

LOCK TABLES `activity` WRITE;
/*!40000 ALTER TABLE `activity` DISABLE KEYS */;
INSERT INTO `activity` VALUES (1,3,500,'1','Esempio di causale','Bonifico','2020-06-23',NULL,'Pending','EUR'),(2,3,500,'1','Esempio di causale','Bonifico','2020-06-23',NULL,'Pending','EUR'),(3,3,500,'1','Esempio di causale','Bonifico','2020-06-23',NULL,'Pending','EUR'),(4,3,500,'1','Esempio di causale','Bonifico','2020-06-23',NULL,'Pending','EUR'),(5,3,500,'2','Esempio di causale','Bonifico','2020-06-23',NULL,'Pending','EUR'),(6,3,500,'2','Esempio di causale','Bonifico','2020-06-23',NULL,'Pending','EUR'),(7,3,500,'2','Esempio di causale','Bonifico','2020-06-23',NULL,'Pending','EUR'),(8,3,500,'2','Esempio di causale','Bonifico','2020-06-23',NULL,'Pending','EUR'),(9,2,500,'3','Esempio di causale','Bonifico','2020-06-23',NULL,'Pending','EUR'),(10,2,500,'3','Esempio di causale','Bonifico','2020-06-23',NULL,'Pending','EUR'),(11,2,500,'3','Esempio di causale','Bonifico','2020-06-23',NULL,'Pending','EUR');
/*!40000 ALTER TABLE `activity` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-06-23 13:14:36
