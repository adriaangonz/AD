package com.dam2.ad.peliculas.conexion;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Proporciona conexiones a la base de datos siguiendo el patron y estilo del profesor.
 * 
 * Principios aplicados:
 * 1. Clase de utilidad con constructor privado para evitar instanciacion indebida.
 * 2. Carga lazy/estatica de propiedades desde 'src/main/resources/db.properties'.
 * 3. Validacion obligatoria de credenciales con mensajes de error descriptivos.
 * 4. Quien solicita la conexion mediante getConexion() es el responsable de cerrarla (try-with-resources).
 */
public class ConexionBD {

    private static final String FICHERO = "/db.properties";

    // Se inicializan una sola vez al cargar la clase en memoria
    private static final Properties PROPS = cargarPropiedades();

    private static final String URL = obligatoria(PROPS, "db.url");
    private static final String USUARIO = obligatoria(PROPS, "db.user");
    private static final String PASSWORD = PROPS.getProperty("db.password", "").trim();

    // Constructor privado: clase puramente utilitaria
    private ConexionBD() {
    }

    /**
     * Devuelve una conexion nueva y abierta con la base de datos.
     * Quien invoca este metodo debe asegurarse de cerrarla adecuadamente.
     *
     * @return Connection abierta
     * @throws SQLException si ocurre un error al conectar con el servidor
     */
    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }

    /**
     * Carga el archivo de propiedades desde el classpath (src/main/resources).
     */
    private static Properties cargarPropiedades() {
        Properties props = new Properties();
        try (InputStream in = ConexionBD.class.getResourceAsStream(FICHERO)) {
            if (in == null) {
                throw new IllegalStateException(
                        "No se encuentra el fichero " + FICHERO + " en src/main/resources (classpath)");
            }
            props.load(new InputStreamReader(in, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new IllegalStateException("Error de lectura al cargar " + FICHERO, e);
        }
        return props;
    }

    /**
     * Valida que una clave exista y contenga un valor no vacio en las propiedades.
     */
    private static String obligatoria(Properties props, String clave) {
        String valor = props.getProperty(clave);
        if (valor == null || valor.isBlank()) {
            throw new IllegalStateException("Falta la propiedad obligatoria '" + clave + "' en " + FICHERO);
        }
        return valor.trim();
    }
}
