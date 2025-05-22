-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: May 22, 2025 at 03:38 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.0.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `Proyecto-Programacion`
--
CREATE DATABASE IF NOT EXISTS `Proyecto-Programacion` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `Proyecto-Programacion`;

-- --------------------------------------------------------

--
-- Table structure for table `coches`
--

DROP TABLE IF EXISTS `coches`;
CREATE TABLE `coches` (
  `id_coche` int(11) NOT NULL,
  `marca` varchar(50) NOT NULL,
  `modelo` varchar(50) NOT NULL,
  `precio` int(11) NOT NULL,
  `kilometraje` int(11) NOT NULL,
  `tipo` varchar(9) NOT NULL,
  `estado` varchar(5) NOT NULL CHECK (`estado` in ('nuevo','usado')),
  `id_vendedor` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `coches`
--

INSERT INTO `coches` (`id_coche`, `marca`, `modelo`, `precio`, `kilometraje`, `tipo`, `estado`, `id_vendedor`) VALUES
(1, 'Toyota', 'Corolla', 18001, 25000, 'sedán', 'usado', 3),
(2, 'Ford', 'F-150', 35000, 15000, 'pickup', 'nuevo', 3),
(3, 'BMW', 'X5', 42001, 30000, 'SUV', 'usado', 4),
(4, 'Audi', 'A3', 29000, 5000, 'hatchback', 'nuevo', 4);

-- --------------------------------------------------------

--
-- Table structure for table `inventario`
--

DROP TABLE IF EXISTS `inventario`;
CREATE TABLE `inventario` (
  `id_inventario` int(11) NOT NULL,
  `id_coche` int(11) NOT NULL,
  `estado` varchar(10) NOT NULL CHECK (`estado` in ('disponible','vendido','reservado'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `inventario`
--

INSERT INTO `inventario` (`id_inventario`, `id_coche`, `estado`) VALUES
(1, 1, 'disponible'),
(2, 2, 'reservado'),
(3, 3, 'disponible'),
(4, 4, 'vendido');

-- --------------------------------------------------------

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
CREATE TABLE `usuarios` (
  `id_usuario` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `telefono` varchar(15) NOT NULL,
  `email` varchar(100) NOT NULL,
  `direccion` varchar(255) NOT NULL,
  `tipo` varchar(10) NOT NULL CHECK (`tipo` in ('cliente','vendedor'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `usuarios`
--

INSERT INTO `usuarios` (`id_usuario`, `nombre`, `telefono`, `email`, `direccion`, `tipo`) VALUES
(1, 'Juan Pérez', '654321987', 'juan.perez@email.com', 'Calle 123, Madrid', 'cliente'),
(2, 'María López', '612345678', 'maria.lopez@email.com', 'Avenida 456, Barcelona', 'cliente'),
(3, 'Carlos Fernández', '698745632', 'carlos.fernandez@email.com', 'Paseo 789, Valencia', 'vendedor'),
(4, 'Lucía Gómez', '677889900', 'lucia.gomez@email.com', 'Plaza Mayor 5, Sevilla', 'vendedor');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `coches`
--
ALTER TABLE `coches`
  ADD PRIMARY KEY (`id_coche`),
  ADD KEY `id_vendedor` (`id_vendedor`);

--
-- Indexes for table `inventario`
--
ALTER TABLE `inventario`
  ADD PRIMARY KEY (`id_inventario`),
  ADD KEY `id_coche` (`id_coche`);

--
-- Indexes for table `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id_usuario`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `coches`
--
ALTER TABLE `coches`
  MODIFY `id_coche` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `inventario`
--
ALTER TABLE `inventario`
  MODIFY `id_inventario` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `id_usuario` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `coches`
--
ALTER TABLE `coches`
  ADD CONSTRAINT `coches_ibfk_1` FOREIGN KEY (`id_vendedor`) REFERENCES `usuarios` (`id_usuario`);

--
-- Constraints for table `inventario`
--
ALTER TABLE `inventario`
  ADD CONSTRAINT `inventario_ibfk_1` FOREIGN KEY (`id_coche`) REFERENCES `coches` (`id_coche`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
