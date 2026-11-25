-- Préstamo del libro 3 («Fortunata y Jacinta») al socio S002.
-- Se ejecuta desde Java como un único script (allowMultiQueries=true)
-- dentro de una transacción: o se hacen las tres instrucciones o ninguna.
INSERT INTO prestamos (fecha, socio, libro) VALUES (CURDATE(), 'S002', 3);
UPDATE libros SET disponibles = disponibles - 1 WHERE num_libro = 3;
UPDATE socios SET num_prestamos = num_prestamos + 1 WHERE id_socio = 'S002';
