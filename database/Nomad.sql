-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: nomad
-- ------------------------------------------------------
-- Server version	8.0.43

CREATE DATABASE IF NOT EXISTS nomad;
USE nomad;

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
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
  `estimated_days` int DEFAULT NULL,
  `proposed_budget` double DEFAULT NULL,
  `applied_date` datetime(6) DEFAULT NULL,
  `freelancer_id` bigint NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `mission_id` bigint NOT NULL,
  `updated_date` datetime(6) DEFAULT NULL,
  `cover_letter` varchar(2000) DEFAULT NULL,
  `additional_notes` varchar(255) DEFAULT NULL,
  `status` enum('ACCEPTED','PENDING','REJECTED','WITHDRAWN') DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKb6j18geningekvlxyamtv2b1e` (`freelancer_id`),
  KEY `FKora3gbldr6agr5t89ggu1n4rh` (`mission_id`),
  CONSTRAINT `FKb6j18geningekvlxyamtv2b1e` FOREIGN KEY (`freelancer_id`) REFERENCES `freelancers` (`id`),
  CONSTRAINT `FKora3gbldr6agr5t89ggu1n4rh` FOREIGN KEY (`mission_id`) REFERENCES `missions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `applications`
--

LOCK TABLES `applications` WRITE;
/*!40000 ALTER TABLE `applications` DISABLE KEYS */;
/*!40000 ALTER TABLE `applications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contracts`
--

DROP TABLE IF EXISTS `contracts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contracts` (
  `budget` double DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `end_date` datetime(6) DEFAULT NULL,
  `freelancer_id` bigint NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `mission_id` bigint DEFAULT NULL,
  `recruiter_id` bigint NOT NULL,
  `start_date` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `title` varchar(500) DEFAULT NULL,
  `description` varchar(2000) DEFAULT NULL,
  `status` enum('ACCEPTED','COMPLETED','IN_PROGRESS','REJECTED') DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKg6n9tbd4t6j2mnlnua0r7wthw` (`freelancer_id`),
  KEY `FKt23hb6gw7nx45dbbfxwkierf8` (`mission_id`),
  KEY `FK2o6l6kotcujm6wjvtfoc15jod` (`recruiter_id`),
  CONSTRAINT `FK2o6l6kotcujm6wjvtfoc15jod` FOREIGN KEY (`recruiter_id`) REFERENCES `recruiters` (`id`),
  CONSTRAINT `FKg6n9tbd4t6j2mnlnua0r7wthw` FOREIGN KEY (`freelancer_id`) REFERENCES `freelancers` (`id`),
  CONSTRAINT `FKt23hb6gw7nx45dbbfxwkierf8` FOREIGN KEY (`mission_id`) REFERENCES `missions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contracts`
--

LOCK TABLES `contracts` WRITE;
/*!40000 ALTER TABLE `contracts` DISABLE KEYS */;
/*!40000 ALTER TABLE `contracts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `freelancers`
--

DROP TABLE IF EXISTS `freelancers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `freelancers` (
  `hourly_rate` double DEFAULT NULL,
  `id` bigint NOT NULL,
  `profile_id` bigint DEFAULT NULL,
  `summary` varchar(1000) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
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
INSERT INTO `freelancers` VALUES (350,4,1,'Passionate full stack developer with 8+ years of experience building scalable web applications. Specialized in Angular, React, Node.js, and cloud technologies.','Senior Full Stack Developer'),(280,5,2,'Creative designer with a passion for crafting beautiful and intuitive user experiences. 6 years of experience in product design and branding.','UI/UX Designer'),(300,6,3,'Mobile developer specializing in React Native and Flutter. Built apps with millions of downloads. Focus on performance and exceptional UX.','Mobile App Developer'),(400,7,4,'DevOps specialist with expertise in cloud infrastructure, CI/CD pipelines, and containerization. Helping teams ship faster and more reliably.','DevOps Engineer'),(320,8,5,'Backend specialist with deep expertise in Java, Spring Boot, and microservices architecture. Building reliable and scalable systems.','Backend Developer');
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
  `budget` double DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `deadline` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `recruiter_id` bigint DEFAULT NULL,
  `description` varchar(2000) DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `status` enum('CLOSED','COMPLETED','IN_PROGRESS','OPEN') DEFAULT NULL,
  `type` enum('FIXED','HOURLY') DEFAULT NULL,
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
INSERT INTO `missions` VALUES (25000,'2026-01-09 16:37:56.265351','2026-02-23 16:37:56.265351',1,1,'We need a full-stack developer to build a modern e-commerce platform with Angular frontend and Spring Boot backend. Features include product catalog, shopping cart, payment integration, and admin dashboard.','Build E-commerce Website','OPEN','FIXED'),(12000,'2026-01-09 16:37:56.277430','2026-01-30 16:37:56.277430',2,2,'Looking for a talented UI/UX designer to create wireframes, mockups, and final designs for a fitness tracking mobile app. Must include user research and prototyping.','Mobile App UI/UX Design','OPEN','FIXED'),(35000,'2026-01-09 16:37:56.286991','2026-03-10 16:37:56.286991',3,3,'Join our team to develop a cutting-edge fintech mobile application. Experience with payment integrations and security best practices required.','React Native Developer for Fintech App','OPEN','FIXED'),(40000,'2026-01-09 16:37:56.296902','2026-04-09 16:37:56.297904',4,1,'Migrate our on-premise infrastructure to AWS. Includes setting up VPC, EC2, RDS, and implementing CI/CD pipelines with proper monitoring.','AWS Cloud Migration','OPEN','FIXED'),(30000,'2026-01-09 16:37:56.306075','2026-03-25 16:37:56.306075',5,2,'Build a project management tool with real-time collaboration features. Tech stack: React, Node.js, MongoDB, WebSockets.','Full Stack Web Application','OPEN','FIXED'),(18000,'2026-01-09 16:37:56.315644','2026-02-08 16:37:56.315644',6,3,'Develop RESTful APIs for our mobile application. Must include authentication, rate limiting, and comprehensive documentation.','Backend API Development','OPEN','HOURLY');
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
  `freelancer_id` bigint DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(500) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `project_url` varchar(255) DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKrwkkyo5u3kletxj4chwsl6o50` (`freelancer_id`),
  CONSTRAINT `FKrwkkyo5u3kletxj4chwsl6o50` FOREIGN KEY (`freelancer_id`) REFERENCES `freelancers` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `portfolios`
--

LOCK TABLES `portfolios` WRITE;
/*!40000 ALTER TABLE `portfolios` DISABLE KEYS */;
INSERT INTO `portfolios` VALUES (4,1,'Full-featured online store with payment integration and inventory management','https://images.pexels.com/photos/230544/pexels-photo-230544.jpeg?auto=compress&cs=tinysrgb&w=400&h=300&fit=crop','https://github.com/karim/ecommerce','E-commerce Platform'),(4,2,'Telemedicine platform for remote consultations with video calling','https://images.pexels.com/photos/40568/medical-appointment-doctor-healthcare-40568.jpeg?auto=compress&cs=tinysrgb&w=400&h=300&fit=crop','https://github.com/karim/healthcare','Healthcare App'),(4,3,'Real-time analytics dashboard for fintech with interactive charts','https://images.pexels.com/photos/186461/pexels-photo-186461.jpeg?auto=compress&cs=tinysrgb&w=400&h=300&fit=crop','https://github.com/karim/finance-dash','Finance Dashboard'),(5,4,'Modern mobile banking experience with improved UX','https://images.pexels.com/photos/50987/money-card-business-credit-card-50987.jpeg?auto=compress&cs=tinysrgb&w=400&h=300&fit=crop','https://dribbble.com/leila/banking','Banking App Redesign'),(5,5,'Complex analytics made simple with intuitive design','https://images.pexels.com/photos/669615/pexels-photo-669615.jpeg?auto=compress&cs=tinysrgb&w=400&h=300&fit=crop','https://dribbble.com/leila/saas','SaaS Dashboard'),(6,6,'Health and workout companion app with 500K+ downloads','https://images.pexels.com/photos/841130/pexels-photo-841130.jpeg?auto=compress&cs=tinysrgb&w=400&h=300&fit=crop','https://github.com/hassan/fitness','Fitness Tracker'),(6,7,'Complete delivery solution with real-time tracking','https://images.pexels.com/photos/1640777/pexels-photo-1640777.jpeg?auto=compress&cs=tinysrgb&w=400&h=300&fit=crop','https://github.com/hassan/delivery','Food Delivery App'),(7,8,'Enterprise AWS migration reducing costs by 40%','https://images.pexels.com/photos/1148820/pexels-photo-1148820.jpeg?auto=compress&cs=tinysrgb&w=400&h=300&fit=crop','https://github.com/amina/cloud-migration','Cloud Migration'),(8,9,'Secure payment processing system handling 1M+ transactions','https://images.pexels.com/photos/4386431/pexels-photo-4386431.jpeg?auto=compress&cs=tinysrgb&w=400&h=300&fit=crop','https://github.com/yassine/payment-gateway','Payment Gateway'),(8,10,'RESTful API platform with comprehensive documentation','https://images.pexels.com/photos/546819/pexels-photo-546819.jpeg?auto=compress&cs=tinysrgb&w=400&h=300&fit=crop','https://github.com/yassine/api-platform','API Platform');
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
  `bio` varchar(1000) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `profile_picture` varchar(255) DEFAULT NULL,
  `experience_level` enum('JUNIOR','MID','SENIOR') DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `profiles`
