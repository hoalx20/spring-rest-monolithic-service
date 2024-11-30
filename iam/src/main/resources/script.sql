-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: spring_monolithic_service_dev
-- ------------------------------------------------------
-- Server version	8.0.34

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */
;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */
;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */
;
/*!50503 SET NAMES utf8mb4 */
;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */
;
/*!40103 SET TIME_ZONE='+00:00' */
;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */
;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */
;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */
;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */
;

--
-- Table structure for table `privilege`
--

DROP TABLE IF EXISTS `privilege`;
/*!40101 SET @saved_cs_client     = @@character_set_client */
;
/*!50503 SET character_set_client = utf8mb4 */
;

CREATE TABLE `privilege` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `created` datetime(6) DEFAULT NULL,
    `description` varchar(255) NOT NULL,
    `name` varchar(255) NOT NULL,
    `updated` datetime(6) DEFAULT NULL,
    `deleted` bit(1) NOT NULL COMMENT 'Soft-delete indicator',
    PRIMARY KEY (`id`),
    UNIQUE KEY `UKh7iwbdg4ev8mgvmij76881tx8` (`name`)
) ENGINE = InnoDB AUTO_INCREMENT = 5 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */
;

--
-- Dumping data for table `privilege`
--

/*!40000 ALTER TABLE `privilege` DISABLE KEYS */
;

INSERT INTO
    `privilege`
VALUES (
        1,
        '2024-11-30 14:04:45.295968',
        'READ PRIVILEGE',
        'READ',
        '2024-11-30 14:04:45.295968',
        0x00
    ),
    (
        2,
        '2024-11-30 14:04:52.141204',
        'CREATE PRIVILEGE',
        'CREATE',
        '2024-11-30 14:04:52.141204',
        0x00
    ),
    (
        3,
        '2024-11-30 14:04:56.809583',
        'UPDATE PRIVILEGE',
        'UPDATE',
        '2024-11-30 14:04:56.809583',
        0x00
    ),
    (
        4,
        '2024-11-30 14:05:01.732173',
        'DELETE PRIVILEGE',
        'DELETE',
        '2024-11-30 14:05:01.732173',
        0x00
    );
/*!40000 ALTER TABLE `privilege` ENABLE KEYS */
;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */
;
/*!50503 SET character_set_client = utf8mb4 */
;

CREATE TABLE `role` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `created` datetime(6) DEFAULT NULL,
    `description` varchar(255) NOT NULL,
    `name` varchar(255) NOT NULL,
    `updated` datetime(6) DEFAULT NULL,
    `deleted` bit(1) NOT NULL COMMENT 'Soft-delete indicator',
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK8sewwnpamngi6b1dwaa88askk` (`name`)
) ENGINE = InnoDB AUTO_INCREMENT = 3 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */
;

--
-- Dumping data for table `role`
--

/*!40000 ALTER TABLE `role` DISABLE KEYS */
;

INSERT INTO
    `role`
VALUES (
        1,
        '2024-11-30 14:05:17.759790',
        'USER ROLE',
        'USER',
        '2024-11-30 14:05:17.760291',
        0x00
    ),
    (
        2,
        '2024-11-30 14:05:27.250023',
        'ADMIN ROLE',
        'ADMIN',
        '2024-11-30 14:05:27.250023',
        0x00
    );
/*!40000 ALTER TABLE `role` ENABLE KEYS */
;

--
-- Table structure for table `role_privileges`
--

DROP TABLE IF EXISTS `role_privileges`;
/*!40101 SET @saved_cs_client     = @@character_set_client */
;
/*!50503 SET character_set_client = utf8mb4 */
;

CREATE TABLE `role_privileges` (
    `active` int NOT NULL COMMENT 'Soft-delete indicator',
    `roles_id` bigint NOT NULL,
    `privileges_id` bigint NOT NULL,
    PRIMARY KEY (`roles_id`, `privileges_id`),
    KEY `FKas5s9i1itvr8tgocse4xmtwox` (`privileges_id`),
    CONSTRAINT `FK9n2w8s3aw0yk00s4nmqvucw6b` FOREIGN KEY (`roles_id`) REFERENCES `role` (`id`),
    CONSTRAINT `FKas5s9i1itvr8tgocse4xmtwox` FOREIGN KEY (`privileges_id`) REFERENCES `privilege` (`id`),
    CONSTRAINT `role_privileges_chk_1` CHECK ((`active` in (1, 0)))
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */
;

--
-- Dumping data for table `role_privileges`
--

/*!40000 ALTER TABLE `role_privileges` DISABLE KEYS */
;

INSERT INTO
    `role_privileges`
VALUES (1, 1, 1),
    (1, 2, 1),
    (1, 2, 2),
    (1, 2, 3),
    (1, 2, 4);
/*!40000 ALTER TABLE `role_privileges` ENABLE KEYS */
;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */
;
/*!50503 SET character_set_client = utf8mb4 */
;

CREATE TABLE `user` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `created` datetime(6) DEFAULT NULL,
    `password` varchar(255) NOT NULL,
    `updated` datetime(6) DEFAULT NULL,
    `username` varchar(255) NOT NULL,
    `deleted` bit(1) NOT NULL COMMENT 'Soft-delete indicator',
    PRIMARY KEY (`id`),
    UNIQUE KEY `UKsb8bbouer5wak8vyiiy4pf2bx` (`username`)
) ENGINE = InnoDB AUTO_INCREMENT = 3 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */
;

--
-- Dumping data for table `user`
--

/*!40000 ALTER TABLE `user` DISABLE KEYS */
;

INSERT INTO
    `user`
VALUES (
        1,
        '2024-11-30 14:05:54.487448',
        '$2a$10$VB221QnJ94FuIhuW2wN5Pu31ZJzSKT9gv6mfueaPhFHPR17Kv/hNO',
        '2024-11-30 14:05:54.487448',
        'hoalx1',
        0x00
    ),
    (
        2,
        '2024-11-30 14:06:22.065779',
        '$2a$10$w6FFlHF9ckouslfgCx7bWu2wt5jy.trRZI2wC/IWbCR1aGuwtXunC',
        '2024-11-30 14:06:22.065779',
        'hoalx2',
        0x00
    );
/*!40000 ALTER TABLE `user` ENABLE KEYS */
;

--
-- Table structure for table `user_roles`
--

DROP TABLE IF EXISTS `user_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */
;
/*!50503 SET character_set_client = utf8mb4 */
;

