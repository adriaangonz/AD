/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.examenrepaso_jdbc;

import com.mycompany.ej1.GestorEj1;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Scanner;

/**
 *
 * @author adrian
 */
public class Main {

    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        GestorEj1 gestor1 = new GestorEj1();
        boolean salir = false;
        try (Connection con = ConexionBD.getConexion()) {
            if (con.isValid(2)) {
                System.out.println("Conexión correcta con la base de datos.");
            }
        } catch (SQLException e) {
            System.out.println("No hay conexión con la base de datos: "
                    + e.getMessage());
            return; // Opcional: detiene el programa si no hay servidor
        }

        while (!salir) {
            mostrarMenu();
            try {
                int opcion = Integer.parseInt(teclado.nextLine());
                switch (opcion) {
                    case 1 ->
                        ejecutarOpcion1(gestor1, teclado);
                    case 4 -> {
                        salir = true;
                        System.out.println("saliendo");
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println(e.toString());
            }
        }
    }

    private static void mostrarMenu() {
        System.out.println("1. Ejercicio 1");
        System.out.println("2. Ejercicio 2");
        System.out.println("3. Ejercicio 3");
        System.out.println("4. Salir");
        System.out.println("eligue una opcion");
    }

    private static void ejecutarOpcion1(GestorEj1 gestor1, Scanner teclado) {
        System.out.println("Introduce el usuario");
        String usuario = null;
        while (usuario == null) {
            usuario = teclado.nextLine();
            if (usuario.length() > 15) {
                usuario = null;
                System.out.println("No puede superar los 15 caracteres");
            }
        }
        System.out.println("Introduce la contraseña");
        String contrasena = teclado.nextLine();

        System.out.println("Introduce el nombre");
        String nombre = null;
        while (nombre == null) {
            nombre = teclado.nextLine();
            if (nombre.length() > 15) {
                nombre = null;
                System.out.println("No puede superar los 15 caracteres");
            }
        }

        System.out.println("Introduce los apellidos");
        String apellidos = null;
        while (apellidos == null) {
            apellidos = teclado.nextLine();
            if (apellidos.length() > 30) {
                apellidos = null;
                System.out.println("No puede superar los 30 caracteres");
            }
        }

        System.out.println("Introduce la fecha de nacimiento (AAAA-MM-DD)");
        LocalDate fechanac = null;
        while (fechanac == null) {
            try {
                fechanac = LocalDate.parse(teclado.nextLine(),
                        DateTimeFormatter.ofPattern("uuuu-MM-dd")
                                .withResolverStyle(ResolverStyle.STRICT));
            } catch (DateTimeParseException e) {
                fechanac = null;
                System.out.println("No cumple el formato");
            }
        }
        if (gestor1.anadirUsuario(usuario, contrasena, nombre, apellidos, fechanac)) {
            System.out.println("Usuario añadido correctamente");
        }
    }
}
