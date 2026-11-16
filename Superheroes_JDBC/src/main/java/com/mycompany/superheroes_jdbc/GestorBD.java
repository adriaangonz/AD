/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.superheroes_jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author adrian
 */
public class GestorBD {

    public List<Personaje> listarPersonajes() throws SQLException {

        String sql = "SELECT id, name, powers, company, origin, isHeroe"
                + " FROM characters ORDER BY id";
        List<Personaje> lista = new ArrayList<>();
        try (Statement st = ConexionBD.getConexion().createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Personaje p = new Personaje(rs.getInt("id"), rs.getString("name"), rs.getString("powers"), rs.getString("company"), rs.getString("origin"), rs.getInt("isHeroe"));
                lista.add(p);
            }
        }
        return lista;
    }

    public List<Pelicula> listarPeliculas() throws SQLException {

        String sql = "SELECT id, title, duration, year, producer"
                + " FROM movies ORDER BY id";
        List<Pelicula> lista = new ArrayList<>();
        try (Statement st = ConexionBD.getConexion().createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Pelicula p = new Pelicula(rs.getInt("id"), rs.getString("title"), rs.getInt("duration"), rs.getInt("duration"), rs.getString("producer"));
                lista.add(p);
            }
        }
        return lista;
    }

    public List<Actuaciones> listarActuaciones() throws SQLException {
        String sql = "SELECT id, character_id, movie_id, minutes_in_movie, main_character, actor FROM acts ORDER BY id";
        List<Actuaciones> lista = new ArrayList<>();
        try (Statement st = ConexionBD.getConexion().createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Actuaciones a = new Actuaciones(rs.getInt("id"), rs.getInt("character_id"), rs.getInt("movie_id"), rs.getInt("minutes_in_movie"), rs.getBoolean("main_character"), rs.getString("actor"));
                lista.add(a);
            }
        }
        return lista;
    }

    public boolean insertarPersonaje(String nombre, String poderes, String compania, String origen, int isHeroe) throws SQLException {
        String sql = "INSERT INTO characters (name, powers, company, origin, isHeroe) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, poderes);
            ps.setString(3, compania);
            ps.setString(4, origen);
            ps.setInt(5, isHeroe);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean insertarPelicula(String titulo, int duracion, int ano, String productora) throws SQLException {
        String sql = "INSERT INTO movies (title, duration, year, producer) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = ConexionBD.getConexion().prepareStatement(sql)) {
            ps.setString(1, titulo);
            ps.setInt(2, duracion);
            ps.setInt(3, ano);
            ps.setString(4, productora);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean insertarActuacion(int id_personaje, int id_pelicula, int duracion, boolean personaje_principal, String actor) throws SQLException {
        String sql = "INSERT INTO acts (character_id, movie_id, minutes_in_movie, main_character, actor) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = ConexionBD.getConexion().prepareStatement(sql)) {
            ps.setInt(1, id_personaje);
            ps.setInt(2, id_pelicula);
            ps.setInt(3, duracion);
            ps.setBoolean(4, personaje_principal);
            ps.setString(5, actor);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminarActuacion(int id_personaje, int id_pelicula) throws SQLException {
        String sql = "DELETE FROM acts WHERE character_id = ? AND movie_id = ?";
        try (PreparedStatement ps = ConexionBD.getConexion().prepareStatement(sql)) {
            ps.setInt(1, id_personaje);
            ps.setInt(2, id_pelicula);
            return ps.executeUpdate() > 0;
        }
    }
}
