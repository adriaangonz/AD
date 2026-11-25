-- Base de datos de prueba para comprobar la conexión desde Java
CREATE DATABASE IF NOT EXISTS mi_base_datos
  CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci;

USE mi_base_datos;

-- Tabla personas
DROP TABLE IF EXISTS personas;

CREATE TABLE personas (
  id      INT              NOT NULL AUTO_INCREMENT,
  nombre  VARCHAR(100)     NOT NULL,
  edad    TINYINT UNSIGNED NOT NULL,
  PRIMARY KEY (id)
) DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_spanish_ci;

-- Datos de ejemplo (el id lo genera MySQL)
INSERT INTO personas (nombre, edad) VALUES
  ('Ana',   28),
  ('Luis',  35),
  ('María', 42);