--

LOCK TABLES `profiles` WRITE;
/*!40000 ALTER TABLE `profiles` DISABLE KEYS */;
INSERT INTO `profiles` VALUES (1,'I love turning complex problems into elegant solutions. With 8+ years in the industry, I\'ve delivered projects for startups and enterprises alike.','Casablanca','https://randomuser.me/api/portraits/men/1.jpg','SENIOR'),(2,'I believe great design solves problems. My approach combines aesthetics with usability to create memorable digital experiences.','Rabat','https://randomuser.me/api/portraits/women/2.jpg','MID'),(3,'Creating mobile experiences that users love. I focus on clean code, smooth animations, and intuitive interfaces.','Marrakech','https://randomuser.me/api/portraits/men/3.jpg','MID'),(4,'Automating everything that can be automated. I help teams build robust deployment pipelines and scalable infrastructure.','Casablanca','https://randomuser.me/api/portraits/women/4.jpg','SENIOR'),(5,'Clean code advocate. I design and build backend systems that are maintainable, testable, and performant.','Fes','https://randomuser.me/api/portraits/men/5.jpg','SENIOR');
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
  `id` bigint NOT NULL,
  `company_name` varchar(255) DEFAULT NULL,
  `company_website` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK97pqd4rhhbppy5y4i70umm42s` FOREIGN KEY (`id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recruiters`
--

LOCK TABLES `recruiters` WRITE;
/*!40000 ALTER TABLE `recruiters` DISABLE KEYS */;
INSERT INTO `recruiters` VALUES (1,'TechMorocco','https://techmorocco.com'),(2,'StartupHub','https://startuphub.ma'),(3,'DigitalMaroc','https://digitalmaroc.com');
/*!40000 ALTER TABLE `recruiters` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reviews`
--