CREATE TABLE `user_roles` (
    `active` int NOT NULL COMMENT 'Soft-delete indicator',
    `users_id` bigint NOT NULL,
    `roles_id` bigint NOT NULL,
    PRIMARY KEY (`users_id`, `roles_id`),
    KEY `FKj9553ass9uctjrmh0gkqsmv0d` (`roles_id`),
    CONSTRAINT `FK7ecyobaa59vxkxckg6t355l86` FOREIGN KEY (`users_id`) REFERENCES `user` (`id`),
    CONSTRAINT `FKj9553ass9uctjrmh0gkqsmv0d` FOREIGN KEY (`roles_id`) REFERENCES `role` (`id`),
    CONSTRAINT `user_roles_chk_1` CHECK ((`active` in (1, 0)))
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */
;

--
-- Dumping data for table `user_roles`
--

/*!40000 ALTER TABLE `user_roles` DISABLE KEYS */
;

INSERT INTO `user_roles` VALUES (1, 1, 1), (1, 2, 2);
/*!40000 ALTER TABLE `user_roles` ENABLE KEYS */
;

--
-- Dumping routines for database 'spring_monolithic_service_dev'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */
;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */
;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */
;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */
;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */
;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */
;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */
;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */
;

--
-- Table structure for table `bad_credential`
--

DROP TABLE IF EXISTS `bad_credential`;
/*!40101 SET @saved_cs_client     = @@character_set_client */
;
/*!50503 SET character_set_client = utf8mb4 */
;

CREATE TABLE `bad_credential` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `access_token_expired_at` datetime(6) NOT NULL,
    `access_token_id` varchar(255) NOT NULL,
    `user_id` bigint NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */
;

--
-- Dumping data for table `bad_credential`
--

/*!40000 ALTER TABLE `bad_credential` DISABLE KEYS */
;
/*!40000 ALTER TABLE `bad_credential` ENABLE KEYS */
;

--
-- Table structure for table `device`
--

DROP TABLE IF EXISTS `device`;
/*!40101 SET @saved_cs_client     = @@character_set_client */
;
/*!50503 SET character_set_client = utf8mb4 */
;

CREATE TABLE `device` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `created` datetime(6) DEFAULT NULL,
    `ip_address` varchar(255) NOT NULL,
    `updated` datetime(6) DEFAULT NULL,
    `user_agent` varchar(255) NOT NULL,
    `deleted` bit(1) NOT NULL COMMENT 'Soft-delete indicator',
    `user_id` bigint DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FKk92m2qj36vn62ctp5pgbt4982` (`user_id`),
    CONSTRAINT `FKk92m2qj36vn62ctp5pgbt4982` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */
;

--
-- Dumping data for table `device`
--

/*!40000 ALTER TABLE `device` DISABLE KEYS */
;
/*!40000 ALTER TABLE `device` ENABLE KEYS */
;

--
-- Table structure for table `status`
--

DROP TABLE IF EXISTS `status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */
;
/*!50503 SET character_set_client = utf8mb4 */
;

CREATE TABLE `status` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `content` varchar(255) NOT NULL,
    `created` datetime(6) DEFAULT NULL,
    `updated` datetime(6) DEFAULT NULL,
    `deleted` bit(1) NOT NULL COMMENT 'Soft-delete indicator',
    `user_id` bigint DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK29jeo70r8smcmqnrsijb1i6hm` (`user_id`),
    CONSTRAINT `FKmpnqojxi5d6ijcnrmkc9dj2nf` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */
;

--
-- Dumping data for table `status`
--

/*!40000 ALTER TABLE `status` DISABLE KEYS */
;
/*!40000 ALTER TABLE `status` ENABLE KEYS */
;

-- Dump completed on 2024-11-30 14:10:27