package com.dam2.ad.biblioteca.sqlite;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.Scanner;

/**
 * Programa principal y coleccion de metodos para la gestion de la biblioteca en SQLite con JDBC.
 * 
 * Contiene la resolucion de:
 * - Metodos base del enunciado: activarClavesAjenas, consultarLibros, insertarSocio,
 *   buscarSocio, buscarSocioInseguro, contarPrestamosActivos, prestar.
 * - Actividad 2: explicacion y manejo de restricciones duplicadas y CHECKs.
 * - Actividad 4: insercion interactiva por teclado con validacion y manejo de NULL.
 * - Actividad 5: prestamos pendientes por socio con INNER JOIN y agrupacion visual.
 * - Actividad 6: devolucion transaccional de prestamos con rollback y actualizacion de stock.
 */
public class BibliotecaJDBC {

    // =========================================================================
    // METODOS BASE DEL GUION
    // =========================================================================

    /**
     * Activa la comprobacion de claves ajenas en SQLite para la conexion actual.
     * En SQLite debe ejecutarse en cada conexion porque por defecto estan deshabilitadas.
     */
    public static void activarClavesAjenas(Connection con) throws SQLException {
        try (Statement st = con.createStatement()) {
            st.execute("PRAGMA foreign_keys = ON;");
        }
    }

