package com.dam2.ad.biblioteca.sqlite;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Actividad 3 & Cuestion C3: Comprobacion de Claves Ajenas en SQLite.
 * 
 * ¿Por que SQLite no comprueba claves foraneas por defecto?
 * Por retrocompatibilidad con versiones antiguas (SQLite 2 y primeras de SQLite 3).
 * El soporte de FOREIGN KEY debe activarse explicitamente en CADA conexion abierta
 * mediante la sentencia SQL: PRAGMA foreign_keys = ON;
 * 
 * En esta clase demostramos la diferencia:
 * 1. Desactivadas: permite insertar un prestamo asignado al socio inexistente 'S999'.
 * 2. Activadas: rechaza la operacion lanzando una SQLException por violacion de clave ajena.
 */
public class PruebaClavesAjena {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("   ACTIVIDAD 3: PRUEBA DE CLAVES AJENAS EN SQLITE ");
        System.out.println("==================================================");

        // Restauramos primero para partir del estado limpio
        RestaurarBD.restaurar();

        System.out.println("\n--- PRUEBA 1: Sin activar claves ajenas (PRAGMA foreign_keys = OFF) ---");
        try (Connection con = ConexionSqlite.getConexion()) {
            // No activamos claves ajenas
            BibliotecaJDBC.prestar(con, "S999", "Niebla");
            System.out.println("[RESULTADO] El prestamo se ha realizado para el socio S999 (¡que no existe!).");
            System.out.println("Consultando estado de libros tras prestamo sin FK:");
            BibliotecaJDBC.consultarLibros(con);
        } catch (SQLException e) {
            System.err.println("Error inesperado: " + e.getMessage());
        }

        System.out.println("\n--- Restaurando base de datos a su estado original... ---");
        RestaurarBD.restaurar();

        System.out.println("\n--- PRUEBA 2: Con claves ajenas activadas (PRAGMA foreign_keys = ON) ---");
        try (Connection con = ConexionSqlite.getConexion()) {
            BibliotecaJDBC.activarClavesAjenas(con);
            BibliotecaJDBC.prestar(con, "S999", "Niebla");
            System.out.println("[RESULTADO] El prestamo se realizo (no deberia llegar aqui).");
        } catch (SQLException e) {
            System.out.println("[COMPORTAMIENTO ESPERADO] SQLite ha rechazado el prestamo correctamente.");
            System.err.printf("Mensaje del motor SQLite: %s%n", e.getMessage());
        }

        System.out.println("\n--- Restaurando base de datos para dejarla lista... ---");
        RestaurarBD.restaurar();
    }
}
