CREATE DATABASE  IF NOT EXISTS `nomad` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `nomad`;
-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: nomad
-- ------------------------------------------------------
-- Server version	8.0.43

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
-- Table structure for table `applications`
--

DROP TABLE IF EXISTS `applications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `applications` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `additional_notes` varchar(255) DEFAULT NULL,
  `applied_date` datetime(6) DEFAULT NULL,
  `cover_letter` varchar(2000) DEFAULT NULL,
  `estimated_days` int DEFAULT NULL,
  `proposed_budget` double DEFAULT NULL,
  `status` enum('ACCEPTED','PENDING','REJECTED','WITHDRAWN') DEFAULT NULL,
  `updated_date` datetime(6) DEFAULT NULL,
  `freelancer_id` bigint NOT NULL,
  `mission_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKb6j18geningekvlxyamtv2b1e` (`freelancer_id`),
  KEY `FKora3gbldr6agr5t89ggu1n4rh` (`mission_id`),
  CONSTRAINT `FKb6j18geningekvlxyamtv2b1e` FOREIGN KEY (`freelancer_id`) REFERENCES `freelancers` (`id`),
  CONSTRAINT `FKora3gbldr6agr5t89ggu1n4rh` FOREIGN KEY (`mission_id`) REFERENCES `missions` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `applications`
--

LOCK TABLES `applications` WRITE;
/*!40000 ALTER TABLE `applications` DISABLE KEYS */;
INSERT INTO `applications` VALUES (1,NULL,'2025-12-30 15:30:23.761112','dmmd',90,120003,'ACCEPTED','2025-12-30 15:30:23.761112',4,2),(2,'bnbn','2026-01-09 00:17:28.672266','please',1,25000,'PENDING','2026-01-09 00:17:28.672266',9,1),(3,NULL,'2026-01-09 00:17:39.782238','pls',2,12000,'ACCEPTED','2026-01-09 00:17:39.782238',9,2);
/*!40000 ALTER TABLE `applications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contracts`
--

DROP TABLE IF EXISTS `contracts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contracts` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `budget` double DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `description` varchar(2000) DEFAULT NULL,
  `end_date` datetime(6) DEFAULT NULL,
  `start_date` datetime(6) DEFAULT NULL,
  `status` enum('ACCEPTED','CANCELLED','COMPLETED','IN_PROGRESS','PENDING','REJECTED') DEFAULT NULL,
  `title` varchar(500) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `freelancer_id` bigint NOT NULL,
  `mission_id` bigint DEFAULT NULL,
  `recruiter_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKg6n9tbd4t6j2mnlnua0r7wthw` (`freelancer_id`),
  KEY `FKt23hb6gw7nx45dbbfxwkierf8` (`mission_id`),
  KEY `FK2o6l6kotcujm6wjvtfoc15jod` (`recruiter_id`),
  CONSTRAINT `FK2o6l6kotcujm6wjvtfoc15jod` FOREIGN KEY (`recruiter_id`) REFERENCES `recruiters` (`id`),
  CONSTRAINT `FKg6n9tbd4t6j2mnlnua0r7wthw` FOREIGN KEY (`freelancer_id`) REFERENCES `freelancers` (`id`),
  CONSTRAINT `FKt23hb6gw7nx45dbbfxwkierf8` FOREIGN KEY (`mission_id`) REFERENCES `missions` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contracts`
--

