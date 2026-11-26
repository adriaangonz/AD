package com.mycompany.examenrepaso_jdbc;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Utilidad para leer y validar datos introducidos por consola.
 *
 * @author adrian
 */
public class Teclado {

    private static final Scanner teclado = new Scanner(System.in);

    private static String leerLinea(String msg) {
        System.out.print(msg);
        return teclado.nextLine().trim();
    }

    /** Lee un texto no vacío. */
    public static String leerString(String msg) {
        while (true) {
            String linea = leerLinea(msg);
            if (!linea.isEmpty()) {
                return linea;
            }
            System.out.println("Error: no puede estar vacío.");
        }
    }

    /** Lee un texto; si se deja vacío (Enter), devuelve null. */
    public static String leerStringNullable(String msg) {
        String linea = leerLinea(msg);
        return linea.isEmpty() ? null : linea;
    }

    /** Lee un número entero obligatorio. */
    public static int leerInt(String msg) {
        while (true) {
            try {
                return Integer.parseInt(leerLinea(msg));
            } catch (NumberFormatException e) {
                System.out.println("Error: introduce un número entero válido.");
            }
        }
    }

    /** Lee un número entero; si se deja vacío (Enter), devuelve null. */
    public static Integer leerIntNullable(String msg) {
        while (true) {
            String linea = leerLinea(msg);
            if (linea.isEmpty()) {
                return null;
            }
            try {
                return Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                System.out.println("Error: introduce un número entero válido (o deja vacío).");
            }
        }
    }

    /** Lee una fecha en formato AAAA-MM-DD. */
    public static LocalDate leerFecha(String msg) {
        while (true) {
            try {
                return LocalDate.parse(leerLinea(msg));
            } catch (DateTimeParseException e) {
                System.out.println("Error: introduce una fecha válida (AAAA-MM-DD).");
            }
        }
    }

    /** Lee una fecha en formato AAAA-MM-DD; si se deja vacía (Enter), devuelve null. */
    public static LocalDate leerFechaNullable(String msg) {
        while (true) {
            String linea = leerLinea(msg);
            if (linea.isEmpty()) {
                return null;
            }
            try {
                return LocalDate.parse(linea);
            } catch (DateTimeParseException e) {
                System.out.println("Error: introduce una fecha válida (AAAA-MM-DD) o deja vacío.");
            }
        }
    }

    /** Lee una confirmación S/N (acepta S, SI, TRUE / N, NO, FALSE). */
    public static boolean leerBoolean(String msg) {
        while (true) {
            String resp = leerString(msg).toLowerCase();
            if (resp.equals("s") || resp.equals("si") || resp.equals("true")) {
                return true;
            }
            if (resp.equals("n") || resp.equals("no") || resp.equals("false")) {
                return false;
            }
            System.out.println("Error: responde S o N.");
        }
    }
}
