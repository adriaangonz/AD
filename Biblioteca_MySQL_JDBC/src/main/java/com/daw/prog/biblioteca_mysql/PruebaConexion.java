package com.daw.prog.biblioteca_mysql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;


/**
 * comprobación del funcionamiento de la conexión a la BD
 * no es necesario hacerlo siempre, lo añado por claridad didáctica
 * @author subie
 */
public class PruebaConexion {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try (Connection con = ConexionBD.getConexion()) {
            System.out.println("Conectados con la base de datos con éxito");
            
            DatabaseMetaData datos = con.getMetaData();
            
            System.out.println(datos.getUserName());
            System.out.println(datos.getURL());
          
            
            
        } catch (SQLException e) {
            System.out.println("Error " + e.getMessage());
        }

    }

}
