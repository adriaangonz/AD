package com.dam2.ad.peliculas;

import com.dam2.ad.peliculas.dao.PeliculaDAO;
import com.dam2.ad.peliculas.modelo.Pelicula;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * Clase principal que gestiona la interfaz por consola mediante un menu interactivo
 * tal como se solicita en el enunciado del ejercicio.
 * 
 * Estructura del Menu:
 * 1. Actualizar
 * 2. Eliminar
 * 3. Agregar
 * 4. Listar
 * 5. Salir
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final PeliculaDAO peliculaDAO = new PeliculaDAO();

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("   APLICACION: GESTION DE PELICULAS CON JDBC     ");
        System.out.println("==================================================");

        boolean salir = false;
        while (!salir) {
            mostrarMenu();
            int opcion = leerEntero("Seleccione una opcion (1-5): ");

            switch (opcion) {
                case 1 -> actualizarPelicula();
                case 2 -> eliminarPelicula();
                case 3 -> agregarPelicula();
                case 4 -> listarPeliculas();
                case 5 -> {
                    salir = true;
                    System.out.println("\n¡Gracias por utilizar la aplicacion de peliculas! Saliendo...");
                }
                default -> System.out.println("\n[AVISO] Opcion no valida. Elija un numero entre 1 y 5.");
            }
        }
    }

    private static void mostrarMenu() {
        System.out.println("\n--------------------------------");
        System.out.println("      Gestion de peliculas      ");
        System.out.println("--------------------------------");
        System.out.println("1. Actualizar");
        System.out.println("2. Eliminar");
        System.out.println("3. Agregar");
        System.out.println("4. Listar");
        System.out.println("5. Salir");
        System.out.println("--------------------------------");
    }

    /**
     * Opcion 1: Actualizar una pelicula existente solicitando su ID y nuevos valores.
     */
    private static void actualizarPelicula() {
        System.out.println("\n--- ACTUALIZAR PELICULA ---");
        int id = leerEntero("Introduce el ID de la pelicula a actualizar: ");

        try {
            Pelicula existente = peliculaDAO.buscarPorId(id);
            if (existente == null) {
                System.out.printf("[ERROR] No se ha encontrado ninguna pelicula con el ID %d.%n", id);
                return;
            }

            System.out.println("Pelicula actual: " + existente);
            System.out.println("Introduce los nuevos datos (pulsa ENTER para mantener el valor actual):");

            System.out.print("Nuevo titulo [" + existente.getTitle() + "]: ");
            String titulo = scanner.nextLine().trim();
            if (titulo.isEmpty()) {
                titulo = existente.getTitle();
            }

            System.out.print("Nuevo director [" + existente.getDirector() + "]: ");
            String director = scanner.nextLine().trim();
            if (director.isEmpty()) {
                director = existente.getDirector();
            }

            System.out.print("Nuevo anio [" + existente.getReleaseYear() + "]: ");
            String anioStr = scanner.nextLine().trim();
            int anio = anioStr.isEmpty() ? existente.getReleaseYear() : Integer.parseInt(anioStr);

            System.out.print("Nuevo genero [" + existente.getGenre() + "]: ");
            String genero = scanner.nextLine().trim();
            if (genero.isEmpty()) {
                genero = existente.getGenre();
            }

            Pelicula actualizada = new Pelicula(id, titulo, director, anio, genero);
            boolean ok = peliculaDAO.actualizar(actualizada);
            if (ok) {
                System.out.println("[OK] Pelicula actualizada con exito en la base de datos.");
            } else {
                System.out.println("[ERROR] No se pudo actualizar el registro.");
            }

        } catch (NumberFormatException e) {
            System.out.println("[ERROR] El anio debe ser un valor numerico valido.");
        } catch (SQLException e) {
            System.err.printf("[ERROR SQL %d - %s]: %s%n", e.getErrorCode(), e.getSQLState(), e.getMessage());
        }
    }

    /**
     * Opcion 2: Eliminar una pelicula solicitando su ID.
     */
    private static void eliminarPelicula() {
        System.out.println("\n--- ELIMINAR PELICULA ---");
        int id = leerEntero("Introduce el ID de la pelicula a eliminar: ");

        try {
            Pelicula p = peliculaDAO.buscarPorId(id);
            if (p == null) {
                System.out.printf("[ERROR] No existe la pelicula con ID %d.%n", id);
                return;
            }

            System.out.println("Pelicula seleccionada: " + p);
            System.out.print("¿Estas seguro de eliminarla permanentemente? (s/N): ");
            String confirmacion = scanner.nextLine().trim().toLowerCase();

            if (confirmacion.equals("s") || confirmacion.equals("si")) {
                boolean ok = peliculaDAO.eliminar(id);
                if (ok) {
                    System.out.println("[OK] Pelicula eliminada satisfactoriamente.");
                } else {
                    System.out.println("[ERROR] No se pudo eliminar la pelicula.");
                }
            } else {
                System.out.println("[INFO] Operacion cancelada por el usuario.");
            }

        } catch (SQLException e) {
            System.err.printf("[ERROR SQL %d - %s]: %s%n", e.getErrorCode(), e.getSQLState(), e.getMessage());
        }
    }

    /**
     * Opcion 3: Agregar una nueva pelicula a la base de datos.
     */
    private static void agregarPelicula() {
        System.out.println("\n--- AGREGAR NUEVA PELICULA ---");

        System.out.print("Titulo: ");
        String titulo = scanner.nextLine().trim();
        if (titulo.isEmpty()) {
            System.out.println("[ERROR] El titulo es un campo obligatorio.");
            return;
        }

        System.out.print("Director: ");
        String director = scanner.nextLine().trim();

        int anio = leerEntero("Anio de estreno: ");

        System.out.print("Genero: ");
        String genero = scanner.nextLine().trim();

        Pelicula nueva = new Pelicula(titulo, director, anio, genero);
        try {
            boolean ok = peliculaDAO.agregar(nueva);
            if (ok) {
                System.out.printf("[OK] Pelicula agregada correctamente con ID asignado: %d%n", nueva.getId());
            } else {
                System.out.println("[ERROR] No se pudo insertar la pelicula.");
            }
        } catch (SQLException e) {
            System.err.printf("[ERROR SQL %d - %s]: %s%n", e.getErrorCode(), e.getSQLState(), e.getMessage());
        }
    }

    /**
     * Opcion 4: Listar todas las peliculas disponibles.
     */
    private static void listarPeliculas() {
        System.out.println("\n--- LISTADO DE PELICULAS ---");
        try {
            List<Pelicula> listado = peliculaDAO.listar();
            if (listado.isEmpty()) {
                System.out.println("[INFO] No hay peliculas registradas en la base de datos.");
            } else {
                System.out.printf("%-5s | %-30s | %-20s | %-6s | %-15s%n", "ID", "TITULO", "DIRECTOR", "ANIO", "GENERO");
                System.out.println("----------------------------------------------------------------------------------------");
                for (Pelicula p : listado) {
                    System.out.printf("%-5d | %-30s | %-20s | %-6d | %-15s%n",
                            p.getId(),
                            recortar(p.getTitle(), 30),
                            recortar(p.getDirector(), 20),
                            p.getReleaseYear(),
                            recortar(p.getGenre(), 15));
                }
                System.out.println("----------------------------------------------------------------------------------------");
                System.out.printf("Total de registros mostrados: %d%n", listado.size());
            }
        } catch (SQLException e) {
            System.err.printf("[ERROR SQL %d - %s]: %s%n", e.getErrorCode(), e.getSQLState(), e.getMessage());
        }
    }

    // Utilidades auxiliares para lectura por teclado
    private static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine().trim();
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("[AVISO] Debe introducir un numero entero valido.");
            }
        }
    }

    private static String recortar(String texto, int longitudMaxima) {
        if (texto == null) return "-";
        if (texto.length() <= longitudMaxima) return texto;
        return texto.substring(0, longitudMaxima - 3) + "...";
    }
}
