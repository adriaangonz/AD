-- =====================================================================
--  Base de datos VETERINARIA
--  Al ejecutarlo se BORRA la base de datos y se vuelve a los datos de partida.
-- =====================================================================

SET NAMES utf8mb4;   -- los textos del script están en UTF-8

DROP DATABASE IF EXISTS veterinaria;
CREATE DATABASE veterinaria CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE veterinaria;

-- ---------------------------------------------------------------------
--  Tablas
-- ---------------------------------------------------------------------

CREATE TABLE clientes (
    id_cliente   INT          AUTO_INCREMENT PRIMARY KEY,
    dni          CHAR(9)      NOT NULL UNIQUE,
    nombre       VARCHAR(60)  NOT NULL,
    telefono     VARCHAR(15),
    email        VARCHAR(80)  UNIQUE
) ENGINE = InnoDB;

CREATE TABLE mascotas (
    id_mascota        INT          AUTO_INCREMENT PRIMARY KEY,
    nombre            VARCHAR(40)  NOT NULL,
    especie           VARCHAR(10)  NOT NULL,
    fecha_nacimiento  DATE,
    id_cliente        INT          NOT NULL,
    CONSTRAINT chk_especie CHECK (especie IN ('perro', 'gato', 'conejo', 'ave', 'otro')),
    CONSTRAINT fk_mascota_cliente FOREIGN KEY (id_cliente) REFERENCES clientes (id_cliente)
) ENGINE = InnoDB;

CREATE TABLE veterinarios (
    id_veterinario  INT          AUTO_INCREMENT PRIMARY KEY,
    nombre          VARCHAR(60)  NOT NULL,
    especialidad    VARCHAR(40)  NOT NULL,
    activo          BOOLEAN      NOT NULL DEFAULT TRUE      -- FALSE = de baja
) ENGINE = InnoDB;

CREATE TABLE citas (
    id_cita         INT           AUTO_INCREMENT PRIMARY KEY,
    id_mascota      INT           NOT NULL,
    id_veterinario  INT           NOT NULL,
    fecha_hora      DATETIME      NOT NULL,
    tipo            VARCHAR(10)   NOT NULL,
    motivo          VARCHAR(100),
    precio          DECIMAL(7,2)  NOT NULL,
    estado          VARCHAR(10)   NOT NULL DEFAULT 'PENDIENTE',
    CONSTRAINT chk_tipo   CHECK (tipo IN ('CONSULTA', 'VACUNA', 'REVISION', 'CIRUGIA')),
    CONSTRAINT chk_estado CHECK (estado IN ('PENDIENTE', 'REALIZADA', 'ANULADA')),
    CONSTRAINT chk_precio CHECK (precio >= 0),
    -- horario de la clínica: de 9:00 a 19:30, en tramos de media hora
    CONSTRAINT chk_horario CHECK (HOUR(fecha_hora) BETWEEN 9 AND 19
                                  AND MINUTE(fecha_hora) IN (0, 30)
                                  AND SECOND(fecha_hora) = 0),
    CONSTRAINT fk_cita_mascota     FOREIGN KEY (id_mascota)     REFERENCES mascotas (id_mascota),
    CONSTRAINT fk_cita_veterinario FOREIGN KEY (id_veterinario) REFERENCES veterinarios (id_veterinario)
) ENGINE = InnoDB;

-- ---------------------------------------------------------------------
--  Datos de partida
-- ---------------------------------------------------------------------

INSERT INTO clientes (id_cliente, dni, nombre, telefono, email) VALUES
    (1, '12345678Z', 'Carmen Díaz',    '600111222', 'carmen.diaz@correo.es'),
    (2, '23456789D', 'Jorge Romero',   '600333444', 'jorge.romero@correo.es'),
    (3, '34567890V', 'Lucía Herrera',  '600555666', 'lucia.herrera@correo.es'),
    (4, '45678901G', 'Andrés Molina',  '600777888', NULL);              -- sin mascotas

INSERT INTO mascotas (id_mascota, nombre, especie, fecha_nacimiento, id_cliente) VALUES
    (1, 'Luna',  'perro',  '2019-04-12', 1),
    (2, 'Michi', 'gato',   '2021-09-03', 1),
    (3, 'Coco',  'conejo', '2023-02-20', 1),
    (4, 'Toby',  'perro',  '2016-11-30', 2),
    (5, 'Kiwi',  'ave',    '2022-06-15', 3),
    (6, 'Nala',  'gato',   NULL,         3);

INSERT INTO veterinarios (id_veterinario, nombre, especialidad, activo) VALUES
    (1, 'Laura Gómez',  'Medicina general', TRUE),
    (2, 'Pablo Ortiz',  'Cirugía',          TRUE),
    (3, 'Elena Martín', 'Animales exóticos', FALSE);                   -- de baja

