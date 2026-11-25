-- MySQL dump 10.13  Distrib 5.7.12, for Win64 (x86_64)
--
-- Host: localhost    Database: cursos
-- ------------------------------------------------------
-- Server version	5.7.16-log

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
-- Table structure for table `alumno`
--

DROP TABLE IF EXISTS `alumno`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `alumno` (
  `num_alu` int(11) NOT NULL AUTO_INCREMENT,
  `nombre_alu` text NOT NULL,
  `nota_media` double DEFAULT NULL,
  `curso` char(4) NOT NULL,
  PRIMARY KEY (`num_alu`),
  KEY `alumno_ibfk_1` (`curso`),
  CONSTRAINT `alumno_ibfk_1` FOREIGN KEY (`curso`) REFERENCES `curso` (`cod`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alumno`
--

LOCK TABLES `alumno` WRITE;
/*!40000 ALTER TABLE `alumno` DISABLE KEYS */;
INSERT INTO `alumno` VALUES (1,'ANA ALONSO',NULL,'DAM1'),(2,'JAVIER ALONSO',2.5,'DAM2'),(3,'BEATRIZ DUENO',4.6,'DAW2'),(4,'ALBERTO DIAZ',NULL,'DAM1'),(5,'SANDRA ORTIZ',7.86,'DAM2'),(6,'LUIS SANTIAGO',7,'DAM2'),(7,'JORGE MARTIN',8,'DAM1'),(8,'ALICIA RODRIGUEZ',7.5,'DAW2'),(9,'MARTINA PALACIOS',6.5,'DAM2');
/*!40000 ALTER TABLE `alumno` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `curso`
--

DROP TABLE IF EXISTS `curso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `curso` (
  `cod` char(4) NOT NULL,
  `nombre_curso` text NOT NULL,
  `tutor` int(11) DEFAULT NULL,
  PRIMARY KEY (`cod`),
  UNIQUE KEY `tutor_UNIQUE` (`tutor`),
  CONSTRAINT `fk_curso_tutor` FOREIGN KEY (`tutor`) REFERENCES `profesor` (`num_prof`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `curso`
--

LOCK TABLES `curso` WRITE;
/*!40000 ALTER TABLE `curso` DISABLE KEYS */;
INSERT INTO `curso` VALUES ('DAM1','1º DESARROLLO DE APLICACIONES MULTIPLATAFORMA',3),('DAM2','2º DESARROLLO DE APLICACIONES MULTIPLATAFORMA',5),('DAW1','1º DESARROLLO DE APLICACIONES WEB',2),('DAW2','2º DESARROLLO DE APLICACIONES WEB',4);
/*!40000 ALTER TABLE `curso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `profesor`
--

DROP TABLE IF EXISTS `profesor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `profesor` (
  `num_prof` int(11) NOT NULL AUTO_INCREMENT,
  `nom_prof` text NOT NULL,
  `fecha_nac` text,
  PRIMARY KEY (`num_prof`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `profesor`
--

LOCK TABLES `profesor` WRITE;
/*!40000 ALTER TABLE `profesor` DISABLE KEYS */;
INSERT INTO `profesor` VALUES (1,'NURIA CELIS','1990-06-06'),(2,'IVAN LORENZO','1990-01-01'),(3,'PEDRO GONZALEZ','1990-02-02'),(4,'MARTA GUERRERO','1990-03-03'),(5,'LUIS HERRERO','1990-04-04'),(6,'ANGELICA FERNANDEZ','1990-05-05'),(7,'MATEA CALLEJA','1990-06-06'),(8,'VICTOR MUELA','1990-07-07'),(9,'LUIS GARCIA','1990-08-08'),(10,'JESUS GIRON','1990-09-09');
/*!40000 ALTER TABLE `profesor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'cursos'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-11-26 22:09:56
