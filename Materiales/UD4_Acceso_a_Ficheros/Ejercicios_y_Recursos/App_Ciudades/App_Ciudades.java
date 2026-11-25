/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.daw.prog.archivos_serializables;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author 
 */
public class App_Ciudades {

    public static void main(String[] args) {

        Scanner entrada = new Scanner(System.in);
        GestorCiudades g = new GestorCiudades(Path.of("ciudades.obj"));

        Path csv = verificarRuta(g, entrada);

        if (csv == null) {
            System.out.println("La ruta no corresponde a un fichero existente. Terminamos.");
        } else {
            gestionarOpcion(entrada, csv, g);
        }

        entrada.close();
    }

    private static Path verificarRuta(GestorCiudades g, Scanner entrada) {
        System.out.println("Introduce la ruta del fichero CSV");

        try {
            Path ruta = Path.of(entrada.nextLine());
            return g.esFicheroValido(ruta) ? ruta : null;
        } catch (InvalidPathException e) {
            return null;
        }
    }

    private static void mostrarMenu() {
        System.out.println("OPCIONES DEL MENÚ");
        System.out.println("1.- Añadir ciudad");
        System.out.println("2.- Leer contenido");
        System.out.println("3.- Obtener repetidas");
        System.out.println("4.- Eliminar ciudad");
        System.out.println("5.- Obtener más poblada");
        System.out.println("6.- Exportar a CSV");//añado una por mejorar el enunciado
        System.out.println("0.- Salir");
        System.out.println("Elige una opción");
    }

    private static void gestionarOpcion(Scanner entrada, Path csv, GestorCiudades g) {
        boolean salir = false;

        while (!salir) {
            mostrarMenu();

            try {
                int opcion = Integer.parseInt(entrada.nextLine());

                switch (opcion) {
                    case 1 -> ejecutarOpcion1(csv, g, entrada);
                    case 2 -> ejecutarOpcion2(g);
                    case 3 -> ejecutarOpcion3(g);
                    case 4 -> ejecutarOpcion4(g, entrada);
                    case 5 -> ejecutarOpcion5(g);
                    case 6 -> ejecutarOpcion6(g, entrada);
                    case 0 -> {
                        System.out.println("Saliendo....");
                        salir = true;
                    }
                    default -> System.out.println("Opción no válida");
                }

            } catch (NumberFormatException e) {
                System.out.println("Las opciones sólo pueden ser enteros");
            }
        }
    }

    // opción 1 -> añadir ciudad desde una línea del CSV
    private static void ejecutarOpcion1(Path csv, GestorCiudades g, Scanner entrada) {
        System.out.println("Introduce el número de línea del fichero CSV");

        try {
            int linea = Integer.parseInt(entrada.nextLine());
            City ciudad = g.leerCiudadCSV(csv, linea);

            if (ciudad == null) {
                System.out.println("La línea " + linea + " no corresponde a ninguna ciudad");
            } else {
                g.anadirCiudad(ciudad);
                System.out.println("Ciudad añadida: " + ciudad);
            }

        } catch (NumberFormatException e) {
            System.out.println("La línea debe ser un número entero");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // opción 2 -> mostrar todas las ciudades, ordenadas
    private static void ejecutarOpcion2(GestorCiudades g) {
        try {
            if (!g.hayCiudades()) {
                System.out.println("No hay ciudades almacenadas");
                return;
            }

            List<City> ciudades = g.obtenerOrdenadas();
            for (City ciudad : ciudades) {
                System.out.println(ciudad);
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // opción 3 -> ciudades repetidas
    private static void ejecutarOpcion3(GestorCiudades g) {
        
    }

    // opción 4 -> eliminar ciudad
    private static void ejecutarOpcion4(GestorCiudades g, Scanner entrada) {
        
    }

    // opción 5 -> ciudad más poblada
    private static void ejecutarOpcion5(GestorCiudades g) {
       
    }
    
        // opción 6 -> exportar todas las ciudades a un CSV nuevo
    private static void ejecutarOpcion6(GestorCiudades g, Scanner entrada) {
        

    
    
}
}