INSERT INTO citas (id_mascota, id_veterinario, fecha_hora, tipo, motivo, precio, estado) VALUES
    (1, 1, '2026-10-05 10:00:00', 'VACUNA',   'Vacuna anual de la rabia', 25.00,  'REALIZADA'),
    (4, 1, '2026-10-14 12:30:00', 'CONSULTA', 'Cojea de la pata trasera', 35.00,  'REALIZADA'),
    (5, 3, '2026-10-20 09:30:00', 'CONSULTA', 'Revisión de plumaje',      35.00,  'ANULADA'),
    (1, 1, '2026-11-10 10:00:00', 'REVISION', 'Control de peso',          20.00,  'PENDIENTE'),
    (2, 1, '2026-11-10 10:30:00', 'VACUNA',   'Vacuna trivalente',        25.00,  'PENDIENTE'),
    (3, 1, '2026-11-10 12:00:00', 'CONSULTA', 'No come bien',             35.00,  'PENDIENTE'),
    (6, 2, '2026-11-10 11:00:00', 'CIRUGIA',  'Esterilización',           150.00, 'PENDIENTE'),
    (4, 1, '2026-11-12 17:00:00', 'CONSULTA', 'Seguimiento de la cojera', 35.00,  'PENDIENTE');

-- ---------------------------------------------------------------------
--  Usuario de la aplicación Java: solo puede trabajar con los datos y
--  ejecutar procedimientos y funciones. No puede crear ni borrar tablas.
--  (Los procedimientos de la Parte 1 se crean como root desde Workbench.)
-- ---------------------------------------------------------------------

DROP USER IF EXISTS 'veterinaria'@'localhost';
CREATE USER 'veterinaria'@'localhost' IDENTIFIED BY 'Vet.2026_clinica';
GRANT SELECT, INSERT, UPDATE, DELETE, EXECUTE ON veterinaria.* TO 'veterinaria'@'localhost';

-- ---------------------------------------------------------------------
--  Rutinas de ejemplo (sirven de modelo para las de la Parte 1)
-- ---------------------------------------------------------------------

DELIMITER //

-- Función: edad en años cumplidos de una mascota (NULL si no se conoce)
CREATE FUNCTION edad_mascota(p_mascota INT) RETURNS INT
READS SQL DATA
BEGIN
    DECLARE v_nacimiento DATE;
    SELECT fecha_nacimiento INTO v_nacimiento
      FROM mascotas WHERE id_mascota = p_mascota;
    RETURN TIMESTAMPDIFF(YEAR, v_nacimiento, CURDATE());
END //

-- Procedimiento: pide una cita y devuelve su identificador (OUT).
-- El precio depende del tipo de cita.
-- No abre ni cierra transacciones: si se llama dentro de una transacción
-- de Java, forma parte de ella.
CREATE PROCEDURE pedir_cita(IN  p_mascota      INT,
                            IN  p_veterinario  INT,
                            IN  p_fecha_hora   DATETIME,
                            IN  p_tipo         VARCHAR(10),
                            IN  p_motivo       VARCHAR(100),
                            OUT p_id_cita      INT)
BEGIN
    DECLARE v_activo  BOOLEAN;
    DECLARE v_ocupado INT;
    DECLARE v_precio  DECIMAL(7,2);

    SELECT activo INTO v_activo
      FROM veterinarios WHERE id_veterinario = p_veterinario;
    IF v_activo IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El veterinario no existe';
    END IF;
    IF NOT v_activo THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El veterinario no está activo';
    END IF;

    SELECT COUNT(*) INTO v_ocupado
      FROM citas
     WHERE id_veterinario = p_veterinario
       AND fecha_hora = p_fecha_hora
       AND estado <> 'ANULADA';
    IF v_ocupado > 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El veterinario ya tiene una cita a esa hora';
    END IF;

    SET v_precio = CASE p_tipo
                       WHEN 'CONSULTA' THEN 35.00
                       WHEN 'VACUNA'   THEN 25.00
                       WHEN 'REVISION' THEN 20.00
                       WHEN 'CIRUGIA'  THEN 150.00
                   END;
    IF v_precio IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Tipo de cita no válido';
    END IF;

    INSERT INTO citas (id_mascota, id_veterinario, fecha_hora, tipo, motivo, precio)
    VALUES (p_mascota, p_veterinario, p_fecha_hora, p_tipo, p_motivo, v_precio);

    SET p_id_cita = LAST_INSERT_ID();
END //

DELIMITER ;

-- ---------------------------------------------------------------------
--  Pruebas rápidas (descomentar para probar desde Workbench)
-- ---------------------------------------------------------------------
-- SELECT nombre, edad_mascota(id_mascota) AS edad FROM mascotas;
-- CALL pedir_cita(4, 2, '2026-11-20 16:30:00', 'CONSULTA', 'Revisión', @id);  SELECT @id;
-- CALL pedir_cita(5, 3, '2026-11-20 10:00:00', 'CONSULTA', 'Revisión', @id);  -- veterinario de baja
-- CALL pedir_cita(2, 1, '2026-11-10 10:00:00', 'VACUNA', 'Refuerzo', @id);    -- hora ocupada