DROP TABLE IF EXISTS `reviews`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reviews` (
  `rating` int NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `freelancer_id` bigint NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `mission_id` bigint DEFAULT NULL,
  `recruiter_id` bigint DEFAULT NULL,
  `comment` varchar(2000) DEFAULT NULL,
  `author_name` varchar(255) DEFAULT NULL,
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
INSERT INTO `reviews` VALUES (5,'2025-12-10 16:37:56.325209',4,1,NULL,1,'Karim delivered an exceptional e-commerce platform. His technical skills are outstanding and communication was excellent throughout the project. Highly recommended!','Youssef El Amrani'),(5,'2025-11-10 16:37:56.332629',4,2,NULL,2,'Working with Karim was a pleasure. He understood our requirements perfectly and delivered ahead of schedule. The code quality was impeccable.','Fatima Benali'),(4,'2025-10-11 16:37:56.337041',4,3,NULL,3,'Great developer with strong problem-solving skills. Minor delays but overall excellent work on our dashboard project.','Omar Tazi'),(5,'2025-12-25 16:37:56.342881',5,4,NULL,2,'Leila\'s design work transformed our app completely. Her attention to detail and understanding of user psychology is remarkable.','Fatima Benali'),(5,'2025-11-25 16:37:56.347515',5,5,NULL,3,'Incredible designer! She delivered beautiful mockups that our users absolutely love. Will definitely work with her again.','Omar Tazi'),(5,'2025-12-20 16:37:56.352840',6,6,NULL,1,'Hassan built us an amazing mobile app. The performance is fantastic and users have rated it 4.8 stars on the app store!','Youssef El Amrani'),(4,'2025-10-26 16:37:56.358553',6,7,NULL,2,'Solid mobile developer. Good communication and delivered a quality app. Would recommend for mobile projects.','Fatima Benali'),(5,'2025-12-30 16:37:56.363459',7,8,NULL,1,'Amina migrated our entire infrastructure to AWS flawlessly. She reduced our cloud costs by 40% and improved deployment speed significantly.','Youssef El Amrani'),(5,'2025-11-30 16:37:56.368362',7,9,NULL,3,'Expert DevOps engineer. Set up our CI/CD pipeline and now deployments that took hours are done in minutes. Highly skilled!','Omar Tazi'),(5,'2025-12-15 16:37:56.373499',8,10,NULL,3,'Yassine built a robust payment gateway that handles millions of transactions. His code is clean and well-documented. Excellent work!','Omar Tazi'),(4,'2025-11-15 16:37:56.378973',8,11,NULL,2,'Strong backend developer with excellent knowledge of Spring Boot. Delivered a scalable API platform for our startup.','Fatima Benali');
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
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `category` enum('ADMIN_SUPPORT','AI_ML','BLOCKCHAIN','BUSINESS','CUSTOMER_SERVICE','DATA_SCIENCE','DESIGN','DEVOPS','ENGINEERING','FINANCE','GAME_DEV','LEGAL','MARKETING','MOBILE','PROGRAMMING','WRITING') DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK85woe63nu9klkk9fa73vf0jd0` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `skills`
--

