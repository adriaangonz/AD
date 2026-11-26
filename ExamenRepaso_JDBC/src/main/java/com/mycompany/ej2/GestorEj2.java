/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ej2;

import com.mycompany.examenrepaso_jdbc.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author adrian
 */
public class GestorEj2 {

    public List<Grupo> listarGrupos() throws SQLException {
        List<Grupo> lista = new ArrayList<>();
        String sql = "SELECT codgrupo, nombre, localidad, estilo, esgrupo, annograb, fechaEstreno, compania FROM grupos ORDER BY codgrupo";
        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int codgrupo = rs.getInt("codgrupo");
                String nombre = rs.getString("nombre");
                String localidad = rs.getString("localidad");
                String estilo = rs.getString("estilo");
                boolean esgrupo = rs.getInt("esgrupo") == 1 ? true : false;
                Integer annograb = rs.getObject("annograb", Integer.class);
                LocalDate fechaEstreno = rs.getObject("fechaEstreno", LocalDate.class);
                String compania = rs.getObject("compania", String.class);
                Grupo g = new Grupo(codgrupo, nombre, localidad, estilo, esgrupo, annograb, fechaEstreno, compania);
                lista.add(g);
            }
        }
        return lista;
    }

    public List<Cancion> listarCanciones() throws SQLException {
        List<Cancion> lista = new ArrayList<>();
        String sql = "SELECT c.numcancion, g.nombre AS grupo, c.duracion, c.titulo, c.total_votos FROM canciones c INNER JOIN grupos g ON c.grupo = g.codgrupo ORDER BY g.nombre, c.numCancion";
        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int numcancion = rs.getInt("numcancion");
                String grupo = rs.getString("grupo");
                LocalTime duracion = rs.getObject("duracion", LocalTime.class);
                String titulo = rs.getString("titulo");
                int total_votos = rs.getInt("total_votos");
                Cancion c = new Cancion(numcancion, grupo, duracion, titulo, total_votos);
                lista.add(c);
            }
        }
        return lista;
    }

    public Map<String, Integer> mostrarGruposCanciones() throws SQLException {
        Map<String, Integer> mapa = new LinkedHashMap<>();
        String sql = "SELECT g.nombre, COUNT(c.grupo) AS numcanciones FROM grupos g LEFT JOIN canciones c ON g.codgrupo = c.grupo GROUP BY g.codgrupo, g.nombre ORDER BY numcanciones DESC";
        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                mapa.put(rs.getString("nombre"), rs.getInt("numcanciones"));
            }
        }
        return mapa;
    }

    public List<Cancion> listarCancionesGrupo(String grupo) throws SQLException {
        List<Cancion> lista = new ArrayList<>();
        String sql = "SELECT c.numcancion, c.duracion, c.titulo FROM grupos g LEFT JOIN canciones c ON g.codgrupo = c.grupo WHERE g.nombre = ? ORDER BY c.numcancion";
        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, grupo);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                if (rs.getObject("numcancion") == null) {
                    return lista;
                }
                do {
                    Integer numcancion = rs.getObject("numcancion", Integer.class);
                    LocalTime duracion = rs.getObject("duracion", LocalTime.class);
                    String titulo = rs.getObject("titulo", String.class);
                    lista.add(new Cancion(numcancion, duracion, titulo));
                } while (rs.next());
            }
            return lista;
        }
    }

    public List<Cancion> listarMasVotadas() throws SQLException {
        List<Cancion> lista = new ArrayList<>();
        String sql = "SELECT c.titulo, g.nombre AS grupo, c.total_votos FROM canciones c INNER JOIN grupos g ON c.grupo = g.codgrupo ORDER BY c.total_votos DESC, c.titulo LIMIT 5";
        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String titulo = rs.getString("titulo");
                String grupo = rs.getString("grupo");
                int total_votos = rs.getInt("total_votos");
                Cancion c = new Cancion(titulo, grupo, total_votos);
                lista.add(c);
            }
        }
        return lista;
    }

    /**
     *
     * @return @throws SQLException
     */
    public List<String> listarGruposSinCancion() throws SQLException {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT g.nombre AS grupo FROM grupos g LEFT JOIN canciones c ON g.codgrupo = c.grupo WHERE c.grupo IS NULL ORDER BY g.nombre";
        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String grupo = rs.getString("grupo");
                lista.add(grupo);
            }
        }
        return lista;
    }

    //fecha, usuario, titulo, grupo
    public List<Voto> listarVotosRecientes() throws SQLException {
        List<Voto> lista = new ArrayList<>();
        String sql = "SELECT v.fecha, v.usuario, c.titulo, g.nombre FROM votos v "
                + "INNER JOIN canciones c ON v.cancion = c.numCancion "
                + "INNER JOIN grupos g ON c.grupo = g.codgrupo "
                + "ORDER BY v.fecha DESC, v.usuario LIMIT 5";
        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                LocalDate fecha = rs.getObject("fecha", LocalDate.class);
                String usuario = rs.getString("usuario");
                String titulo = rs.getString("titulo");
                String grupo = rs.getString("nombre");
                Voto v = new Voto(usuario, fecha, titulo, grupo);
                lista.add(v);
            }
        }
        return lista;
    }

    //num votos y numcacniones que se eliminan
    public int[] consultarCuantasElimina(String grupo) throws SQLException {
        int[] recuento = new int[2];
        String sql = "SELECT COUNT(DISTINCT c.numCancion) AS numCanciones, "
                + "COUNT(v.cancion) AS votos FROM grupos g LEFT JOIN canciones c ON "
                + "g.codgrupo = c.grupo LEFT JOIN votos v ON c.numCancion = "
                + "v.cancion WHERE g.nombre = ?";
        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, grupo);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                recuento[0] = rs.getInt("numCanciones");
                recuento[1] = rs.getInt("votos");
            }
        }
        return recuento;
    }

    // desconatar de num votos de cada usuario los votos dados a canciones del grupo
    // eliminar los votos de las canciones del grupo
    // se eliminan las canciones del grupo
    public int[] eliminarCanciones(String grupo) {
        int[] eliminados = new int[2];
        try (Connection con = ConexionBD.getConexion()) {
            con.setAutoCommit(false);
            try {
                String sql1 = "SELECT v.usuario, COUNT(*) AS cantidad FROM votos v JOIN canciones c ON v.cancion = c.numCancion "
                        + "JOIN grupos g ON c.grupo = g.codgrupo WHERE g.nombre = ? GROUP BY v.usuario";
                try (PreparedStatement ps1 = con.prepareStatement(sql1)) {
                    ps1.setString(1, grupo);
                    try (ResultSet rs = ps1.executeQuery()) {
                        String sql2 = "UPDATE usuarios SET numvotos = numvotos - ? WHERE user = ?";
                        try (PreparedStatement ps2 = con.prepareStatement(sql2)) {
                            while (rs.next()) {
                                ps2.setInt(1, rs.getInt("cantidad"));
                                ps2.setString(2, rs.getString("usuario"));
                                ps2.executeUpdate();

                            }
                        }

                    }
                }
                String sql3 = "DELETE v FROM votos v JOIN canciones ON cancion = numcancion JOIN grupos ON grupo = codgrupo WHERE nombre = ?";
                try (PreparedStatement ps3 = con.prepareStatement(sql3)) {
                    ps3.setString(1, grupo);
                    eliminados[0] = ps3.executeUpdate();
                }
                String sql4 = "DELETE c FROM canciones c JOIN grupos ON grupo = codgrupo WHERE nombre = ?";
                try (PreparedStatement ps4 = con.prepareStatement(sql4)) {
                    ps4.setString(1, grupo);
                    eliminados[1] = ps4.executeUpdate();
                }
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                System.getLogger(GestorEj2.class.getName()).log(System.Logger.Level.ERROR, (String) null, e);
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            System.getLogger(GestorEj2.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return eliminados;
    }

    //nombre estilo, anograb, fecha primera act, localidad, compania
    // pregunta cual quiere modificar y se pide valor y actualiza
    public Grupo mostrarDatosGrupo(String nombre) throws SQLException {
        List<Grupo> lista = listarGrupos();
        for (Grupo g : lista) {
            if (g.getNombre().equalsIgnoreCase(nombre)) {
                return g;
            }
        }
        return null;
    }

    public <T> void actualizarNombre(int columna, String nombre, T valor) throws SQLException {
        String sql = "";
        switch (columna) {
            case 1 ->
                sql = "UPDATE grupos SET nombre = ? WHERE nombre = ?";
            case 2 ->
                sql = "UPDATE grupos SET estilo = ? WHERE nombre = ?";
            case 3 ->
                sql = "UPDATE grupos SET annoGrab = ? WHERE nombre = ?";
            case 4 ->
                sql = "UPDATE grupos SET fechaEstreno = ? WHERE nombre = ?";
            case 5 ->
                sql = "UPDATE grupos SET localidad = ? WHERE nombre = ?";
            case 6 ->
                sql = "UPDATE grupos SET compania = ? WHERE nombre = ?";
        }
        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            if (valor == null) {
                ps.setNull(1, Types.NULL);
            } else {
                ps.setObject(1, valor);
            }
            ps.setInt(2, columna);
            ps.executeUpdate();
        }
    }
}
