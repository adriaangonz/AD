package com.dam2.ad.biblioteca.sqlite;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Actividad 1 & Cuestion C2: Conectar desde Java a SQLite mediante JDBC.
 * 
 * Aspectos fundamentales explicados:
 * 1. En SQLite, la base de datos es un unico fichero local en disco (.db).
 * 2. URL de conexion: "jdbc:sqlite:biblioteca.db" (relativa a la raiz del proyecto donde reside el pom.xml).
 * 3. ¿Que ocurre si el fichero no existe?:
 *    El driver de SQLite crea AUTOMATICAMENTE un fichero nuevo y completamente vacio en esa ruta.
 *    Por tanto, que DriverManager.getConnection() devuelva exito ("Conexion OK") NO garantiza que
 *    estemos usando la base de datos adecuada ni que contenga tablas; si el nombre no coincide exactamente
 *    (o en Linux por mayusculas/minusculas), se creara una BD vacia y cualquier SELECT fallara con "no such table".
 */
public class ConexionSqlite {

    // Ruta de la base de datos por defecto junto al pom.xml
    public static final String RUTA_BD = "biblioteca.db";
    public static final String URL_BD = "jdbc:sqlite:" + RUTA_BD;

    public static Connection getConexion() throws SQLException {
        // En Windows puede requerirse configurar el directorio temporal para DLLs nativas:
        // System.setProperty("org.sqlite.tmpdir", System.getProperty("user.home") + File.separator + "Documents");
        return DriverManager.getConnection(URL_BD);
    }

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("   ACTIVIDAD 1: COMPROBACION CONEXION SQLITE      ");
        System.out.println("==================================================");

        File fichero = new File(RUTA_BD);
        System.out.printf("Ruta esperada del archivo: %s%n", fichero.getAbsolutePath());
        System.out.printf("¿El fichero existe fisicamente antes de conectar?: %b%n", fichero.exists());

        try (Connection con = getConexion()) {
            if (con != null) {
                System.out.println("[OK] Conexion establecida con exito mediante el driver sqlite-jdbc.");
                System.out.printf("AutoCommit activo por defecto: %b%n", con.getAutoCommit());
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] No se pudo conectar a SQLite: " + e.getMessage());
        }
    }
}
