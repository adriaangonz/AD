-- =====================================================================
--  Base de datos BIBLIOTECA
-- =====================================================================

DROP DATABASE IF EXISTS biblioteca;
CREATE DATABASE biblioteca CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE biblioteca;

-- ---------------------------------------------------------------------
--  Tablas (InnoDB: necesario para usar transacciones)
-- ---------------------------------------------------------------------

CREATE TABLE socios (
    id_socio       VARCHAR(10)  PRIMARY KEY,
    nombre         VARCHAR(60)  NOT NULL,
    email          VARCHAR(80)  UNIQUE,
    num_prestamos  INT          NOT NULL DEFAULT 0      -- préstamos realizados (histórico)
) ENGINE = InnoDB;

CREATE TABLE libros (
    num_libro      INT          AUTO_INCREMENT PRIMARY KEY,
    titulo         VARCHAR(80)  NOT NULL UNIQUE,
    autor          VARCHAR(60)  NOT NULL,
    ejemplares     INT          NOT NULL,
    disponibles    INT          NOT NULL,
    CONSTRAINT chk_disponibles CHECK (disponibles BETWEEN 0 AND ejemplares)
) ENGINE = InnoDB;

CREATE TABLE prestamos (
    id_prestamo       INT          AUTO_INCREMENT PRIMARY KEY,
    fecha             DATE         NOT NULL,
    socio             VARCHAR(10)  NOT NULL,
    libro             INT          NOT NULL,
    fecha_devolucion  DATE         NULL,                -- NULL = aún no devuelto
    CONSTRAINT fk_prestamo_socio FOREIGN KEY (socio) REFERENCES socios (id_socio),
    CONSTRAINT fk_prestamo_libro FOREIGN KEY (libro) REFERENCES libros (num_libro)
) ENGINE = InnoDB;

-- ---------------------------------------------------------------------
--  Datos de ejemplo
-- ---------------------------------------------------------------------

INSERT INTO socios (id_socio, nombre, email, num_prestamos) VALUES
    ('S001', 'Ana García',    'ana.garcia@correo.es',   2),
    ('S002', 'Luis Pérez',    'luis.perez@correo.es',   1),
    ('S003', 'Marta Ruiz',    'marta.ruiz@correo.es',   1),
    ('S004', 'Javier Sanz',   'javier.sanz@correo.es',  0);

INSERT INTO libros (num_libro, titulo, autor, ejemplares, disponibles) VALUES
    (1, 'La Regenta',           'Leopoldo Alas «Clarín»', 3, 2),
    (2, 'Niebla',               'Miguel de Unamuno',      2, 2),
    (3, 'Fortunata y Jacinta',  'Benito Pérez Galdós',    2, 1),
    (4, 'Luces de bohemia',     'Ramón del Valle-Inclán', 1, 0),
    (5, 'Platero y yo',         'Juan Ramón Jiménez',     4, 4),
    (6, 'Campos de Castilla',   'Antonio Machado',        2, 2);

INSERT INTO prestamos (fecha, socio, libro, fecha_devolucion) VALUES
    ('2026-09-15', 'S002', 2, '2026-09-29'),   -- ya devuelto
    ('2026-10-01', 'S001', 1, NULL),
    ('2026-10-05', 'S001', 4, NULL),
    ('2026-10-08', 'S003', 3, NULL);

-- ---------------------------------------------------------------------
--  Procedimiento prestar: registra el préstamo de un libro a un socio.
--  Las tres modificaciones forman una única transacción.
-- ---------------------------------------------------------------------

DELIMITER //

CREATE PROCEDURE prestar(IN soc VARCHAR(10), IN tit VARCHAR(80))
BEGIN
    DECLARE numl INT;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
        BEGIN ROLLBACK; RESIGNAL; END;

    SELECT num_libro INTO numl FROM libros WHERE titulo = tit;
    IF numl IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El libro no existe';
    END IF;

    START TRANSACTION;
    INSERT INTO prestamos (fecha, socio, libro) VALUES (CURDATE(), soc, numl);
    UPDATE libros SET disponibles = disponibles - 1 WHERE num_libro = numl;
    UPDATE socios SET num_prestamos = num_prestamos + 1 WHERE id_socio = soc;
    COMMIT;
END //

-- ---------------------------------------------------------------------
--  Procedimiento con parámetro OUT: ejemplares disponibles de un título
-- ---------------------------------------------------------------------

CREATE PROCEDURE libros_disponibles(IN tit VARCHAR(80), OUT total INT)
BEGIN
    SELECT disponibles INTO total FROM libros WHERE titulo = tit;
END //

-- ---------------------------------------------------------------------
--  Función: número de préstamos activos (sin devolver) de un socio
-- ---------------------------------------------------------------------

CREATE FUNCTION prestamos_activos(soc VARCHAR(10)) RETURNS INT
READS SQL DATA
BEGIN
    RETURN (SELECT COUNT(*) FROM prestamos
            WHERE socio = soc AND fecha_devolucion IS NULL);
END //

DELIMITER ;

-- ---------------------------------------------------------------------
--  Pruebas rápidas (descomentar para probar desde el cliente)
-- ---------------------------------------------------------------------
-- CALL prestar('S002', 'Niebla');
-- CALL prestar('S004', 'Luces de bohemia');   -- error: no quedan ejemplares (CHECK)
-- CALL prestar('S004', 'El Quijote');         -- error 45000: el libro no existe
-- CALL libros_disponibles('Niebla', @total); SELECT @total;
-- SELECT prestamos_activos('S001');
