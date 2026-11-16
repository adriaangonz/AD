/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.conexiones_jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 *
 * @author adrian
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
