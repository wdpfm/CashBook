-- phpMyAdmin SQL Dump
-- version 4.7.9
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: 2020-07-07 14:44:07
-- 服务器版本： 5.7.30-log
-- PHP Version: 7.2.31

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `api_buu_app`
--

-- --------------------------------------------------------

--
-- 表的结构 `backup`
--

CREATE TABLE `backup` (
  `uid` int(11) NOT NULL,
  `balance` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `backup`
--

INSERT INTO `backup` (`uid`, `balance`) VALUES
(1, '5000'),
(23, '395'),
(24, '553'),
(27, '100'),
(29, '5'),
(35, '190'),
(36, '0'),
(37, '988');

-- --------------------------------------------------------

--
-- 表的结构 `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `users`
--

INSERT INTO `users` (`id`, `username`, `password`) VALUES
(1, '123', '123'),
(2, '123', '123'),
(3, '123', '123'),
(4, '123', '123'),
(5, '123', '123'),
(6, '123', '123'),
(7, '123', '123'),
(8, '123', '123'),
(9, '123', '123'),
(10, '123', '123'),
(11, '123', '123'),
(12, 'test', '12'),
(13, 'test', '12'),
(14, 'test', '12'),
(15, 'test', '12'),
(16, 'test', '12'),
(17, 'test', '12'),
(18, 'test', '12'),
(19, 'test', '12'),
(20, 'abc', 'abc'),
(21, 'test1', 'test'),
(22, 'test3', 'test'),
(23, 'test5', '123'),
(24, 'kk', '123'),
(25, 'a', '1'),
(26, '1', '1'),
(27, 'hello', '123456'),
(28, 'aq', '12'),
(29, 'b', 'b'),
(30, '1234', '12'),
(31, 'likuo', '123'),
(32, '喵喵', 'hyt990125'),
(33, '喵喵', 'hyt990125'),
(34, '喵喵', 'hyt990125'),
(35, 'tt', '123'),
(36, 'test123', '1'),
(37, 'pfm', '123'),
(38, '123123', '123');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `backup`
--
ALTER TABLE `backup`
  ADD PRIMARY KEY (`uid`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`) USING BTREE;

--
-- 在导出的表使用AUTO_INCREMENT
--

--
-- 使用表AUTO_INCREMENT `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=39;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
