/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.taller_jdbc;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

/**
 *
 * @author adrian
 */
public class TallerDAO {

    public boolean altaCliente(Cliente c) {
        String sql = "INSERT INTO clientes (dni, nombre, telefono, saldo_bono) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getDni());
            ps.setString(2, c.getNombre());
            if (c.getTelefono() == null) {
                ps.setNull(3, Types.VARCHAR);
            } else {
                ps.setString(3, c.getTelefono());
            }
            ps.setBigDecimal(4, c.getSaldo_bono());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            if (ex.getErrorCode() == 1062) {
                System.out.println("ESe dni ya existe");
            } else {
                System.getLogger(TallerDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
            return false;
        }
    }
    
    public boolean registrarReparacion(Reparacion r) {
        
    }
}
