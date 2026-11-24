/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ej1;

import com.mycompany.examenrepaso_jdbc.ConexionBD;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 *
 * @author adrian
 */
public class GestorEj1 {

    public boolean anadirUsuario(String user, String contrasena, String nombre, String apellidos, LocalDate fechanac) {
        String sql = "INSERT INTO usuarios (user, contrasena, nombre, apellidos, fechanac) "
                + "VALUES (?, MD5(?), ?, ?, ?)";
        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, user);
            ps.setString(2, contrasena);
            ps.setString(3, nombre);
            ps.setString(4, apellidos);
            ps.setDate(5, Date.valueOf(fechanac));
            if (ps.executeUpdate() > 0) {
                return true;
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                System.out.println("Error: El identificador (user) ya está en uso.");
            } else {
                System.out.println("Error al insertar el usuario en la base de datos: " + e.getMessage());
            }
        }
        return false;
    }
}
