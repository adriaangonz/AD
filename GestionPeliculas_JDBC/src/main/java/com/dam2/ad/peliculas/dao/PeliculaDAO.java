package com.dam2.ad.peliculas.dao;

import com.dam2.ad.peliculas.conexion.ConexionBD;
import com.dam2.ad.peliculas.modelo.Pelicula;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) para la entidad Pelicula (tabla 'movies').
 * 
 * Sigue los patrones estrictos de diseno vistos en clase:
 * - Cada operacion abre su conexion en un bloque try-with-resources y la cierra al finalizar.
 * - Consultas parametrizadas con PreparedStatement y marcadores '?' para evitar inyeccion SQL.
 * - Mapeo de ResultSet a instancias de objetos del modelo.
 * - Manejo riguroso de excepciones SQLException.
 */
public class PeliculaDAO {

    /**
     * Recupera todas las peliculas ordenadas por id.
     *
     * @return Lista de peliculas registradas en la base de datos
     * @throws SQLException si ocurre un error en la ejecucion SQL
     */
    public List<Pelicula> listar() throws SQLException {
        String sql = "SELECT id, title, director, release_year, genre FROM movies ORDER BY id";
        List<Pelicula> peliculas = new ArrayList<>();

        // try-with-resources garantiza el cierre automatico de Connection, PreparedStatement y ResultSet
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String director = rs.getString("director");
                int releaseYear = rs.getInt("release_year");
                String genre = rs.getString("genre");

                peliculas.add(new Pelicula(id, title, director, releaseYear, genre));
            }
        }
        return peliculas;
    }

    /**
     * Inserta una nueva pelicula en la base de datos.
     * Obtiene y asigna el ID autogenerado mediante getGeneratedKeys().
     *
     * @param p Objeto Pelicula con los datos a insertar
     * @return true si se inserto correctamente, false en caso contrario
     * @throws SQLException si falla la insercion
     */
    public boolean agregar(Pelicula p) throws SQLException {
        String sql = "INSERT INTO movies (title, director, release_year, genre) VALUES (?, ?, ?, ?)";

        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, p.getTitle());
            ps.setString(2, p.getDirector());
            ps.setInt(3, p.getReleaseYear());
            ps.setString(4, p.getGenre());

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        p.setId(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;
        }
    }

    /**
     * Actualiza los datos de una pelicula existente a partir de su ID.
     *
     * @param p Objeto Pelicula con el ID y los nuevos valores
     * @return true si se actualizo alguna fila, false si la pelicula no existe
     * @throws SQLException si falla la actualizacion
     */
    public boolean actualizar(Pelicula p) throws SQLException {
        String sql = "UPDATE movies SET title = ?, director = ?, release_year = ?, genre = ? WHERE id = ?";

        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getTitle());
            ps.setString(2, p.getDirector());
            ps.setInt(3, p.getReleaseYear());
            ps.setString(4, p.getGenre());
            ps.setInt(5, p.getId());

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Elimina una pelicula de la base de datos por su ID.
     *
     * @param id Identificador de la pelicula a borrar
     * @return true si se borro la fila, false si no existia
     * @throws SQLException si ocurre un error
     */
    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM movies WHERE id = ?";

        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Busca una pelicula por su identificador primario.
     *
     * @param id Identificador de la pelicula
     * @return Objeto Pelicula si existe, o null si no se encuentra
     * @throws SQLException si ocurre un fallo en la consulta
     */
    public Pelicula buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, title, director, release_year, genre FROM movies WHERE id = ?";

        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Pelicula(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("director"),
                            rs.getInt("release_year"),
                            rs.getString("genre")
                    );
                }
            }
        }
        return null;
    }
}
