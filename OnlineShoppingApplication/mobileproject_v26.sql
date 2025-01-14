-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- 主機： 127.0.0.1
-- 產生時間： 2024-11-24 12:54:10
-- 伺服器版本： 10.4.22-MariaDB
-- PHP 版本： 8.1.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 資料庫: `mobileproject`
--

-- --------------------------------------------------------
DROP DATABASE IF EXISTS `mobileproject`;
CREATE DATABASE `mobileproject`;
USE `mobileproject`;
--
-- 資料表結構 `account`
--

CREATE TABLE `account` (
  `accountID` int(20) NOT NULL,
  `accountName` varchar(255) NOT NULL,
  `password` varbinary(255) NOT NULL,
  `privilege` enum('admin','user') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 傾印資料表的資料 `account`
--

INSERT INTO `account` (`accountID`, `accountName`, `password`, `privilege`) VALUES
(1, 'testuser1', 0xfec80a7bc873fadb6fefa84b2b139a61, 'user'),
(2, 'rootadmin1', 0x865beda6692485dd545544de409dae8d, 'admin'),
(3, 'testuser2', 0x6c6af897332c53265f480f402b0286ab, 'user'),
(4, 'testuser3', 0x0b49c1e17031b3813ec8cdd9e52e4001, 'user');

-- --------------------------------------------------------

--
-- 資料表結構 `admindetail`
--

CREATE TABLE `admindetail` (
  `adminID` int(20) NOT NULL,
  `adminName` varchar(255) NOT NULL,
  `privilege` enum('rootAdmin','normalAdmin') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 傾印資料表的資料 `admindetail`
--

INSERT INTO `admindetail` (`adminID`, `adminName`, `privilege`) VALUES
(1, 'rootadmin1', 'rootAdmin');

-- --------------------------------------------------------

--
-- 資料表結構 `cart`
--

CREATE TABLE `cart` (
  `userID` int(20) NOT NULL,
  `productID` int(20) NOT NULL,
  `productQTY` int(20) DEFAULT 0,
  `totalPrice` decimal(20,1) DEFAULT 0.0,
  `selectedSize` varchar(30) DEFAULT NULL,
  `selectedColor` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 傾印資料表的資料 `cart`
--

INSERT INTO `cart` (`userID`, `productID`, `productQTY`, `totalPrice`, `selectedSize`, `selectedColor`) VALUES
(1, 1, 2, '559.0', NULL, NULL),
(1, 2, 5, '55.6', NULL, NULL),
(2, 2, 2, '56.3', NULL, NULL),
(2, 3, 1, '99.9', NULL, NULL),
(3, 1, 2, '16.8', NULL, NULL);

-- --------------------------------------------------------

--
-- 資料表結構 `category`
--

CREATE TABLE `category` (
  `categoryID` int(20) NOT NULL,
  `categoryName` varchar(255) NOT NULL,
  `useStatus` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 傾印資料表的資料 `category`
--

INSERT INTO `category` (`categoryID`, `categoryName`, `useStatus`) VALUES
(0, 'Undefine', 1),
(1, 'Shoes', 1),
(2, 'Clothes', 1),
(3, 'Bags', 1),
(4, 'Hats', 1),
(5, 'Sport Equipments', 1),
(17, 'Drink', 1);

-- --------------------------------------------------------

--
-- 資料表結構 `invoice`
--

CREATE TABLE `invoice` (
  `invoiceID` int(20) NOT NULL,
  `userID` int(20) NOT NULL,
  `paymentType` varchar(255) DEFAULT NULL,
  `invoiceDate` timestamp NOT NULL DEFAULT current_timestamp(),
  `shippingState` varchar(255) DEFAULT NULL,
  `shippingAddress` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 傾印資料表的資料 `invoice`
--

INSERT INTO `invoice` (`invoiceID`, `userID`, `paymentType`, `invoiceDate`, `shippingState`, `shippingAddress`) VALUES
(4, 1, 'Credit Card', '2024-11-18 06:32:14', 'Processing', 'Kwun Tong');

-- --------------------------------------------------------

--
-- 資料表結構 `invoiceproductdetail`
--

CREATE TABLE `invoiceproductdetail` (
  `invoiceID` int(20) NOT NULL,
  `productID` int(20) NOT NULL,
  `buyQty` int(20) NOT NULL,
  `totalPrice` decimal(20,1) NOT NULL DEFAULT 0.0,
  `selectedSize` varchar(30) NOT NULL,
  `selectedColor` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 傾印資料表的資料 `invoiceproductdetail`
--

INSERT INTO `invoiceproductdetail` (`invoiceID`, `productID`, `buyQty`, `totalPrice`, `selectedSize`, `selectedColor`) VALUES
(4, 1, 1, '555.0', '', ''),
(4, 2, 1, '555.0', '', ''),
(4, 3, 2, '555.0', '', '');

-- --------------------------------------------------------

--
-- 資料表結構 `product`
--

CREATE TABLE `product` (
  `productID` int(20) NOT NULL,
  `categoryID` int(20) DEFAULT 0,
  `productName` varchar(255) NOT NULL,
  `color` varchar(255) NOT NULL,
  `size` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `sellPrice` decimal(20,1) DEFAULT NULL,
  `discount` double(20,2) DEFAULT 1.00,
  `remainingQTY` int(20) DEFAULT NULL,
  `imageURL` varchar(255) DEFAULT NULL,
  `useStatus` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 傾印資料表的資料 `product`
--

INSERT INTO `product` (`productID`, `categoryID`, `productName`, `color`, `size`, `description`, `sellPrice`, `discount`, `remainingQTY`, `imageURL`, `useStatus`) VALUES
(21, 2, 'Nike Tech', 'ANTHRACITE/(ANTHRACITE)', '2XL', 'Men\'s Tailored Fleece Pants', '949.0', 1.00, 500, 'https://static.nike.com.hk/resources/product/FZ7584-060/FZ7584-060_BL1.png', 1),
(19, 2, 'Nike Tech', 'ANTHRACITE/(ANTHRACITE)', 'L', 'Men\'s Tailored Fleece Pants', '949.0', 1.00, 500, 'https://static.nike.com.hk/resources/product/FZ7584-060/FZ7584-060_BL1.png', 1),
(18, 2, 'Nike Tech', 'ANTHRACITE/(ANTHRACITE)', 'M', 'Men\'s Tailored Fleece Pants', '949.0', 1.00, 500, 'https://static.nike.com.hk/resources/product/FZ7584-060/FZ7584-060_BL1.png', 1),
(17, 2, 'Nike Tech', 'ANTHRACITE/(ANTHRACITE)', 'S', 'Men\'s Tailored Fleece Pants', '949.0', 1.00, 500, 'https://static.nike.com.hk/resources/product/FZ7584-060/FZ7584-060_BL1.png', 1),
(20, 2, 'Nike Tech', 'ANTHRACITE/(ANTHRACITE)', 'XL', 'Men\'s Tailored Fleece Pants', '949.0', 1.00, 500, 'https://static.nike.com.hk/resources/product/FZ7584-060/FZ7584-060_BL1.png', 1),
(27, 4, 'Jordan Peak', 'BLACK/(WHITE)', 'One Size', 'Essential Beanie', '249.0', 1.00, 500, 'https://static.nike.com.hk/resources/product/FN4672-010/FN4672-010_BL1.png', 1),
(26, 4, 'Nike Dri-FIT Fly', 'BLACK/ANTHRACITE/(WHITE)', 'L/XL', 'Unstructured Swoosh Cap', '199.0', 1.00, 500, 'https://static.nike.com.hk/resources/product/FB5624-010/FB5624-010_BL1.png', 1),
(25, 4, 'Nike Dri-FIT Fly', 'BLACK/ANTHRACITE/(WHITE)', 'M/L', 'Unstructured Swoosh Cap', '199.0', 1.00, 500, 'https://static.nike.com.hk/resources/product/FB5624-010/FB5624-010_BL1.png', 1),
(24, 4, 'Nike Dri-FIT Fly', 'BLACK/ANTHRACITE/(WHITE)', 'S/M', 'Unstructured Swoosh Cap', '199.0', 1.00, 500, 'https://static.nike.com.hk/resources/product/FB5624-010/FB5624-010_BL1.png', 1),
(22, 3, 'Nike Hayward', 'BLACK/BLACK/(WHITE)', 'One Size', 'Backpack', '449.0', 1.00, 500, 'https://static.nike.com.hk/resources/product/DV1296-010/DV1296-010_BL1.png', 1),
(28, 5, 'Jordan Legacy 2.0 8P', 'BLACK/BLACK/BLACK/(METALLIC GOLD)', '7', 'Basketball', '329.0', 1.00, 500, 'https://static.nike.com.hk/resources/product/FB2300-051/FB2300-051_BL1.png', 1),
(7, 1, 'Nike C1TY', 'BLACK/SUMMIT WHITE-STADIUM GREEN', '6', 'Men\'s Shoes', '849.0', 0.90, 500, 'https://static.nike.com.hk/resources/product/FZ3863-006/FZ3863-006_BL1.png', 1),
(33, 5, 'Nike Match', 'BLACK/SUNSET PULSE/(BLACK)', '10', 'Soccer Goalkeeper Gloves(1 Pair)', '169.0', 0.90, 500, 'https://static.nike.com.hk/resources/product/FJ4862-014/FJ4862-014_BL1.png', 1),
(34, 5, 'Nike Match', 'BLACK/SUNSET PULSE/(BLACK)', '11', 'Soccer Goalkeeper Gloves(1 Pair)', '169.0', 0.90, 500, 'https://static.nike.com.hk/resources/product/FJ4862-014/FJ4862-014_BL1.png', 1),
(29, 5, 'Nike Match', 'BLACK/SUNSET PULSE/(BLACK)', '6', 'Soccer Goalkeeper Gloves(1 Pair)', '169.0', 0.90, 500, 'https://static.nike.com.hk/resources/product/FJ4862-014/FJ4862-014_BL1.png', 1),
(30, 5, 'Nike Match', 'BLACK/SUNSET PULSE/(BLACK)', '7', 'Soccer Goalkeeper Gloves(1 Pair)', '169.0', 0.90, 500, 'https://static.nike.com.hk/resources/product/FJ4862-014/FJ4862-014_BL1.png', 1),
(31, 5, 'Nike Match', 'BLACK/SUNSET PULSE/(BLACK)', '8', 'Soccer Goalkeeper Gloves(1 Pair)', '169.0', 0.90, 500, 'https://static.nike.com.hk/resources/product/FJ4862-014/FJ4862-014_BL1.png', 1),
(32, 5, 'Nike Match', 'BLACK/SUNSET PULSE/(BLACK)', '9', 'Soccer Goalkeeper Gloves(1 Pair)', '169.0', 0.90, 500, 'https://static.nike.com.hk/resources/product/FJ4862-014/FJ4862-014_BL1.png', 1),
(6, 1, 'Nike C1TY', 'FLAX/BLACK-STADIUM GREEN', '6', 'Men\'s Shoes', '849.0', 0.90, 500, 'https://static.nike.com.hk/resources/product/FZ3863-200/FZ3863-200_BL1.png', 1),
(1, 1, 'Nike C1TY', 'LIGHT ARMY/BLACK-CARGO KHAKI', '6', 'Men\'s Shoes', '849.0', 0.90, 500, 'https://static.nike.com.hk/resources/product/FZ3863-300/FZ3863-300_BL1.png', 1),
(2, 1, 'Nike C1TY', 'LIGHT ARMY/BLACK-CARGO KHAKI', '6.5', 'Men\'s Shoes', '849.0', 0.90, 500, 'https://static.nike.com.hk/resources/product/FZ3863-300/FZ3863-300_BL1.png', 1),
(3, 1, 'Nike C1TY', 'LIGHT ARMY/BLACK-CARGO KHAKI', '7', 'Men\'s Shoes', '849.0', 0.90, 500, 'https://static.nike.com.hk/resources/product/FZ3863-300/FZ3863-300_BL1.png', 1),
(4, 1, 'Nike C1TY', 'LIGHT ARMY/BLACK-CARGO KHAKI', '7.5', 'Men\'s Shoes', '849.0', 0.90, 500, 'https://static.nike.com.hk/resources/product/FZ3863-300/FZ3863-300_BL1.png', 1),
(5, 1, 'Nike C1TY', 'LIGHT ARMY/BLACK-CARGO KHAKI', '8', 'Men\'s Shoes', '849.0', 0.90, 500, 'https://static.nike.com.hk/resources/product/FZ3863-300/FZ3863-300_BL1.png', 1),
(16, 2, 'Nike Pro', 'WHITE/(BLACK)', '2XL', 'Men\'s Dri-FIT Long-Sleeve Fitness Mock', '329.0', 1.00, 500, 'https://static.nike.com.hk/resources/product/FB8516-100/FB8516-100_BL1.png', 1),
(14, 2, 'Nike Pro', 'WHITE/(BLACK)', 'L', 'Men\'s Dri-FIT Long-Sleeve Fitness Mock', '329.0', 1.00, 500, 'https://static.nike.com.hk/resources/product/FB8516-100/FB8516-100_BL1.png', 1),
(13, 2, 'Nike Pro', 'WHITE/(BLACK)', 'M', 'Men\'s Dri-FIT Long-Sleeve Fitness Mock', '329.0', 1.00, 500, 'https://static.nike.com.hk/resources/product/FB8516-100/FB8516-100_BL1.png', 1),
(12, 2, 'Nike Pro', 'WHITE/(BLACK)', 'S', 'Men\'s Dri-FIT Long-Sleeve Fitness Mock', '329.0', 1.00, 500, 'https://static.nike.com.hk/resources/product/FB8516-100/FB8516-100_BL1.png', 1),
(15, 2, 'Nike Pro', 'WHITE/(BLACK)', 'XL', 'Men\'s Dri-FIT Long-Sleeve Fitness Mock', '329.0', 1.00, 500, 'https://static.nike.com.hk/resources/product/FB8516-100/FB8516-100_BL1.png', 1),
(8, 1, 'Air Jordan 1 Retro Low OG”', 'WHITE/BLACK-DK MOCHA', '5', 'Men\'s Shoes', '1099.0', 1.00, 500, 'https://static.nike.com.hk/resources/product/CZ0790-102/CZ0790-102_BL1.png', 1),
(9, 1, 'Air Jordan 1 Retro Low OG”', 'WHITE/BLACK-DK MOCHA', '5.5', 'Men\'s Shoes', '1099.0', 1.00, 500, 'https://static.nike.com.hk/resources/product/CZ0790-102/CZ0790-102_BL1.png', 1),
(10, 1, 'Air Jordan 1 Retro Low OG”', 'WHITE/BLACK-DK MOCHA', '6', 'Men\'s Shoes', '1099.0', 1.00, 500, 'https://static.nike.com.hk/resources/product/CZ0790-102/CZ0790-102_BL1.png', 1),
(11, 1, 'Air Jordan 1 Retro Low OG”', 'WHITE/BLACK-DK MOCHA', '6.5', 'Men\'s Shoes', '1099.0', 1.00, 500, 'https://static.nike.com.hk/resources/product/CZ0790-102/CZ0790-102_BL1.png', 1),
(23, 3, 'Nike Hoops Elite', 'WHITE/BLACK/(METALLIC GOLD)', 'One Size', 'Duffel Bag', '449.0', 0.78, 500, 'https://static.nike.com.hk/resources/product/DX9789-100/DX9789-100_BL2.png', 1);

-- --------------------------------------------------------

--
-- 資料表結構 `userdetail`
--

CREATE TABLE `userdetail` (
  `userID` int(20) NOT NULL,
  `username` varchar(255) NOT NULL,
  `customerName` varchar(255) NOT NULL,
  `phoneNo` varchar(255) NOT NULL,
  `userEmail` varchar(255) NOT NULL,
  `shippingAddress` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 傾印資料表的資料 `userdetail`
--

INSERT INTO `userdetail` (`userID`, `username`, `customerName`, `phoneNo`, `userEmail`, `shippingAddress`) VALUES
(1, 'testuser1', 'Peter Chan', '12345678', 'useremail@gmail.com', 'Kwun Tong'),
(2, 'testuser2', 'Mary Ann', '12345678', 'mary@gmail.com', 'kowloon'),
(3, 'testuser3', 'Kenny Li ', '55554444', 'kenny@gmail.com', '123road');

--
-- 已傾印資料表的索引
--

--
-- 資料表索引 `account`
--
ALTER TABLE `account`
  ADD PRIMARY KEY (`accountID`,`accountName`),
  ADD UNIQUE KEY `accountName` (`accountName`);

--
-- 資料表索引 `admindetail`
--
ALTER TABLE `admindetail`
  ADD PRIMARY KEY (`adminID`),
  ADD UNIQUE KEY `adminName` (`adminName`);

--
-- 資料表索引 `cart`
--
ALTER TABLE `cart`
  ADD PRIMARY KEY (`userID`,`productID`);

--
-- 資料表索引 `category`
--
ALTER TABLE `category`
  ADD PRIMARY KEY (`categoryID`),
  ADD UNIQUE KEY `categoryName` (`categoryName`);

--
-- 資料表索引 `invoice`
--
ALTER TABLE `invoice`
  ADD PRIMARY KEY (`invoiceID`),
  ADD KEY `userID` (`userID`);

--
-- 資料表索引 `invoiceproductdetail`
--
ALTER TABLE `invoiceproductdetail`
  ADD PRIMARY KEY (`invoiceID`,`productID`),
  ADD KEY `invoiceID` (`invoiceID`),
  ADD KEY `productID` (`productID`);

--
-- 資料表索引 `product`
--
ALTER TABLE `product`
  ADD PRIMARY KEY (`color`,`size`,`productName`),
  ADD KEY `categoryID` (`categoryID`),
  ADD KEY `productID` (`productID`);

--
-- 資料表索引 `userdetail`
--
ALTER TABLE `userdetail`
  ADD PRIMARY KEY (`userID`),
  ADD UNIQUE KEY `username` (`username`);

--
-- 在傾印的資料表使用自動遞增(AUTO_INCREMENT)
--

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `account`
--
ALTER TABLE `account`
  MODIFY `accountID` int(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `admindetail`
--
ALTER TABLE `admindetail`
  MODIFY `adminID` int(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `category`
--
ALTER TABLE `category`
  MODIFY `categoryID` int(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `invoice`
--
ALTER TABLE `invoice`
  MODIFY `invoiceID` int(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `product`
--
ALTER TABLE `product`
  MODIFY `productID` int(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=73;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `userdetail`
--
ALTER TABLE `userdetail`
  MODIFY `userID` int(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- 已傾印資料表的限制式
--

--
-- 資料表的限制式 `admindetail`
--
ALTER TABLE `admindetail`
  ADD CONSTRAINT `admindetail_ibfk_1` FOREIGN KEY (`adminName`) REFERENCES `account` (`accountName`);

--
-- 資料表的限制式 `product`
--
ALTER TABLE `product`
  ADD CONSTRAINT `product_ibfk_1` FOREIGN KEY (`categoryID`) REFERENCES `category` (`categoryID`);

--
-- 資料表的限制式 `userdetail`
--
ALTER TABLE `userdetail`
  ADD CONSTRAINT `userdetail_ibfk_1` FOREIGN KEY (`username`) REFERENCES `account` (`accountName`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
