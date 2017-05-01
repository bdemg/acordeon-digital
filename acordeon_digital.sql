-- phpMyAdmin SQL Dump
-- version 4.6.5.2
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 01-05-2017 a las 23:52:34
-- Versión del servidor: 10.1.21-MariaDB
-- Versión de PHP: 5.6.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `acordeon_digital`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `concepts`
--

CREATE TABLE `concepts` (
  `concept_id` int(11) NOT NULL,
  `concept_name` varchar(25) NOT NULL,
  `category` varchar(25) NOT NULL,
  `definition` varchar(500) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `creation`
--

CREATE TABLE `creation` (
  `user_id` int(11) NOT NULL,
  `concept_id` int(11) NOT NULL,
  `creation_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `editions`
--

CREATE TABLE `editions` (
  `user_id` int(11) NOT NULL,
  `concept_id` int(11) NOT NULL,
  `edit_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `eliminations`
--

CREATE TABLE `eliminations` (
  `user_id` int(11) NOT NULL,
  `concept_name` varchar(25) NOT NULL,
  `category` varchar(25) NOT NULL,
  `elimination_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `name` varchar(30) NOT NULL,
  `password` varchar(27) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `users`
--

INSERT INTO `users` (`user_id`, `name`, `password`) VALUES
(1, 'John', 'root');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `concepts`
--
ALTER TABLE `concepts`
  ADD PRIMARY KEY (`concept_id`);

--
-- Indices de la tabla `creation`
--
ALTER TABLE `creation`
  ADD PRIMARY KEY (`user_id`,`concept_id`,`creation_date`),
  ADD KEY `concept_id` (`concept_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indices de la tabla `editions`
--
ALTER TABLE `editions`
  ADD PRIMARY KEY (`user_id`,`concept_id`,`edit_date`),
  ADD KEY `concept_id` (`concept_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indices de la tabla `eliminations`
--
ALTER TABLE `eliminations`
  ADD PRIMARY KEY (`user_id`,`concept_name`,`elimination_date`),
  ADD KEY `user_id` (`user_id`);

--
-- Indices de la tabla `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `concepts`
--
ALTER TABLE `concepts`
  MODIFY `concept_id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT de la tabla `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `concepts`
--
ALTER TABLE `concepts`
  ADD CONSTRAINT `concepts2creation` FOREIGN KEY (`concept_id`) REFERENCES `creation` (`concept_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `concepts2edition` FOREIGN KEY (`concept_id`) REFERENCES `editions` (`concept_id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `editions`
--
ALTER TABLE `editions`
  ADD CONSTRAINT `users2editions` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `eliminations`
--
ALTER TABLE `eliminations`
  ADD CONSTRAINT `users2eliminations` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
