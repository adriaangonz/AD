package com.mycompany.practica_batch_callable;

import java.util.Scanner;

/**
 * Práctica de Batch, Transacciones, Procedimientos y Funciones con JDBC (BD: biblioteca).
 *
 * @author adrian
 */
public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean salir = false;

        while (!salir) {
            System.out.println("\n--- PRACTICA BATCH Y CALLABLE (biblioteca) ---");
            System.out.println("1. Insertar varios registros (Batch + Transaccion)");
            System.out.println("2. Procedimiento IN: prestar(socio, titulo)");
            System.out.println("3. Procedimiento OUT: libros_disponibles(titulo, OUT total)");
            System.out.println("4. Funcion: prestamos_activos(socio)");
            System.out.println("0. Salir");
            System.out.print("Elige una opcion: ");

            String op = sc.nextLine().trim();
            switch (op) {
                case "1" -> insertarEnBatch(sc);
                case "2" -> llamarPrestar(sc);
                case "3" -> llamarLibrosDisponibles(sc);
                case "4" -> llamarPrestamosActivos(sc);
                case "0" -> salir = true;
                default -> System.out.println("Opcion no valida.");
            }
        }
    }

    private static void insertarEnBatch(Scanner sc) {
        // TODO: setAutoCommit(false), addBatch(), executeBatch(), commit() / rollback()
    }

    private static void llamarPrestar(Scanner sc) {
        // TODO: CallableStatement "{call prestar(?, ?)}"
    }

    private static void llamarLibrosDisponibles(Scanner sc) {
        // TODO: CallableStatement "{call libros_disponibles(?, ?)}" + registerOutParameter
    }

    private static void llamarPrestamosActivos(Scanner sc) {
        // TODO: CallableStatement "{? = call prestamos_activos(?)}" + registerOutParameter
    }
}
