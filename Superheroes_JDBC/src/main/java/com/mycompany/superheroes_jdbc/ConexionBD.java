/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.superheroes_jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author adrian
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
                throw new IllegalStateException(
                        "No se encuentra" + FICHERO + "en src/main/resources");
            }
            props.load(new InputStreamReader(in, StandardCharsets.UTF_8));
        } catch (IOException ex) {
            System.getLogger(ConexionBD.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return props;
    }

    private static String obligatoria(Properties props, String clave) {
        String valor = props.getProperty(clave);
        if (valor == null || valor.isBlank()) {
            throw new IllegalStateException(
                    "Falta la propiedad'" + clave + "' en " + FICHERO);
        }
        return valor.trim();
    }
}
