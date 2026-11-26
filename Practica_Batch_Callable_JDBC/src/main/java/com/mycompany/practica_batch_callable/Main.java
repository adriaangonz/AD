package com.mycompany.practica_batch_callable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Práctica de Batch, Transacciones, Procedimientos y Funciones con JDBC (BD:
 * biblioteca).
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
                case "1" ->
                    insertarEnBatch(sc);
                case "2" ->
                    llamarPrestar(sc);
                case "3" ->
                    llamarLibrosDisponibles(sc);
                case "4" ->
                    llamarPrestamosActivos(sc);
                case "0" ->
                    salir = true;
                default ->
                    System.out.println("Opcion no valida.");
            }
        }
    }

    private static void insertarEnBatch(Scanner sc) {
        List<Libro> lista = new ArrayList<>();
        boolean anadir = true;
        while (anadir) {

            Libro l = new Libro();
            System.out.println("Introduce el titulo");
            l.setTitulo(sc.nextLine());
            System.out.println("Introduce el autor");
            l.setAutor(sc.nextLine());
            System.out.println("Introduce el numero de ejemplares");
            l.setEjemplares(Integer.parseInt(sc.nextLine()));
            System.out.println("Introduce cuantos hay disponibles");
            l.setDisponibles(Integer.parseInt(sc.nextLine()));
            lista.add(l);
            System.out.println("Quieresañadir otro libro? s/n");
            if (!sc.nextLine().equalsIgnoreCase("s")) {
                anadir = false;
            }
        }
        String sql = "INSERT INTO libros (titulo, autor, ejemplares, disponibles) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexionBD.getConexion()) {
            con.setAutoCommit(false);
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                for (Libro l : lista) {
                    ps.setString(1, l.getTitulo());
                    ps.setString(2, l.getAutor());
                    ps.setInt(3, l.getEjemplares());
                    ps.setInt(4, l.getDisponibles());
                    ps.addBatch();
                }
                ps.executeBatch();
                con.commit();
                System.out.println("Añadidos " + lista.size() + " libros");
            } catch (SQLException e) {
                con.rollback();
                System.out.println("ERROR EN EL BATCH");
                if (e.getErrorCode()==1062) {
                    System.out.println("algun titulo ya existe en la bd");
                }
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            System.getLogger(Main.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
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