LOCK TABLES `skills` WRITE;
/*!40000 ALTER TABLE `skills` DISABLE KEYS */;
INSERT INTO `skills` VALUES (1,'Frontend framework for building web applications','Angular','PROGRAMMING'),(2,'JavaScript library for building user interfaces','React','PROGRAMMING'),(3,'Java framework for building backend applications','Spring Boot','PROGRAMMING'),(4,'JavaScript runtime for server-side development','Node.js','PROGRAMMING'),(5,'Collaborative design tool for UI/UX','Figma','DESIGN'),(6,'User interface design principles and practices','UI Design','DESIGN'),(7,'User experience research and testing','UX Research','DESIGN'),(8,'Framework for building mobile apps','React Native','MOBILE'),(9,'Google\'s UI toolkit for mobile development','Flutter','MOBILE'),(10,'Typed superset of JavaScript','TypeScript','PROGRAMMING'),(11,'Subset of machine learning with neural networks','Deep Learning','DATA_SCIENCE'),(12,'NoSQL database for modern applications','MongoDB','DATA_SCIENCE'),(13,'Advanced open-source relational database','PostgreSQL','DATA_SCIENCE'),(14,'Amazon Web Services cloud platform','AWS','DEVOPS'),(15,'Containerization platform','Docker','DEVOPS'),(16,'Structured Query Language for database management','SQL','DATA_SCIENCE'),(17,'Algorithms that improve automatically through experience','Machine Learning','DATA_SCIENCE');
/*!40000 ALTER TABLE `skills` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `is_active` bit(1) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `username` varchar(255) NOT NULL,
  `role` enum('FREELANCER','RECRUITER') DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`),
  UNIQUE KEY `UKr43af9ap4edm43mmtq01oddj6` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (_binary '','2026-01-09 16:37:55.442979',1,'youssef@techmorocco.com','Youssef','El Amrani','$2a$10$Yn9xc3hH4XLgt91A0aXyveA5SarijdmjJxDAV30.k7YP3I7Jo.gg6','+212661234567','youssef_tech','RECRUITER'),(_binary '','2026-01-09 16:37:55.546048',2,'fatima@startuphub.ma','Fatima','Benali','$2a$10$YBqYhCsvwBSOJ18WDNOGG.Dycki2XMX3GcbNbY6AbG0pcBBOym8Ju','+212662345678','fatima_startup','RECRUITER'),(_binary '','2026-01-09 16:37:55.629655',3,'omar@digitalmaroc.com','Omar','Tazi','$2a$10$v8Jxv85Um9mqmGQie4vHd.qfvhmukuTOI2cPwKERB9/bR66yvGynu','+212663456789','omar_digital','RECRUITER'),(_binary '','2026-01-09 16:37:55.714354',4,'karim@devpro.ma','Karim','Benjelloun','$2a$10$UTCDua8prXbEe/F.MR26jOmIgWQNEgahGJYS9WOrTmTDwnHlRd3LW','+212664567890','karim_dev','FREELANCER'),(_binary '','2026-01-09 16:37:55.832037',5,'leila@designstudio.ma','Leila','Mansouri','$2a$10$pCqqTeEDjd0D4jbTsfm/E.HGNQGzcrYY6ZbYKv8YeEl.pyi5d5jRu','+212665678901','leila_design','FREELANCER'),(_binary '','2026-01-09 16:37:55.933285',6,'hassan@mobiledev.ma','Hassan','El Amrani','$2a$10$TS78ufLxIAWbPIRTqotgNeRx8pvMQ.Dp4KIMcJmiZyJ91tVHauckK','+212666789012','hassan_mobile','FREELANCER'),(_binary '','2026-01-09 16:37:56.051954',7,'amina@cloudpro.ma','Amina','Berrada','$2a$10$jgrqCCm.bvmGCy1QXEwcGOP1B054r6hH7RxBNNM9nxaPTBrnarGU.','+212667890123','amina_devops','FREELANCER'),(_binary '','2026-01-09 16:37:56.150489',8,'yassine@backend.ma','Yassine','Cherkaoui','$2a$10$/JEKrmXT5jmMpgqRBTjzb.TW5p11m9PhCkGZrhkjDr87XnpCHAJwC','+212668901234','yassine_backend','FREELANCER');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-09 16:39:21
