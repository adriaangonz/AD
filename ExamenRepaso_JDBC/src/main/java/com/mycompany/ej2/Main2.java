/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ej2;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author adrian
 */
public class Main2 {

    public static void main(Scanner teclado) {
        GestorEj2 gestor = new GestorEj2();
        boolean salir = false;
        while (!salir) {
            mostrarMenu();
            try {
                int opcion = Integer.parseInt(teclado.nextLine());
                switch (opcion) {
                    case 1 ->
                        ejecutarOpcion1(gestor);
                    case 2 ->
                        ejecutarOpcion2(gestor);
                    case 3 ->
                        ejecutarOpcion3(gestor);
                    case 4 ->
                        ejecutarOpcion4(gestor, teclado);
                    case 5 ->
                        ejecutarOpcion5(gestor);
                    case 6 ->
                        ejecutarOpcion6(gestor);
                    case 7 ->
                        ejecutarOpcion7(gestor);
                    case 8 ->
                        ejecutarOpcion8(gestor, teclado);
                    case 9 ->
                        ejecutarOpcion9(gestor, teclado);
                    case 0 -> {
                        salir = true;
                        System.out.println("SAliendo");
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Introduce un numero entero.");
            }
        }
    }

    private static void mostrarMenu() {
        System.out.println("1. Listado de grupos");
        System.out.println("2. Listado de canciones");
        System.out.println("3. NUmero de canciones por grupo");
        System.out.println("4. Canciones de un grupo");
        System.out.println("5. Las 5 canciones mas votadas");
        System.out.println("6. GRupos sin canciones");
        System.out.println("7. Los ultimos 5 votos");
        System.out.println("8. Eliminar canciones de un grupo");
        System.out.println("9. MOdificar datos de un grupo");
        System.out.println("0. SAlir");
        System.out.println("Introduce una opcion: ");
    }

    private static void ejecutarOpcion1(GestorEj2 gestor) {
        try {
            List<Grupo> lista = gestor.listarGrupos();
            for (Grupo g : lista) {
                System.out.println(g.getCodgrupo() + g.getNombre() + g.getLocalidad() + g.getEstilo());
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar los grupos");
        }
    }

    private static void ejecutarOpcion2(GestorEj2 gestor) {
        try {
            List<Cancion> lista = gestor.listarCanciones();
            String grupoAnt = "";
            for (Cancion c : lista) {
                if (!c.getGrupo().equals(grupoAnt)) {
                    grupoAnt = c.getGrupo();
                    System.out.println(grupoAnt);
                }
                System.out.println("     " + c.getTitulo());
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar las canciones");
        }
    }

    private static void ejecutarOpcion3(GestorEj2 gestor) {
        try {
            Map<String, Integer> mapa = gestor.mostrarGruposCanciones();
            mapa.forEach((nombre, numcanciones) -> System.out.println(nombre + ": " + numcanciones));
        } catch (SQLException e) {
            System.out.println("Error al listar las canciones");
        }
    }

    private static void ejecutarOpcion4(GestorEj2 gestor, Scanner teclado) {
        String grupo = "";
        while (grupo.length() < 1 || grupo.length() > 30) {
            System.out.println("Introduce el nombre del grupo");
            grupo = teclado.nextLine();
        }
        try {
            List<Cancion> lista = gestor.listarCancionesGrupo(grupo);
            if (lista == null) {
                System.out.println("Ese grupo no existe");
                return;
            }
            if (lista.size()==0) {
                System.out.println("Ese grupo no tiene canciones");
                return;
            }
            System.out.println("Grupo: " + grupo);
            System.out.println("Numero de canciones: " + lista.size());
            for (Cancion c : lista) {
                System.out.println("Cancion: " + c.getTitulo());
                System.out.println("Duracion: " + c.getDuracion());
                System.out.println("----------------");
            }
        } catch (SQLException ex) {
            System.getLogger(Main2.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    private static void ejecutarOpcion5(GestorEj2 gestor) {
        try {
            List<Cancion> lista = gestor.listarMasVotadas();
            for (Cancion c : lista) {
                System.out.println("Titulo: " + c.getTitulo());
                System.out.println("Grupo: " + c.getGrupo());
                System.out.println("Numero de votos: " + c.getTotal_votos());
                System.out.println("-----------");
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar");
        }
    }

    private static void ejecutarOpcion6(GestorEj2 gestor) {

    }

    private static void ejecutarOpcion7(GestorEj2 gestor) {

    }

    private static void ejecutarOpcion8(GestorEj2 gestor, Scanner teclado) {

    }

    private static void ejecutarOpcion9(GestorEj2 gestor, Scanner teclado) {

    }
}