LOCK TABLES `contracts` WRITE;
/*!40000 ALTER TABLE `contracts` DISABLE KEYS */;
INSERT INTO `contracts` VALUES (1,12000,'2026-01-09 00:18:06.103310','Contract for mission: Looking for a talented UI/UX designer to create wireframes, mockups, and final designs for a fitness tracking mobile app. Must include user research and prototyping.',NULL,'2026-01-09 00:18:06.102308','IN_PROGRESS','Contract: Mobile App UI/UX Design','2026-01-09 00:18:06.103310',9,2,2),(2,2,'2026-01-09 00:29:37.483521','nn','2026-01-25 23:59:59.000000','2026-01-25 00:00:00.000000','REJECTED','hi','2026-01-09 00:38:22.804735',4,2,2),(3,788,'2026-01-09 00:31:17.699026','n n n','2026-01-09 23:59:59.000000','2026-01-01 00:00:00.000000','REJECTED','rrwactt','2026-01-09 00:37:46.592440',4,5,2);
/*!40000 ALTER TABLE `contracts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `educations`
--

DROP TABLE IF EXISTS `educations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `educations` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `currently_studying` bit(1) DEFAULT NULL,
  `degree` varchar(255) NOT NULL,
  `end_year` int DEFAULT NULL,
  `field_of_study` varchar(255) DEFAULT NULL,
  `grade` varchar(255) DEFAULT NULL,
  `institution` varchar(255) NOT NULL,
  `start_year` int DEFAULT NULL,
  `profile_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKbufn19514tqheek69srnucok2` (`profile_id`),
  CONSTRAINT `FKbufn19514tqheek69srnucok2` FOREIGN KEY (`profile_id`) REFERENCES `profiles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `educations`
--

LOCK TABLES `educations` WRITE;
/*!40000 ALTER TABLE `educations` DISABLE KEYS */;
/*!40000 ALTER TABLE `educations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `freelancers`
--

DROP TABLE IF EXISTS `freelancers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `freelancers` (
  `hourly_rate` double DEFAULT NULL,
  `summary` varchar(1000) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `id` bigint NOT NULL,
  `profile_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK470qv7wgaub5p7e680p58n2ef` (`profile_id`),
  CONSTRAINT `FK1wmvix35elgln1nm9nxof37k` FOREIGN KEY (`profile_id`) REFERENCES `profiles` (`id`),
  CONSTRAINT `FKh6tivt91b7fy16jmrj8vexnjo` FOREIGN KEY (`id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `freelancers`
--

LOCK TABLES `freelancers` WRITE;
/*!40000 ALTER TABLE `freelancers` DISABLE KEYS */;
INSERT INTO `freelancers` VALUES (350,'Passionate full stack developer with 8+ years of experience building scalable web applications. Specialized in Angular, React, Node.js, and cloud technologies.','Senior Full Stack Developer',4,1),(280,'Creative designer with a passion for crafting beautiful and intuitive user experiences. 6 years of experience in product design and branding.','UI/UX Designer',5,2),(300,'Mobile developer specializing in React Native and Flutter. Built apps with millions of downloads. Focus on performance and exceptional UX.','Mobile App Developer',6,3),(400,'DevOps specialist with expertise in cloud infrastructure, CI/CD pipelines, and containerization. Helping teams ship faster and more reliably.','DevOps Engineer',7,4),(320,'Backend specialist with deep expertise in Java, Spring Boot, and microservices architecture. Building reliable and scalable systems.','Backend Developer',8,5),(0,'','',9,NULL),(200,'hello y\'all','The web Dev',10,6);
/*!40000 ALTER TABLE `freelancers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `freelancers_skills`
--

DROP TABLE IF EXISTS `freelancers_skills`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `freelancers_skills` (
  `freelancers_id` bigint NOT NULL,
  `skills_id` bigint NOT NULL,
  KEY `FK9y3lr0tmtmsese7k54dcf8dq1` (`skills_id`),
  KEY `FK34dcunpskx41ctdugyyyrc4dj` (`freelancers_id`),
  CONSTRAINT `FK34dcunpskx41ctdugyyyrc4dj` FOREIGN KEY (`freelancers_id`) REFERENCES `freelancers` (`id`),
  CONSTRAINT `FK9y3lr0tmtmsese7k54dcf8dq1` FOREIGN KEY (`skills_id`) REFERENCES `skills` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `freelancers_skills`
--

LOCK TABLES `freelancers_skills` WRITE;
/*!40000 ALTER TABLE `freelancers_skills` DISABLE KEYS */;
INSERT INTO `freelancers_skills` VALUES (4,1),(4,2),(4,3),(4,4),(4,10),(4,12),(4,14),(4,15),(5,5),(5,6),(5,7),(6,8),(6,9),(6,10),(7,14),(7,15),(7,4),(8,3),(8,13),(8,15),(8,14);
/*!40000 ALTER TABLE `freelancers_skills` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `missions`
--

DROP TABLE IF EXISTS `missions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `missions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `budget` double DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `deadline` datetime(6) DEFAULT NULL,
  `description` varchar(2000) DEFAULT NULL,
  `status` enum('CLOSED','COMPLETED','IN_PROGRESS','OPEN') DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `type` enum('FIXED','HOURLY') DEFAULT NULL,
  `recruiter_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2y5ftljrv434dfugbfk7uve4j` (`recruiter_id`),
  CONSTRAINT `FK2y5ftljrv434dfugbfk7uve4j` FOREIGN KEY (`recruiter_id`) REFERENCES `recruiters` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `missions`
--

LOCK TABLES `missions` WRITE;
/*!40000 ALTER TABLE `missions` DISABLE KEYS */;
INSERT INTO `missions` VALUES (1,25000,'2025-12-30 15:29:47.836927','2026-02-13 15:29:47.836927','We need a full-stack developer to build a modern e-commerce platform with Angular frontend and Spring Boot backend. Features include product catalog, shopping cart, payment integration, and admin dashboard.','OPEN','Build E-commerce Website','FIXED',1),(2,12000,'2025-12-30 15:29:47.845290','2026-01-20 15:29:47.845290','Looking for a talented UI/UX designer to create wireframes, mockups, and final designs for a fitness tracking mobile app. Must include user research and prototyping.','OPEN','Mobile App UI/UX Design','FIXED',2),(3,35000,'2025-12-30 15:29:47.853280','2026-02-28 15:29:47.853280','Join our team to develop a cutting-edge fintech mobile application. Experience with payment integrations and security best practices required.','OPEN','React Native Developer for Fintech App','FIXED',3),(4,40000,'2025-12-30 15:29:47.862966','2026-03-30 15:29:47.862966','Migrate our on-premise infrastructure to AWS. Includes setting up VPC, EC2, RDS, and implementing CI/CD pipelines with proper monitoring.','OPEN','AWS Cloud Migration','FIXED',1),(5,30000,'2025-12-30 15:29:47.877831','2026-03-15 15:29:47.877831','Build a project management tool with real-time collaboration features. Tech stack: React, Node.js, MongoDB, WebSockets.','OPEN','Full Stack Web Application','FIXED',2),(6,18000,'2025-12-30 15:29:47.887256','2026-01-29 15:29:47.887256','Develop RESTful APIs for our mobile application. Must include authentication, rate limiting, and comprehensive documentation.','OPEN','Backend API Development','HOURLY',3);
/*!40000 ALTER TABLE `missions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `missions_required_skills`
--

DROP TABLE IF EXISTS `missions_required_skills`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `missions_required_skills` (
  `missions_id` bigint NOT NULL,
  `required_skills_id` bigint NOT NULL,
  KEY `FKsthshmf2lsiny33m6vj4uplq0` (`required_skills_id`),
  KEY `FKsluewynyyr433latug4x0cjn0` (`missions_id`),
  CONSTRAINT `FKsluewynyyr433latug4x0cjn0` FOREIGN KEY (`missions_id`) REFERENCES `missions` (`id`),
  CONSTRAINT `FKsthshmf2lsiny33m6vj4uplq0` FOREIGN KEY (`required_skills_id`) REFERENCES `skills` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `missions_required_skills`
--

LOCK TABLES `missions_required_skills` WRITE;
/*!40000 ALTER TABLE `missions_required_skills` DISABLE KEYS */;
INSERT INTO `missions_required_skills` VALUES (1,1),(1,3),(1,13),(2,5),(2,6),(2,7),(3,8),(3,10),(3,4),(4,14),(4,15),(5,2),(5,4),(5,12),(6,3),(6,13),(6,15);
/*!40000 ALTER TABLE `missions_required_skills` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `portfolios`
--

DROP TABLE IF EXISTS `portfolios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `portfolios` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(500) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `project_url` varchar(255) DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `freelancer_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKrwkkyo5u3kletxj4chwsl6o50` (`freelancer_id`),
  CONSTRAINT `FKrwkkyo5u3kletxj4chwsl6o50` FOREIGN KEY (`freelancer_id`) REFERENCES `freelancers` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `portfolios`
--

LOCK TABLES `portfolios` WRITE;
/*!40000 ALTER TABLE `portfolios` DISABLE KEYS */;
INSERT INTO `portfolios` VALUES (1,'Full-featured online store with payment integration and inventory management','https://images.pexels.com/photos/230544/pexels-photo-230544.jpeg?auto=compress&cs=tinysrgb&w=400&h=300&fit=crop','https://github.com/karim/ecommerce','E-commerce Platform',4),(2,'Telemedicine platform for remote consultations with video calling','https://images.pexels.com/photos/40568/medical-appointment-doctor-healthcare-40568.jpeg?auto=compress&cs=tinysrgb&w=400&h=300&fit=crop','https://github.com/karim/healthcare','Healthcare App',4),(3,'Real-time analytics dashboard for fintech with interactive charts','https://images.pexels.com/photos/186461/pexels-photo-186461.jpeg?auto=compress&cs=tinysrgb&w=400&h=300&fit=crop','https://github.com/karim/finance-dash','Finance Dashboard',4),(4,'Modern mobile banking experience with improved UX','https://images.pexels.com/photos/50987/money-card-business-credit-card-50987.jpeg?auto=compress&cs=tinysrgb&w=400&h=300&fit=crop','https://dribbble.com/leila/banking','Banking App Redesign',5),(5,'Complex analytics made simple with intuitive design','https://images.pexels.com/photos/669615/pexels-photo-669615.jpeg?auto=compress&cs=tinysrgb&w=400&h=300&fit=crop','https://dribbble.com/leila/saas','SaaS Dashboard',5),(6,'Health and workout companion app with 500K+ downloads','https://images.pexels.com/photos/841130/pexels-photo-841130.jpeg?auto=compress&cs=tinysrgb&w=400&h=300&fit=crop','https://github.com/hassan/fitness','Fitness Tracker',6),(7,'Complete delivery solution with real-time tracking','https://images.pexels.com/photos/1640777/pexels-photo-1640777.jpeg?auto=compress&cs=tinysrgb&w=400&h=300&fit=crop','https://github.com/hassan/delivery','Food Delivery App',6),(8,'Enterprise AWS migration reducing costs by 40%','https://images.pexels.com/photos/1148820/pexels-photo-1148820.jpeg?auto=compress&cs=tinysrgb&w=400&h=300&fit=crop','https://github.com/amina/cloud-migration','Cloud Migration',7),(9,'Secure payment processing system handling 1M+ transactions','https://images.pexels.com/photos/4386431/pexels-photo-4386431.jpeg?auto=compress&cs=tinysrgb&w=400&h=300&fit=crop','https://github.com/yassine/payment-gateway','Payment Gateway',8),(10,'RESTful API platform with comprehensive documentation','https://images.pexels.com/photos/546819/pexels-photo-546819.jpeg?auto=compress&cs=tinysrgb&w=400&h=300&fit=crop','https://github.com/yassine/api-platform','API Platform',8),(11,'Cool project','https://tse4.mm.bing.net/th/id/OIP.U86KzUwi77p9vIMyG2CfwwHaHa?rs=1&pid=ImgDetMain&o=7&rm=3','','Calorie Tracker',10);
/*!40000 ALTER TABLE `portfolios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `profiles`
--

DROP TABLE IF EXISTS `profiles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `profiles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `bio` varchar(1000) NOT NULL,
  `experience_level` enum('JUNIOR','MID','SENIOR') DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `profile_picture` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `profiles`
--

LOCK TABLES `profiles` WRITE;
/*!40000 ALTER TABLE `profiles` DISABLE KEYS */;
INSERT INTO `profiles` VALUES (1,'I love turning complex problems into elegant solutions. With 8+ years in the industry, I\'ve delivered projects for startups and enterprises alikeee','JUNIOR','Casablanca','https://randomuser.me/api/portraits/men/1.jpg'),(2,'I believe great design solves problems. My approach combines aesthetics with usability to create memorable digital experiences.','MID','Rabat','https://randomuser.me/api/portraits/women/2.jpg'),(3,'Creating mobile experiences that users love. I focus on clean code, smooth animations, and intuitive interfaces.','MID','Marrakech','https://randomuser.me/api/portraits/men/3.jpg'),(4,'Automating everything that can be automated. I help teams build robust deployment pipelines and scalable infrastructure.','SENIOR','Casablanca','https://randomuser.me/api/portraits/women/4.jpg'),(5,'Clean code advocate. I design and build backend systems that are maintainable, testable, and performant.','SENIOR','Fes','https://randomuser.me/api/portraits/men/5.jpg'),(6,'Hello i\'m Mimi','JUNIOR','Casablanca','https://randomuser.me/api/portraits/men/9.jpg');
/*!40000 ALTER TABLE `profiles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `profiles_skills`
--

DROP TABLE IF EXISTS `profiles_skills`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `profiles_skills` (
  `profile_id` bigint NOT NULL,
  `skills_id` bigint NOT NULL,
  KEY `FKgfem29yg06exfm6a8t4m9pppr` (`skills_id`),
  KEY `FKemh561y7tw9pya5qdpi3ssvhi` (`profile_id`),
  CONSTRAINT `FKemh561y7tw9pya5qdpi3ssvhi` FOREIGN KEY (`profile_id`) REFERENCES `profiles` (`id`),
  CONSTRAINT `FKgfem29yg06exfm6a8t4m9pppr` FOREIGN KEY (`skills_id`) REFERENCES `skills` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `profiles_skills`
--

LOCK TABLES `profiles_skills` WRITE;
/*!40000 ALTER TABLE `profiles_skills` DISABLE KEYS */;
/*!40000 ALTER TABLE `profiles_skills` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recruiters`
--

DROP TABLE IF EXISTS `recruiters`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recruiters` (
  `company_name` varchar(255) DEFAULT NULL,
  `company_website` varchar(255) DEFAULT NULL,
  `id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK97pqd4rhhbppy5y4i70umm42s` FOREIGN KEY (`id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recruiters`
--

LOCK TABLES `recruiters` WRITE;
/*!40000 ALTER TABLE `recruiters` DISABLE KEYS */;
INSERT INTO `recruiters` VALUES ('TechMorocco','https://techmorocco.com',1),('StartupHub','https://startuphub.ma',2),('DigitalMaroc','https://digitalmaroc.com',3);
/*!40000 ALTER TABLE `recruiters` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reviews`
--

DROP TABLE IF EXISTS `reviews`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reviews` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `author_name` varchar(255) DEFAULT NULL,
  `comment` varchar(2000) DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `rating` int NOT NULL,
  `freelancer_id` bigint NOT NULL,
  `mission_id` bigint DEFAULT NULL,
  `recruiter_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKg7u7njwblw1kq18fd1qt7ijrm` (`freelancer_id`),
  KEY `FKc9xwx05jb9tnybs05j8flu2j8` (`mission_id`),
  KEY `FKqv0epgy6u689a4njv7c5lxwid` (`recruiter_id`),
  CONSTRAINT `FKc9xwx05jb9tnybs05j8flu2j8` FOREIGN KEY (`mission_id`) REFERENCES `missions` (`id`),
  CONSTRAINT `FKg7u7njwblw1kq18fd1qt7ijrm` FOREIGN KEY (`freelancer_id`) REFERENCES `freelancers` (`id`),
  CONSTRAINT `FKqv0epgy6u689a4njv7c5lxwid` FOREIGN KEY (`recruiter_id`) REFERENCES `recruiters` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reviews`
--

LOCK TABLES `reviews` WRITE;
/*!40000 ALTER TABLE `reviews` DISABLE KEYS */;
INSERT INTO `reviews` VALUES (1,'Youssef El Amrani','Karim delivered an exceptional e-commerce platform. His technical skills are outstanding and communication was excellent throughout the project. Highly recommended!','2025-11-30 15:29:47.894392',5,4,NULL,1),(2,'Fatima Benali','Working with Karim was a pleasure. He understood our requirements perfectly and delivered ahead of schedule. The code quality was impeccable <3','2025-10-31 15:29:47.900633',5,4,NULL,2),(3,'Omar Tazi','Great developer with strong problem-solving skills. Minor delays but overall excellent work on our dashboard project.','2025-10-01 15:29:47.904942',4,4,NULL,3),(4,'Fatima Benali','Leila\'s design work transformed our app completely. Her attention to detail and understanding of user psychology is remarkable.','2025-12-15 15:29:47.908638',5,5,NULL,2),(5,'Omar Tazi','Incredible designer! She delivered beautiful mockups that our users absolutely love. Will definitely work with her again.','2025-11-15 15:29:47.913657',5,5,NULL,3),(6,'Youssef El Amrani','Hassan built us an amazing mobile app. The performance is fantastic and users have rated it 4.8 stars on the app store!','2025-12-10 15:29:47.918365',5,6,NULL,1),(7,'Fatima Benali','Solid mobile developer. Good communication and delivered a quality app. Would recommend for mobile projects.','2025-10-16 15:29:47.923075',4,6,NULL,2),(8,'Youssef El Amrani','Amina migrated our entire infrastructure to AWS flawlessly. She reduced our cloud costs by 40% and improved deployment speed significantly.','2025-12-20 15:29:47.927102',5,7,NULL,1),(9,'Omar Tazi','Expert DevOps engineer. Set up our CI/CD pipeline and now deployments that took hours are done in minutes. Highly skilled!','2025-11-20 15:29:47.931253',5,7,NULL,3),(10,'Omar Tazi','Yassine built a robust payment gateway that handles millions of transactions. His code is clean and well-documented. Excellent work!','2025-12-05 15:29:47.935394',5,8,NULL,3),(11,'Fatima Benali','Strong backend developer with excellent knowledge of Spring Boot. Delivered a scalable API platform for our startup.','2025-11-05 15:29:47.940451',4,8,NULL,2);
/*!40000 ALTER TABLE `reviews` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `skills`
--

DROP TABLE IF EXISTS `skills`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `skills` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `category` enum('ADMIN_SUPPORT','AI_ML','BLOCKCHAIN','BUSINESS','CUSTOMER_SERVICE','DATA_SCIENCE','DESIGN','DEVOPS','ENGINEERING','FINANCE','GAME_DEV','LEGAL','MARKETING','MOBILE','PROGRAMMING','WRITING') DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK85woe63nu9klkk9fa73vf0jd0` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `skills`
--

LOCK TABLES `skills` WRITE;
/*!40000 ALTER TABLE `skills` DISABLE KEYS */;
INSERT INTO `skills` VALUES (1,'PROGRAMMING','Frontend framework for building web applications','Angular'),(2,'PROGRAMMING','JavaScript library for building user interfaces','React'),(3,'PROGRAMMING','Java framework for building backend applications','Spring Boot'),(4,'PROGRAMMING','JavaScript runtime for server-side development','Node.js'),(5,'DESIGN','Collaborative design tool for UI/UX','Figma'),(6,'DESIGN','User interface design principles and practices','UI Design'),(7,'DESIGN','User experience research and testing','UX Research'),(8,'MOBILE','Framework for building mobile apps','React Native'),(9,'MOBILE','Google\'s UI toolkit for mobile development','Flutter'),(10,'PROGRAMMING','Typed superset of JavaScript','TypeScript'),(11,'DATA_SCIENCE','Subset of machine learning with neural networks','Deep Learning'),(12,'DATA_SCIENCE','NoSQL database for modern applications','MongoDB'),(13,'DATA_SCIENCE','Advanced open-source relational database','PostgreSQL'),(14,'DEVOPS','Amazon Web Services cloud platform','AWS'),(15,'DEVOPS','Containerization platform','Docker'),(16,'DATA_SCIENCE','Structured Query Language for database management','SQL'),(17,'DATA_SCIENCE','Algorithms that improve automatically through experience','Machine Learning');
/*!40000 ALTER TABLE `skills` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `last_name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `role` enum('FREELANCER','RECRUITER') DEFAULT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`),
  UNIQUE KEY `UKr43af9ap4edm43mmtq01oddj6` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'2025-12-30 15:29:46.930449','youssef@techmorocco.com','Youssef',_binary '','El Amrani','$2a$10$AyYuVtC680kMIKvebIh1d.VZJF4UCMyrm/felPZIY.ipGXpkwvJmS','+212661234567','RECRUITER','youssef_tech'),(2,'2025-12-30 15:29:47.026806','fatima@startuphub.ma','Fatima',_binary '','Benali','$2a$10$Pc2GqVS.fcai2j1iOOarQeO7lmdUcg5OVkNxIC8RZj3KQDf28oImW','+212662345678','RECRUITER','fatima_startup'),(3,'2025-12-30 15:29:47.135257','omar@digitalmaroc.com','Omar',_binary '','Tazi','$2a$10$z4HQNi3snOR8kSOJQGGfZOWvx/gv9N2cGknb/zVushNdUOc4vR8C2','+212663456789','RECRUITER','omar_digital'),(4,'2025-12-30 15:29:47.218206','karim@devpro.ma','Karim',_binary '','Benjelloun','$2a$10$Z4ncTjNuNvHRkpytfBcH7.7tuQMch3.SkWrMmClYjL3L0KWFNXu4K','+212664567890','FREELANCER','karim_dev'),(5,'2025-12-30 15:29:47.340369','leila@designstudio.ma','Leila',_binary '','Mansouri','$2a$10$qjzwGljEDB.a3zBgyxWBKupQYUQAUb93POSRsR051FoTFJwzbBAa2','+212665678901','FREELANCER','leila_design'),(6,'2025-12-30 15:29:47.452715','hassan@mobiledev.ma','Hassan',_binary '','El Amrani','$2a$10$1OZiGxlVlw2Kct5PWjUw5.IY.vRubNigqHpBY7unEujzGSUjA9c/O','+212666789012','FREELANCER','hassan_mobile'),(7,'2025-12-30 15:29:47.571831','amina@cloudpro.ma','Amina',_binary '','Berrada','$2a$10$mpUpRZ8WcFnki2.rNjv71.GOrAMBzHtFuM2CnxAoemDHSur1aUj4a','+212667890123','FREELANCER','amina_devops'),(8,'2025-12-30 15:29:47.708882','yassine@backend.ma','Yassine',_binary '','Cherkaoui','$2a$10$Fi0nmZ8Arj5y7KwSwY.BIewWpo/5PHwIe..RqhMzMaQ06RkytH856','+212668901234','FREELANCER','yassine_backend'),(9,'2026-01-08 16:14:13.531597','mouftahnouhaila@gmail.com','nouha',_binary '','','$2a$10$whu9KKZz/sL3A2UnW6vtAOoHiNonJihKMpZ/1uRn.V2JbWlTxHX7G','0645891665','FREELANCER','mouftahnouhaila@gmail.com'),(10,'2026-01-09 01:09:07.979708','mimith@gmail.com','mimi',_binary '','lilith','$2a$10$TQSKjKAfD3fRrlQogB5zgO7sNzLh18PZcqz5SnIyEI4wjwIkKmzCG','0584848481','FREELANCER','mimith@gmail.com');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `work_experiences`
--

DROP TABLE IF EXISTS `work_experiences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `work_experiences` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `company` varchar(255) NOT NULL,
  `currently_working` bit(1) DEFAULT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `position` varchar(255) NOT NULL,
  `start_date` date NOT NULL,
  `profile_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKd0iu9htbtwg3i8j4krg5boc0e` (`profile_id`),
  CONSTRAINT `FKd0iu9htbtwg3i8j4krg5boc0e` FOREIGN KEY (`profile_id`) REFERENCES `profiles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `work_experiences`
--

LOCK TABLES `work_experiences` WRITE;
/*!40000 ALTER TABLE `work_experiences` DISABLE KEYS */;
/*!40000 ALTER TABLE `work_experiences` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-09 10:35:53
