package veterinaria;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Proporciona conexiones a la base de datos. Los datos de conexión se leen
 * de src/main/resources/db.properties.
 */
public class ConexionBD {

    private static final String FICHERO = "/db.properties";

    // Se inicializan en este orden, una sola vez, al usar la clase por primera vez
    private static final Properties PROPS = cargarPropiedades();

    private static final String URL = obligatoria(PROPS, "db.url");
    private static final String USUARIO = obligatoria(PROPS, "db.user");
    private static final String PASSWORD = PROPS.getProperty("db.password", "");

    // Clase de utilidad: no se instancia
    private ConexionBD() {
    }

    /**
     * Devuelve una conexión nueva. Quien la pide es responsable de cerrarla.
     *
     * @return conexión abierta con la base de datos
     * @throws SQLException si no se puede conectar
     */
    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }

    private static Properties cargarPropiedades() {
        Properties props = new Properties();
        try (InputStream in = ConexionBD.class.getResourceAsStream(FICHERO)) {
            if (in == null) {
                throw new IllegalStateException("No se encuentra " + FICHERO + " en el classpath (src/main/resources)");
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
            throw new IllegalStateException("Falta la propiedad '" + clave + "' en " + FICHERO);
        }
        return valor.trim();
    }
}
