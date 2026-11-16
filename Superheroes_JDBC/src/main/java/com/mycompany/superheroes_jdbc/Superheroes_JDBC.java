/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.superheroes_jdbc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author adrian
 */
public class Superheroes_JDBC {

    private static List<Personaje> listaP = new ArrayList<>();
    private static List<Pelicula> listaM = new ArrayList<>();

    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);
        GestorBD g = new GestorBD();
        boolean salir = false;

        while (!salir) {

            mostrarMenu(g);
            try {
                Integer opcion = Integer.parseInt(teclado.nextLine());
                switch (opcion) {
                    case 1 ->
                        ejecutarOpcion1(g, teclado);
                    case 2 ->
                        ejecutarOpcion2(g, teclado);
                    case 3 ->
                        ejecutarOpcion3(g, teclado);
                    case 4 ->
                        ejecutarOpcion4(g, teclado);
                    case 0 -> {
                        salir = true;
                        System.out.println("Saliendo");
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println(e.toString());
            }
        }
    }

    private static void mostrarMenu(GestorBD g) {
        try {
            listaP = g.listarPersonajes();
            System.out.println("PERSONAJES");
            for (Personaje p : listaP) {
                System.out.println(p.toString());
            }
            listaM = g.listarPeliculas();
            System.out.println("PELICULAS");
            for (Pelicula p : listaM) {
                System.out.println(p.toString());
            }
            System.out.println("MENU");
            System.out.println("1. Añadir personaje");
            System.out.println("2. Añadir pelicula");
            System.out.println("3. Añadir actuacion");
            System.out.println("4. Eliminar actuacion");
            System.out.println("0. Salir");
            System.out.println("Elige una opcion: ");
        } catch (SQLException ex) {
            System.getLogger(Superheroes_JDBC.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

    }

    private static void ejecutarOpcion1(GestorBD g, Scanner teclado) {
        System.out.println("Intorduce el nombre:");
        String nombre = teclado.nextLine();
        for (Personaje p : listaP) {
            if (!p.getName().equalsIgnoreCase(nombre))  {
                System.out.println("Ese personaje ya existe");
                return;
            }
        }
        System.out.println("Introduce los poderes: ");
        String poderes = teclado.nextLine();
        System.out.println("Introduce la compañia: ");
        String compañia = teclado.nextLine();
        System.out.println("Introduce el origen: ");
        String origen = teclado.nextLine();
        System.out.println("Es heroe? (S/N)"); 
        boolean esHeroe = teclado.nextLine().equalsIgnoreCase("s");
        Personaje p = new Personaje(nombre, poderes, compañia, origen, esHeroe);
    }

    private static void ejecutarOpcion2(GestorBD g, Scanner teclado) {
    }

    private static void ejecutarOpcion3(GestorBD g, Scanner teclado) {
    }

    private static void ejecutarOpcion4(GestorBD g, Scanner teclado) {
    }

}
