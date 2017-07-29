-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Apr 27, 2016 at 10:28 PM
-- Server version: 10.1.10-MariaDB
-- PHP Version: 7.0.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `mystorage`
--

-- --------------------------------------------------------

--
-- Table structure for table `files`
--

CREATE TABLE `files` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `filename` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `timestamp` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `files`
--

INSERT INTO `files` (`id`, `user_id`, `filename`, `timestamp`) VALUES
(87, 1, '11219026_495465350652107_3990266273046946086_n.jpg', 1461084794812),
(88, 1, '12311259_958175694248158_2698570778020174810_n.jpg', 1460135841830),
(89, 1, '12313546_484742288376776_3546269592371639630_n.jpg', 1461092741831),
(90, 1, '12654248_502611769923161_5646186873740770491_n.jpg', 1461092727569),
(91, 1, '12985558_885935814886499_5706085289936625415_n.jpg', 1461081520540),
(92, 1, '13000191_861075557330730_7334099781856348490_n.jpg', 1461236840908),
(93, 1, '13001319_1116247795092850_8839294566140819849_n.jpg', 1461093917935),
(94, 1, '13010658_861673050604314_4913282917629219254_n.jpg', 1461076542217),
(95, 1, '13076656_1339718659377988_2266502189347024521_n.jpg', 1461431042523),
(96, 1, 'CTDT2012-Nhom2 K58-13012015.pdf', 1438180093177),
(97, 1, 'Eminem – 25 To Life .mp3', 1460814003883),
(98, 1, 'Eminem – 3 a.m. .mp3', 1460814005995),
(99, 1, 'info.txt', 1460885525167),
(100, 1, 'Katy Perry – Birthday .mp3', 1460814020608),
(101, 1, 'Katy Perry – By The Grace Of God .mp3', 1460814022719),
(102, 1, 'Katy Perry – Circle The Drain .mp3', 1460814024891),
(103, 1, 'Katy Perry – I Kissed A Girl .mp3', 1460813833571),
(104, 1, 'New Journal Document.jnt', 1460882992677),
(110, 1, 'yeahman.xlsx', 1461683275032),
(112, 1, 'Hello guy.txt', 1461697386048);

-- --------------------------------------------------------

--
-- Table structure for table `storages`
--

CREATE TABLE `storages` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `name` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `path` varchar(50) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `storages`
--

INSERT INTO `storages` (`id`, `user_id`, `name`, `path`) VALUES
(1, 1, '', 'E:\\GuyDrive');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `email` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `fullname` varchar(20) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `email`, `password`, `fullname`) VALUES
(1, 'ngoccuongbka94@gmail.com', 'cuong123', 'cuong nguyen ngoc');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `files`
--
ALTER TABLE `files`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `storages`
--
ALTER TABLE `storages`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `files`
--
ALTER TABLE `files`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=113;
--
-- AUTO_INCREMENT for table `storages`
--
ALTER TABLE `storages`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
