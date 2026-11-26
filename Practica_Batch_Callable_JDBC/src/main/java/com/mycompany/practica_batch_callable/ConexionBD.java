package com.mycompany.practica_batch_callable;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Clase de conexión a la base de datos (para rellenar).
 *
 * @author adrian
 */
public class ConexionBD {

    private static final String FICHERO = "/db.properties";
    private static final Properties PROPS = cargarPropiedades();
    private static final String URL = obligatoria(PROPS, "db.url");
    private static final String USER = obligatoria(PROPS, "db.user");
    private static final String PASSWORD = obligatoria(PROPS, "db.password");

    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private static Properties cargarPropiedades() {
        Properties props = new Properties();
        try (InputStream in = ConexionBD.class.getResourceAsStream(FICHERO)) {
            if (in==null) {
                throw new IllegalStateException("No se encuentra " + FICHERO);
            }
            props.load(new InputStreamReader(in, StandardCharsets.UTF_8));
        } catch (IOException ex) {
            System.getLogger(ConexionBD.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return props;
    }

    private static String obligatoria(Properties props, String valor) {
        String texto = props.getProperty(valor);
        if (texto == null || texto.isBlank()) {
            throw new IllegalStateException("La propiedad " + texto + " esta vacia");
        }
        return texto.trim();
    }
}
