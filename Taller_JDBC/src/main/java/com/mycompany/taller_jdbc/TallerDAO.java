/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.taller_jdbc;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.List;

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
        String sql = "INSERT INTO reparaciones (matricula, descripcion, fecha_entrada, fecha_salida, coste, id_cliente) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, r.getMatricula());
            ps.setString(2, r.getDescripcion());
            ps.setDate(3, Date.valueOf(r.getFecha_entrada()));
            if (r.getFecha_salida() == null) {
                ps.setNull(4, Types.DATE);
            } else {
                ps.setDate(4, Date.valueOf(r.getFecha_salida()));
            }
            if (r.getCoste() == null) {
                ps.setNull(5, Types.DECIMAL);
            } else {
                ps.setBigDecimal(5, r.getCoste());
            }
            if (r.getId_cliente()==null) {
                ps.setNull(6, Types.INTEGER);
            } else {
                ps.setInt(6, r.getId_cliente());
            }
            return ps.executeUpdate()>0;
        } catch (SQLException ex) {
            if (ex.getErrorCode() == 1452) {
                System.out.println("El id_cliente no existe");
            } else {
                System.getLogger(TallerDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
            return false;
        }
    }
    
    public void importarReparaciones(List<Reparacion> lista) {
        
    }
    
    public boolean importarCSV(String rutaArchivo) {
        String sql = "INSERT INTO reparaciones (matricula, descripcion, fecha_entrada, fecha_salida, coste, id_cliente) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.getConexion()) {
            List<String> lineas = Files.readAllLines(Path.of(rutaArchivo), StandardCharsets.UTF_8);
            con.setAutoCommit(false);
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                for (int i = 1; i < lineas.size(); i++) {
                    String linea = lineas.get(i);
                    if (linea.isBlank()) {
                        continue;
                    }
                    String[] datos = linea.split(";", -1);
                    ps.setString(1, datos[0].trim());
                    ps.setString(2, datos[1].trim());
                    ps.setDate(3, Date.valueOf(LocalDate.parse(datos[2])));
                    if (datos[3].isBlank()) {
                        ps.setNull(4, Types.DATE);
                    } else {
                        ps.setDate(4, Date.valueOf(LocalDate.parse(datos[3])));
                    }
                    if (datos[4].isBlank()) {
                        ps.setNull(5, Types.DECIMAL);
                    } else {
                        ps.setBigDecimal(5, new BigDecimal(datos[4]));
                    }
                    if (datos[5].isBlank()) {
                        ps.setNull(6, Types.INTEGER);
                    } else {
                        ps.setInt(6, Integer.parseInt(datos[5].trim()));
                    }
                    ps.addBatch();
                }
                ps.executeBatch();
                con.commit();
                return true;
            } catch (SQLException e) {
                con.rollback();
                System.out.println("eRROR");
                return false;
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException | IOException ex) {
            System.out.println("Error");
            return false;
        }
    }
}
