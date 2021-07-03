-- phpMyAdmin SQL Dump
-- version 5.0.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 03, 2021 at 01:22 PM
-- Server version: 10.4.16-MariaDB
-- PHP Version: 7.4.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ninja`
--

-- --------------------------------------------------------

--
-- Table structure for table `attempt_status`
--

CREATE TABLE `attempt_status` (
  `id` int(11) NOT NULL,
  `description` varchar(45) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `attempt_status`
--

INSERT INTO `attempt_status` (`id`, `description`) VALUES
(1, 'Game Over'),
(2, 'Level Complete'),
(3, 'New Highscore');

-- --------------------------------------------------------

--
-- Table structure for table `levels`
--

CREATE TABLE `levels` (
  `id` int(11) NOT NULL,
  `name` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `baseXP` int(11) NOT NULL,
  `killXP` int(11) NOT NULL,
  `baseScore` int(11) NOT NULL,
  `killScore` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `levels`
--

INSERT INTO `levels` (`id`, `name`, `baseXP`, `killXP`, `baseScore`, `killScore`) VALUES
(1, 'Level 1 - Awakening', 50, 10, 500, 100),
(2, 'Level 2 - Assault', 100, 20, 700, 150),
(3, 'Level 3 - Crimson Shadow', 200, 30, 1000, 200);

-- --------------------------------------------------------

--
-- Table structure for table `level_attempts`
--

CREATE TABLE `level_attempts` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `level_id` int(11) NOT NULL,
  `status_id` int(11) NOT NULL,
  `score` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `level_attempts`
--

INSERT INTO `level_attempts` (`id`, `user_id`, `level_id`, `status_id`, `score`) VALUES
(1, 2, 1, 2, 1100),
(2, 2, 2, 3, 1100);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `role` enum('player','admin') COLLATE utf8_unicode_ci NOT NULL DEFAULT 'player',
  `level1_score` int(11) NOT NULL DEFAULT 0,
  `level2_score` int(11) NOT NULL DEFAULT 0,
  `level3_score` int(11) NOT NULL DEFAULT 0,
  `experience` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `role`, `level1_score`, `level2_score`, `level3_score`, `experience`) VALUES
(1, 'Reza', 'testtest', 'admin', 1100, 0, 0, 310),
(2, 'marko', 'testtest', 'player', 1100, 1100, 2200, 2160),
(4, 'test', 'testtest', 'player', 700, 0, 0, 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `attempt_status`
--
ALTER TABLE `attempt_status`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `levels`
--
ALTER TABLE `levels`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `level_attempts`
--
ALTER TABLE `level_attempts`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_user_id` (`user_id`),
  ADD KEY `fk_level_id` (`level_id`),
  ADD KEY `fk_status_id` (`status_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `attempt_status`
--
ALTER TABLE `attempt_status`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `level_attempts`
--
ALTER TABLE `level_attempts`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `level_attempts`
--
ALTER TABLE `level_attempts`
  ADD CONSTRAINT `fk_level_id` FOREIGN KEY (`level_id`) REFERENCES `levels` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_status_id` FOREIGN KEY (`status_id`) REFERENCES `attempt_status` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
