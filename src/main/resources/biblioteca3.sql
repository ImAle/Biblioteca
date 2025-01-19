-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 19-01-2025 a las 14:09:31
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `biblioteca3`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `libro`
--

CREATE TABLE `libro` (
  `id` bigint(20) NOT NULL,
  `anio_publicacion` int(11) NOT NULL,
  `autor` varchar(255) NOT NULL,
  `genero` varchar(255) NOT NULL,
  `imagen` varchar(255) DEFAULT NULL,
  `titulo` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `libro`
--

INSERT INTO `libro` (`id`, `anio_publicacion`, `autor`, `genero`, `imagen`, `titulo`) VALUES
(1, 1997, 'J.K. Rowling', 'Fantasía', 'http://localhost:8080/fotos/harry_potter.jpg', 'Harry Potter y la piedra filosofal'),
(2, 1954, 'J.R.R. Tolkien', 'Fantasía', 'http://localhost:8080/fotos/el_senor_de_los_anillos.jpg', 'El Señor de los Anillos: La comunidad del anillo'),
(3, 2003, 'Dan Brown', 'Thriller', 'http://localhost:8080/fotos/el_codigo_da_vinci.jpg', 'El Código Da Vinci'),
(4, 1813, 'Jane Austen', 'Romántico', 'http://localhost:8080/fotos/orgullo_y_prejuicio.jpg', 'Orgullo y prejuicio'),
(5, 2005, 'Stephen King', 'Terror', 'http://localhost:8080/fotos/la_cupula.jpg', 'La cúpula'),
(6, 1949, 'George Orwell', 'Distópico', 'http://localhost:8080/fotos/1984.jpg', '1984'),
(7, 1960, 'Harper Lee', 'Ficción', 'http://localhost:8080/fotos/matar_un_ruiseñor.jpg', 'Matar un ruiseñor'),
(8, 2009, 'Suzanne Collins', 'Distópico', 'http://localhost:8080/fotos/los_juegos_del_hambre.jpg', 'Los juegos del hambre'),
(9, 1990, 'Stephen King', 'Terror', 'http://localhost:8080/fotos/it.jpg', 'It'),
(10, 2001, 'J.K. Rowling', 'Fantasía', 'http://localhost:8080/fotos/harry_potter_2.jpg', 'Harry Potter y la cámara secreta'),
(11, 1987, 'Margaret Atwood', 'Ciencia ficción', 'http://localhost:8080/fotos/el_cuento_de_la_criada.jpg', 'El cuento de la criada'),
(12, 2017, 'Angie Thomas', 'Juvenil', 'http://localhost:8080/fotos/the_hate_u_give.jpg', 'The Hate U Give'),
(13, 1999, 'John Grisham', 'Legal', 'http://localhost:8080/fotos/el_abogado.jpg', 'El abogado del diablo'),
(14, 2020, 'Colleen Hoover', 'Romántico', 'http://localhost:8080/fotos/it_ends_with_us.jpg', 'It Ends With Us'),
(15, 2011, 'Veronica Roth', 'Distópico', 'http://localhost:8080/fotos/divergente.jpg', 'Divergente'),
(16, 1985, 'Douglas Adams', 'Ciencia ficción', 'http://localhost:8080/fotos/guia_del_autostopista.jpg', 'Guía del autoestopista galáctico'),
(17, 2004, 'John Green', 'Juvenil', 'http://localhost:8080/fotos/bajo_la_misma_estrella.jpg', 'Bajo la misma estrella'),
(18, 2014, 'Paulo Coelho', 'Filosofía', 'http://localhost:8080/fotos/el_alquimista.jpg', 'El alquimista'),
(19, 2003, 'Terry Pratchett', 'Fantasía', 'http://localhost:8080/fotos/guardias_guardias.jpg', 'Guardias! Guardias!'),
(20, 1992, 'Neil Gaiman', 'Fantasía', 'http://localhost:8080/fotos/american_gods.jpg', 'American Gods'),
(21, 2012, 'Ransom Riggs', 'Fantasía', 'http://localhost:8080/fotos/el_instituto_de_tesoros.jpg', 'El instituto de tesoros'),
(22, 2007, 'C.S. Lewis', 'Fantasía', 'http://localhost:8080/fotos/el_leon_la_bruja_y_el_armario.jpg', 'El león, la bruja y el armario'),
(23, 2010, 'Stieg Larsson', 'Thriller', 'http://localhost:8080/fotos/los_hombres_que_no_amaban_a_las_mujeres.jpg', 'Los hombres que no amaban a las mujeres'),
(24, 1994, 'Michael Ende', 'Fantasía', 'http://localhost:8080/fotos/la_historia_interminable.jpg', 'La historia interminable'),
(25, 1998, 'J.K. Rowling', 'Fantasía', 'http://localhost:8080/fotos/harry_potter_3.jpg', 'Harry Potter y el prisionero de Azkaban'),
(26, 1995, 'Roald Dahl', 'Infantil', 'http://localhost:8080/fotos/matilda.jpg', 'Matilda'),
(27, 2018, 'Gail Honeyman', 'Ficción', 'http://localhost:8080/fotos/eleanor_oliphant.jpg', 'Eleanor Oliphant está perfectamente'),
(28, 2008, 'Stephenie Meyer', 'Romántico', 'http://localhost:8080/fotos/crepusculo.jpg', 'Crepúsculo'),
(29, 2000, 'Carlos Ruiz Zafón', 'Ficción', 'http://localhost:8080/fotos/la_sombra_del_viento.jpg', 'La sombra del viento'),
(30, 2013, 'Jojo Moyes', 'Romántico', 'http://localhost:8080/fotos/yo_porque_yo.jpg', 'Yo antes de ti');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `prestamo`
--

CREATE TABLE `prestamo` (
  `id` bigint(20) NOT NULL,
  `fecha_fin` date DEFAULT NULL,
  `fecha_inicio` date DEFAULT NULL,
  `libro_id` bigint(20) DEFAULT NULL,
  `usuario_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `prestamo`
--

INSERT INTO `prestamo` (`id`, `fecha_fin`, `fecha_inicio`, `libro_id`, `usuario_id`) VALUES
(1, '2025-01-16', '2025-01-15', 20, 2),
(2, '2025-01-15', '2025-01-15', 17, 2),
(3, '2025-01-15', '2025-01-15', 17, 2),
(4, '2025-01-16', '2025-01-15', 6, 2),
(5, '2025-01-22', '2025-01-15', 17, 2),
(6, '2025-01-17', '2025-01-15', 28, 2),
(8, '2025-01-16', '2025-01-16', 13, 3),
(9, '2025-01-16', '2025-01-16', 15, 3),
(10, '2025-01-16', '2025-01-16', 18, 3),
(11, '2025-01-16', '2025-01-16', 3, 3),
(12, '2025-01-16', '2025-01-16', 11, 3),
(13, '2025-01-17', '2025-01-16', 2, 4),
(14, '2025-01-16', '2025-01-16', 22, 3),
(15, '2025-01-17', '2025-01-16', 18, 3),
(16, '2025-01-16', '2025-01-16', 13, 4),
(17, '2025-01-16', '2025-01-16', 13, 4),
(18, '2025-01-17', '2025-01-16', 13, 4),
(19, '2024-10-02', '2024-10-05', 13, 4),
(20, '2025-01-17', '2025-01-17', 20, 3),
(21, '2025-01-17', '2025-01-17', 6, 3),
(22, '2025-01-17', '2025-01-17', 20, 3),
(23, '2025-01-17', '2025-01-17', 6, 3),
(24, '2025-01-17', '2025-01-17', 6, 3),
(25, '2025-01-17', '2025-01-17', 20, 3),
(26, '2025-01-17', '2025-01-17', 13, 3),
(27, '2025-01-24', '2025-01-17', 15, 3),
(28, '2025-01-24', '2025-01-17', 18, 3),
(29, '2025-01-18', '2025-01-18', 6, 3);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `reserva`
--

CREATE TABLE `reserva` (
  `id` bigint(20) NOT NULL,
  `estado` varchar(255) DEFAULT NULL,
  `fecha_reserva` date DEFAULT NULL,
  `libro_id` bigint(20) DEFAULT NULL,
  `usuario_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `reserva`
--

INSERT INTO `reserva` (`id`, `estado`, `fecha_reserva`, `libro_id`, `usuario_id`) VALUES
(3, 'notificado', '2025-01-15', 20, 3),
(7, 'notificado', '2025-01-16', 22, 5),
(12, 'pendiente', '2025-01-17', 17, 3);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `id` bigint(20) NOT NULL,
  `apellido` varchar(20) NOT NULL,
  `email` varchar(255) NOT NULL,
  `enabled` bit(1) NOT NULL,
  `imagen` varchar(255) DEFAULT NULL,
  `nombre` varchar(20) NOT NULL,
  `password` varchar(255) NOT NULL,
  `rol` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`id`, `apellido`, `email`, `enabled`, `imagen`, `nombre`, `password`, `rol`) VALUES
(1, 'administrador', 'admin@gmail.com', b'1', NULL, 'admin', '$2a$10$35wo5IiDvc3PLL4ylwte5utguODu.k6f1eq4StVyc325QUK30xjRe', 'ROLE_ADMIN'),
(2, 'Gallego', 'alejandro@gmail.com', b'1', NULL, 'Alejandro', '$2a$10$aS/gkl6b1b6CBYGZBJTh..SOQlk8up7atGcSuheMt7wlVMgZHdpAu', 'ROLE_USER'),
(3, 'gallego', 'user@gmail.com', b'1', NULL, 'jorge', '$2a$10$n1JTRpiHqVdJFOAAM205jOggqgcgGkG3fK0QW83i8o7xGz2rDQ97C', 'ROLE_USER'),
(4, 'Lopez', 'manu@gmail.com', b'1', NULL, 'Manuel', '$2a$10$oH8RmB7ZXXbi5Nl5A.9q8u9p7Ul7f44Jf5DUDRtDBmH2N36J2AFG6', 'ROLE_USER'),
(5, 'Dominguez', 'gallego.doale20@cadiz.salesianos.edu', b'1', NULL, 'Alejandrito', '$2a$10$5giV8/bEUMd0RkOj2kYXm.jeRnq4lcZPwJdD8jRsaaeE2EOcZkwkO', 'ROLE_USER');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `libro`
--
ALTER TABLE `libro`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `prestamo`
--
ALTER TABLE `prestamo`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKk7uwwn2ov4su2plcn1jh4dbi0` (`libro_id`),
  ADD KEY `FKqxhq6d4w6fuv27c7j3af28wdu` (`usuario_id`);

--
-- Indices de la tabla `reserva`
--
ALTER TABLE `reserva`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKrdf90l0yn62m5swr152un9rmj` (`libro_id`),
  ADD KEY `FKiad9w96t12u3ms2ul93l97mel` (`usuario_id`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK5171l57faosmj8myawaucatdw` (`email`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `libro`
--
ALTER TABLE `libro`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT de la tabla `prestamo`
--
ALTER TABLE `prestamo`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;

--
-- AUTO_INCREMENT de la tabla `reserva`
--
ALTER TABLE `reserva`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT de la tabla `usuario`
--
ALTER TABLE `usuario`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `prestamo`
--
ALTER TABLE `prestamo`
  ADD CONSTRAINT `FKk7uwwn2ov4su2plcn1jh4dbi0` FOREIGN KEY (`libro_id`) REFERENCES `libro` (`id`),
  ADD CONSTRAINT `FKqxhq6d4w6fuv27c7j3af28wdu` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`);

--
-- Filtros para la tabla `reserva`
--
ALTER TABLE `reserva`
  ADD CONSTRAINT `FKiad9w96t12u3ms2ul93l97mel` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`),
  ADD CONSTRAINT `FKrdf90l0yn62m5swr152un9rmj` FOREIGN KEY (`libro_id`) REFERENCES `libro` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
