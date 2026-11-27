DROP DATABASE IF EXISTS bd_taller_jdbc;
CREATE DATABASE bd_taller_jdbc CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE bd_taller_jdbc;

CREATE TABLE clientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(12) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    telefono VARCHAR(20) NULL,
    saldo_bono DECIMAL(8,2) NOT NULL DEFAULT 0.00
);

CREATE TABLE reparaciones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    matricula VARCHAR(10) NOT NULL,
    descripcion VARCHAR(200) NOT NULL,
    fecha_entrada DATE NOT NULL,
    fecha_salida DATE NULL,
    coste DECIMAL(8,2) NULL,
    id_cliente INT NULL,
    CONSTRAINT fk_rep_cliente FOREIGN KEY (id_cliente)
        REFERENCES clientes(id) ON DELETE RESTRICT
);

INSERT INTO clientes (dni, nombre, telefono, saldo_bono) VALUES
('11111111A', 'Laura Gómez', '600111222', 50.00),
('22222222B', 'Carlos Ruiz', NULL, 20.00),
('33333333C', 'Marta Pérez', '699888777', 0.00),
('44444444D', 'Javier López', NULL, 100.00);

INSERT INTO reparaciones (matricula, descripcion, fecha_entrada, fecha_salida, coste, id_cliente) VALUES
('1234ABC', 'Cambio de aceite y filtros', '2026-11-10', '2026-11-11', 120.50, 1),
('1234ABC', 'Revisión frenos delanteros', '2026-11-20', NULL, NULL, 1),
('5678DEF', 'Sustitución batería', '2026-11-18', '2026-11-18', 95.00, 2),
('9012GHI', 'Diagnóstico testigo motor', '2026-11-25', NULL, 45.00, 3),
('3456JKL', 'Vehículo de cortesía - puesta a punto', '2026-11-26', NULL, NULL, NULL);

DELIMITER //

-- 1. Procedimiento IN: finaliza una reparación asignando fecha actual y coste
CREATE PROCEDURE finalizar_reparacion(
    IN p_id INT,
    IN p_coste DECIMAL(8,2)
)
BEGIN
    UPDATE reparaciones
    SET fecha_salida = CURDATE(),
        coste = p_coste
    WHERE id = p_id;
END //

-- 2. Procedimiento OUT: devuelve total de reparaciones y cuántas siguen pendientes
CREATE PROCEDURE estadisticas_cliente(
    IN p_id_cliente INT,
    OUT p_total_rep INT,
    OUT p_pendientes INT
)
BEGIN
    SELECT COUNT(*),
           COALESCE(SUM(CASE WHEN fecha_salida IS NULL THEN 1 ELSE 0 END), 0)
    INTO p_total_rep, p_pendientes
    FROM reparaciones
    WHERE id_cliente = p_id_cliente;
END //

-- 3. Procedimiento INOUT: descuenta el saldo_bono del cliente del importe recibido
--    y devuelve en p_importe la cantidad restante a pagar.
CREATE PROCEDURE aplicar_descuento_bono(
    IN p_id_cliente INT,
    INOUT p_importe DECIMAL(8,2)
)
BEGIN
    DECLARE v_bono DECIMAL(8,2) DEFAULT 0.00;
    DECLARE v_descuento DECIMAL(8,2) DEFAULT 0.00;

    SELECT saldo_bono INTO v_bono
    FROM clientes
    WHERE id = p_id_cliente;

    IF v_bono >= p_importe THEN
        SET v_descuento = p_importe;
    ELSE
        SET v_descuento = v_bono;
    END IF;

    UPDATE clientes
    SET saldo_bono = saldo_bono - v_descuento
    WHERE id = p_id_cliente;

    SET p_importe = p_importe - v_descuento;
END //

DELIMITER ;
