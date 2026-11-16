package com.daw.prog.biblioteca_mysql;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

/**
 *
 * @author subie
 */
public class MetodosClase {

    public void insertarDuplicado() {//lo adecuado sería que el método intentara dar de alta un socio y si se diera la circunsatncia de que ya existe, advertir de ello

        String sql = "INSERT INTO socios (id_socio, nombre) VALUES (?, ?)";
        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "S001");
            ps.setString(2, "Ana García");
            ps.executeUpdate();
        } catch (SQLException e) {
            String estado = e.getSQLState();
            if (estado != null && estado.startsWith("23")) {
                System.err.println("El socio ya existe");
            } else {
                for (SQLException ex = e; ex != null; ex = ex.getNextException()) {
                    System.err.printf("[%s] %d: %s%n",
                            ex.getSQLState(), ex.getErrorCode(), ex.getMessage());
                }
            }
        }
    }

    private String leerScript(String recurso) throws IOException {
        try (InputStream in = MetodosClase.class.getResourceAsStream(recurso)) {
            if (in == null) {
                throw new IOException("no se encuetnra " + recurso + "en fichero");
            }
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    public void ejecutarScript() throws SQLException, IOException {
        System.out.println("Ejecución del scrip script.sql de la teoría de la presentación");
        String script = leerScript("/script.sql");
        System.out.println("Script leído:\n" + script);   // nunca está de más, al principio, verificar si lee el script correcto

        try (Connection con = ConexionBD.getConexion()) {
            con.setAutoCommit(false);             // inicio de la transacción
            try (Statement st = con.createStatement()) {
                st.execute(script);               // todas las instrucciones
                con.commit();                     // todo correcto: se confirma
            } catch (SQLException e) {
                con.rollback();                   // algo falló: se deshace todo
                System.err.println("Script anulado: " + e.getMessage());
            }
        }

    }

    /**
     * Llama a prestar(soc, tit). Devuelve true si el préstamo se ha registrado.
     */
    public boolean prestar(String socio, String titulo) {
        String sql = "{call prestar(?, ?)}";          // las llaves son sintaxis de escape JDBC
        try (Connection con = ConexionBD.getConexion(); CallableStatement cs = con.prepareCall(sql)) {
            cs.setString(1, socio);                    // 1.er ?  → IN soc
            cs.setString(2, titulo);                   // 2.º ?   → IN tit -> es el título del libro (NO es el id)
            cs.execute();
            return true;
        } catch (SQLException e) {
            if ("45000".equals(e.getSQLState())) {
                System.err.println("Error del procedimiento: " + e.getMessage());
            } else {
                System.err.printf("No se ha podido prestar [%s] %d: %s%n",
                        e.getSQLState(), e.getErrorCode(), e.getMessage());
            }
            return false;
        }
    }

    /**
     * Llama a libros_disponibles(IN tit, OUT total).
     */
    public int librosDisponibles(String titulo) throws SQLException {
        try (Connection con = ConexionBD.getConexion(); CallableStatement cs = con.prepareCall("{call libros_disponibles(?, ?)}")) {
            cs.setString(1, titulo);                        // IN
            cs.registerOutParameter(2, Types.INTEGER);      // OUT: se registra ANTES de ejecutar
            cs.execute();
            return cs.getInt(2);                            // se lee DESPUÉS de ejecutar
        }
    }

    /**
     * Llama a la función prestamos_activos(soc).
     */
    public int prestamosActivos(String socio) throws SQLException {
        try (Connection con = ConexionBD.getConexion(); CallableStatement cs = con.prepareCall("{? = call prestamos_activos(?)}")) {
            cs.registerOutParameter(1, Types.INTEGER);      // el valor de retorno es el 1.er ?
            cs.setString(2, socio);
            cs.execute();
            return cs.getInt(1);
        }
    }

}