    /**
     * Muestra todos los libros con sus ejemplares disponibles y totales.
     */
    public static void consultarLibros(Connection con) throws SQLException {
        String sql = "SELECT num_libro, titulo, autor, ejemplares, disponibles FROM libros ORDER BY num_libro";
        System.out.println("\n--- LISTADO DE LIBROS (DISPONIBILIDAD) ---");
        System.out.printf("%-4s | %-25s | %-20s | %-10s | %-11s%n", "NUM", "TITULO", "AUTOR", "EJEMPLARES", "DISPONIBLES");
        System.out.println("-------------------------------------------------------------------------------");

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                System.out.printf("%-4d | %-25s | %-20s | %-10d | %-11d%n",
                        rs.getInt("num_libro"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getInt("ejemplares"),
                        rs.getInt("disponibles"));
            }
        }
        System.out.println("-------------------------------------------------------------------------------");
    }

    /**
     * Inserta un socio con PreparedStatement.
     * Si el email es null o vacio, se inserta como NULL en la base de datos (Types.VARCHAR).
     */
    public static void insertarSocio(Connection con, String id, String nombre, String email) throws SQLException {
        String sql = "INSERT INTO socios (id_socio, nombre, email, num_prestamos) VALUES (?, ?, ?, 0)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.setString(2, nombre);
            if (email == null || email.isBlank()) {
                ps.setNull(3, Types.VARCHAR);
            } else {
                ps.setString(3, email.trim());
            }
            ps.executeUpdate();
            System.out.printf("[OK] Socio insertado correctamente: %s (%s)%n", nombre, id);
        }
    }

    /**
     * Busca un socio de forma SEGURA usando PreparedStatement para evitar SQL Injection.
     */
    public static void buscarSocio(Connection con, String nombre) throws SQLException {
        String sql = "SELECT id_socio, nombre, email, num_prestamos FROM socios WHERE nombre = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.printf("[BUSQUEDA SEGURA] Encontrado: %s (%s) - Email: %s%n",
                            rs.getString("nombre"), rs.getString("id_socio"), rs.getString("email"));
                } else {
                    System.out.printf("[BUSQUEDA SEGURA] No se encontro al socio '%s'%n", nombre);
                }
            }
        }
    }

    /**
     * Demostracion de busqueda INSEGURA concatenando strings (vulnerable a inyeccion SQL).
     */
    public static void buscarSocioInseguro(Connection con, String nombre) throws SQLException {
        // VULNERABLE: concatenacion directa de parametros en la sentencia SQL
        String sql = "SELECT id_socio, nombre, email, num_prestamos FROM socios WHERE nombre = '" + nombre + "'";
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                System.out.printf("[BUSQUEDA INSEGURA] Encontrado: %s (%s)%n", rs.getString("nombre"), rs.getString("id_socio"));
            } else {
                System.out.printf("[BUSQUEDA INSEGURA] No se encontro al socio '%s'%n", nombre);
            }
        }
    }

    /**
     * Devuelve cuantos libros tiene un socio sin devolver (fecha_devolucion IS NULL).
     */
    public static int contarPrestamosActivos(Connection con, String idSocio) throws SQLException {
        String sql = "SELECT COUNT(*) FROM prestamos WHERE socio = ? AND fecha_devolucion IS NULL";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idSocio);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    /**
     * Registra un prestamo dentro de una transaccion ACID:
     * 1. Comprueba disponibilidad del libro.
     * 2. Inserta el prestamo en 'prestamos'.
     * 3. Descuenta 1 disponible en 'libros' (falla por CHECK si disponibles llega a < 0).
     * 4. Incrementa en 1 num_prestamos en 'socios'.
     */
    public static boolean prestar(Connection con, String idSocio, String tituloLibro) throws SQLException {
        boolean autoCommitOriginal = con.getAutoCommit();
        con.setAutoCommit(false); // Iniciar transaccion

        String sqlBuscarLibro = "SELECT num_libro, disponibles FROM libros WHERE titulo = ?";
        String sqlInsertPrestamo = "INSERT INTO prestamos (socio, libro, fecha, fecha_devolucion) VALUES (?, ?, DATE('now'), NULL)";
        String sqlUpdateLibro = "UPDATE libros SET disponibles = disponibles - 1 WHERE num_libro = ?";
        String sqlUpdateSocio = "UPDATE socios SET num_prestamos = num_prestamos + 1 WHERE id_socio = ?";

        try (PreparedStatement psBuscar = con.prepareStatement(sqlBuscarLibro);
             PreparedStatement psInsert = con.prepareStatement(sqlInsertPrestamo);
             PreparedStatement psUpdLibro = con.prepareStatement(sqlUpdateLibro);
             PreparedStatement psUpdSocio = con.prepareStatement(sqlUpdateSocio)) {

            // 1. Localizar el libro
            psBuscar.setString(1, tituloLibro);
            int numLibro = -1;
            int disponibles = 0;
            try (ResultSet rs = psBuscar.executeQuery()) {
                if (rs.next()) {
                    numLibro = rs.getInt("num_libro");
                    disponibles = rs.getInt("disponibles");
                } else {
                    con.rollback();
                    System.err.printf("[ERROR PRESTAMO] El libro '%s' no existe en la biblioteca.%n", tituloLibro);
                    return false;
                }
            }

            if (disponibles <= 0) {
                con.rollback();
                System.err.printf("[ERROR PRESTAMO] No quedan ejemplares disponibles del libro '%s'.%n", tituloLibro);
                return false;
            }

            // 2. Insertar prestamo
            psInsert.setString(1, idSocio);
            psInsert.setInt(2, numLibro);
            psInsert.executeUpdate();

            // 3. Descontar stock disponible
            psUpdLibro.setInt(1, numLibro);
            psUpdLibro.executeUpdate();

            // 4. Sumar prestamo al socio
            psUpdSocio.setString(1, idSocio);
            psUpdSocio.executeUpdate();

            con.commit(); // Confirmar transaccion
            System.out.printf("[OK PRESTAMO] Prestamo registrado: Libro '%s' a socio '%s'.%n", tituloLibro, idSocio);
            return true;

        } catch (SQLException e) {
            con.rollback(); // Cancelar transaccion ante cualquier fallo
            System.err.printf("[FALLO PRESTAMO - ROLLBACK]: %s%n", e.getMessage());
            throw e;
        } finally {
            con.setAutoCommit(autoCommitOriginal);
        }
    }

    // =========================================================================
    // ACTIVIDAD 5: PRESTAMOS PENDIENTES POR SOCIO
    // =========================================================================

    /**
     * Muestra, para cada socio, los titulos que tiene sin devolver (INNER JOIN).
     * El nombre de cada socio aparece una sola vez con sus titulos indentados debajo.
     * 
     * Formato esperado:
     * Ana García (S001):
     *   La Regenta (desde 2026-10-01)
     *   Luces de bohemia (desde 2026-10-05)
     * Marta Ruiz (S003):
     *   Fortunata y Jacinta (desde 2026-10-08)
     */
    public static void prestamosPendientesPorSocio(Connection con) throws SQLException {
        String sql = """
            SELECT s.id_socio, s.nombre, l.titulo, p.fecha
            FROM socios s
            INNER JOIN prestamos p ON s.id_socio = p.socio
            INNER JOIN libros l ON p.libro = l.num_libro
            WHERE p.fecha_devolucion IS NULL
            ORDER BY s.nombre, p.fecha
            """;

        System.out.println("\n--- ACTIVIDAD 5: PRESTAMOS PENDIENTES POR SOCIO (INNER JOIN) ---");

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            String socioActual = "";
            boolean hayResultados = false;

            while (rs.next()) {
                hayResultados = true;
                String idSocio = rs.getString("id_socio");
                String nombreSocio = rs.getString("nombre");
                String titulo = rs.getString("titulo");
                String fecha = rs.getString("fecha");

                // Agrupacion visual: si cambia el socio, mostramos su cabecera
                if (!idSocio.equals(socioActual)) {
                    socioActual = idSocio;
                    System.out.printf("%n%s (%s):%n", nombreSocio, idSocio);
                }
                System.out.printf("  %s (desde %s)%n", titulo, fecha);
            }

            if (!hayResultados) {
                System.out.println("No hay prestamos pendientes actualmente.");
            }
        }
    }

    /**
     * Ampliacion de la Actividad 5: usa LEFT JOIN para incluir tambien a los socios
     * que no tienen ningun libro pendiente.
     */
    public static void todosLosSociosConPrestamosPendientes(Connection con) throws SQLException {
        String sql = """
            SELECT s.id_socio, s.nombre, l.titulo, p.fecha
            FROM socios s
            LEFT JOIN prestamos p ON s.id_socio = p.socio AND p.fecha_devolucion IS NULL
            LEFT JOIN libros l ON p.libro = l.num_libro
            ORDER BY s.nombre, p.fecha
            """;

        System.out.println("\n--- AMPLIACION ACTIVIDAD 5: TODOS LOS SOCIOS (LEFT JOIN) ---");

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            String socioActual = "";

            while (rs.next()) {
                String idSocio = rs.getString("id_socio");
                String nombreSocio = rs.getString("nombre");
                String titulo = rs.getString("titulo");
                String fecha = rs.getString("fecha");

                if (!idSocio.equals(socioActual)) {
                    socioActual = idSocio;
                    System.out.printf("%n%s (%s):%n", nombreSocio, idSocio);
                }

                if (titulo != null) {
                    System.out.printf("  %s (desde %s)%n", titulo, fecha);
                } else {
                    System.out.println("  (Sin libros pendientes)");
                }
            }
        }
    }

    // =========================================================================
    // ACTIVIDAD 6: DEVOLVER UN PRESTAMO
    // =========================================================================

    /**
     * Devuelve un prestamo en una transaccion:
     * 1. Actualiza fecha_devolucion = DATE('now') en prestamos para el id_prestamo dado
     *    siempre que fecha_devolucion IS NULL.
     * 2. Si executeUpdate() devuelve 0 (el prestamo no existe o ya estaba devuelto):
     *    hace rollback() e informa con: "Devolucion cancelada: El prestamo X no existe o ya esta devuelto".
     * 3. Si actualizo la fila (filas = 1):
     *    Suma 1 a los disponibles del libro asociado y confirma la transaccion (commit).
     *    Informa con: "Prestamo X devuelto".
     */
    public static boolean devolver(Connection con, int idPrestamo) throws SQLException {
        boolean autoCommitOriginal = con.getAutoCommit();
        con.setAutoCommit(false); // Iniciar transaccion

        String sqlDevolver = """
            UPDATE prestamos
            SET fecha_devolucion = DATE('now')
            WHERE id_prestamo = ? AND fecha_devolucion IS NULL
            """;

        String sqlObtenerLibro = "SELECT libro FROM prestamos WHERE id_prestamo = ?";
        String sqlSumarStock = "UPDATE libros SET disponibles = disponibles + 1 WHERE num_libro = ?";

        try (PreparedStatement psDevolver = con.prepareStatement(sqlDevolver);
             PreparedStatement psLibro = con.prepareStatement(sqlObtenerLibro);
             PreparedStatement psSumar = con.prepareStatement(sqlSumarStock)) {

            psDevolver.setInt(1, idPrestamo);
            int filasAfectadas = psDevolver.executeUpdate();

            // Si no se modifico ninguna fila, el prestamo no existia o ya estaba devuelto
            if (filasAfectadas == 0) {
                con.rollback();
                System.out.printf("Devolucion cancelada: El prestamo %d no existe o ya esta devuelto%n", idPrestamo);
                return false;
            }

            // Averiguar cual era el libro del prestamo para incrementar su stock disponible
            int numLibro = -1;
            psLibro.setInt(1, idPrestamo);
            try (ResultSet rs = psLibro.executeQuery()) {
                if (rs.next()) {
                    numLibro = rs.getInt("libro");
                }
            }

            if (numLibro != -1) {
                psSumar.setInt(1, numLibro);
                psSumar.executeUpdate();
            }

            con.commit(); // Confirmar devolucion y actualizacion de stock
            System.out.printf("Prestamo %d devuelto%n", idPrestamo);
            return true;

        } catch (SQLException e) {
            con.rollback();
            System.err.printf("[ERROR EN DEVOLUCION %d]: %s%n", idPrestamo, e.getMessage());
            throw e;
        } finally {
            con.setAutoCommit(autoCommitOriginal);
        }
    }

    // =========================================================================
    // ACTIVIDAD 4: PEDIR DATOS POR TECLADO
    // =========================================================================

    public static void pedirDatosSocioTeclado(Connection con, Scanner scanner) {
        System.out.println("\n--- ACTIVIDAD 4: ALTA INTERACTIVA DE SOCIO ---");
        System.out.print("Introduce el codigo del socio (obligatorio, ej. S005): ");
        String id = scanner.nextLine().trim();
        if (id.isEmpty()) {
            System.out.println("[AVISO] El codigo de socio es obligatorio. No se inserta nada.");
            return;
        }

        System.out.print("Introduce el nombre del socio (obligatorio): ");
        String nombre = scanner.nextLine().trim();
        if (nombre.isEmpty()) {
            System.out.println("[AVISO] El nombre de socio es obligatorio. No se inserta nada.");
            return;
        }

        System.out.print("Introduce el email (opcional, pulsa ENTER si no tiene): ");
        String email = scanner.nextLine().trim();
        if (email.isEmpty()) {
            email = null; // Se guardara como NULL en la base de datos
            System.out.println("[INFO] Se guardara el email como NULL (setNull).");
        }

        try {
            insertarSocio(con, id, nombre, email);
        } catch (SQLException e) {
            System.err.printf("[FALLO RESTRICCION SQLITE]: %s%n", e.getMessage());
        }
    }

    // =========================================================================
    // PROGRAMA PRINCIPAL
    // =========================================================================

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("   BIBLIOTECA SQLITE CON JDBC (ACTIVIDADES 1 A 6) ");
        System.out.println("==================================================");

        // Restaurar estado inicial limpio
        RestaurarBD.restaurar();

        try (Connection con = ConexionSqlite.getConexion()) {
            activarClavesAjenas(con);

            // Actividad 5 al principio: prestamos pendientes antes de prestar nada
            System.out.println("\n1. Estado de prestamos pendientes antes de nuevos prestamos:");
            prestamosPendientesPorSocio(con);

            // Estado de libros
            consultarLibros(con);

            // Intentos de prestamo del enunciado base
            System.out.println("\n2. Realizando prestamos de prueba:");
            prestar(con, "S002", "Niebla");
            prestar(con, "S004", "Luces de bohemia"); // Disponible 0 -> CHECK fallara
            prestar(con, "S004", "El Quijote");       // No existe -> fallara

            // Actividad 6: Devolver prestamo
            System.out.println("\n3. Pruebas de devolucion (Actividad 6):");
            devolver(con, 2);
            devolver(con, 2);  // Segunda vez: ya devuelto
            devolver(con, 99); // No existe

            // Comprobacion post-devolucion
            System.out.println("\n4. Estado tras la devolucion:");
            prestamosPendientesPorSocio(con);
            consultarLibros(con);

            // Ampliacion Actividad 5: LEFT JOIN
            todosLosSociosConPrestamosPendientes(con);

        } catch (SQLException e) {
            System.err.println("Error general en ejecucion: " + e.getMessage());
        }
    }
}
