
package com.mycompany.conexion.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author alumno
 */
public class ConexionSqlite {


     

    public static void main(String[] args) {
        // Windows: la carpeta temporal bloquea la DLL de SQLite; usamos otra
System.setProperty("org.sqlite.tmpdir", System.getProperty("user.home") + "\\Documents");//en windows la tengo que añadir porque da algunos problemas en una carpeta temporal
        // CORRIGE ESTA RUTA: Pon el camino exacto hacia tu archivo biblioteca.db
        // Ejemplo en Windows: "C:\\Proyectos\\biblioteca.db" (usa doble barra)
        String rutaBaseDatos = "biblioteca.db"; 
        String url = "jdbc:sqlite:" + rutaBaseDatos;
        
        System.out.println("Intentando conectar a: " + url);

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                System.out.println("Conexión establecida. Java se ha conectado correctamente a tu base de datos.");
            }
        } catch (SQLException e) {
            System.out.println("ERROR DE CONEXIÓN: No se pudo conectar a la base de datos.");
            System.out.println("Detalle del error: " + e.getMessage());
        }
    }
}
    
