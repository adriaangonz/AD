package com.dam2.ad.preguntastest.conexion;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Gestor de conexiones para la base de datos 'preguntastest'.
 * 
 * Sigue el patron estricto del profesor:
 * - Clase de utilidad no instanciable (constructor privado).
 * - Carga automatica de credenciales desde 'db.properties' en el classpath.
 * - getConexion() suministra conexiones que deben cerrarse con try-with-resources.
 */
public class ConexionBD {

    private static final String FICHERO = "/db.properties";
    private static final Properties PROPS = cargarPropiedades();

    private static final String URL = obligatoria(PROPS, "db.url");
    private static final String USUARIO = obligatoria(PROPS, "db.user");
    private static final String PASSWORD = PROPS.getProperty("db.password", "").trim();

    private ConexionBD() {
    }

    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }

    private static Properties cargarPropiedades() {
        Properties props = new Properties();
        try (InputStream in = ConexionBD.class.getResourceAsStream(FICHERO)) {
            if (in == null) {
                throw new IllegalStateException("No se encuentra " + FICHERO + " en src/main/resources (classpath)");
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
            throw new IllegalStateException("Falta la propiedad obligatoria '" + clave + "' en " + FICHERO);
        }
        return valor.trim();
    }
}
