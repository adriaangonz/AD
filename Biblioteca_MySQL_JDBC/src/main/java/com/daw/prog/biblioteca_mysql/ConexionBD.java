package com.daw.prog.biblioteca_mysql;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

/**
 * proporciona conexiones a la Base de datos
 * @author subie
 */
public class ConexionBD {

    private static final String FICHERO = "/db.properties";

    // Se inicializan en este orden, una sola vez, al usar la clase por primera vez
    private static final Properties PROPS = cargarPropiedades();

    private static final String URL = obligatoria(PROPS, "db.url");
    private static final String USUARIO = obligatoria(PROPS, "db.user");
    private static final String PASSWORD = PROPS.getProperty("db.password", "").trim();

    // Clase de utilidad: no se instancia. el constructor es así para que nadie pueda hacer nada
    private ConexionBD() {
    }

    /**
     * Devuelve una conexión nueva. Quien la pide es responsable de cerrarla.
     *
     * @return
     * @throws java.sql.SQLException
     */
    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }

    private static Properties cargarPropiedades() {
        Properties props = new Properties();
        try (InputStream in = ConexionBD.class.getResourceAsStream(FICHERO)) {
            if (in == null) {
                throw new IllegalStateException(
                        "No se encuentra " + FICHERO + " en src/main/resources");//en realidad en el classpath
            }
            props.load(new InputStreamReader(in, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new IllegalStateException("Error leyendo " + FICHERO, e);
        }
        return props;
    }

    private static String obligatoria(Properties props, String clave) {
        String valor = props.getProperty(clave);
        if (valor == null || valor.isBlank()) {
            throw new IllegalStateException(
                    "Falta la propiedad '" + clave + "' en " + FICHERO);
        }
        return valor.trim();
    }

    PreparedStatement prepareStatement(String sql) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
