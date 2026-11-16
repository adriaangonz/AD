package com.dam2.ad.preguntastest;

import com.dam2.ad.preguntastest.dao.PreguntasDAO;
import java.util.Scanner;

/**
 * Punto de entrada principal para el proyecto PreguntasTest_JDBC (Hoja 1-02).
 * 
 * Permite ejecutar de forma interactiva:
 * - Ejercicio 1: Consulta de preguntas por tipo con paginacion.
 * - Ejercicio 2: Simulacro de test con autenticacion MD5 y transacciones.
 * - Ejercicio 2 (Ampliacion): Simulacro con registro en test_usuarios y preguntas_formuladas.
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PreguntasDAO dao = new PreguntasDAO();

        System.out.println("========================================================");
        System.out.println("   ACCESO A DATOS (DAM 2) - HOJA 1-02: PREGUNTAS TEST   ");
        System.out.println("========================================================");

        boolean salir = false;
        while (!salir) {
            System.out.println("\nSelecciona la actividad a ejecutar:");
            System.out.println("1. Ejercicio 1: Consultar preguntas no canceladas por tipo");
            System.out.println("2. Ejercicio 2: Realizar simulacro de test (5 preguntas tipo 2)");
            System.out.println("3. Ejercicio 2 con AMPLIACION (registro completo en test_usuarios)");
            System.out.println("4. Salir");
            System.out.print("Opcion (1-4): ");

            String opcion = scanner.nextLine().trim();
            switch (opcion) {
                case "1" -> new Ejercicio1_ConsultarPreguntas(dao, scanner).ejecutar();
                case "2" -> new Ejercicio2_SimulacroTest(dao, scanner).ejecutar(false);
                case "3" -> new Ejercicio2_SimulacroTest(dao, scanner).ejecutar(true);
                case "4" -> {
                    salir = true;
                    System.out.println("\nCerrando aplicacion de Preguntas Test...");
                }
                default -> System.out.println("[AVISO] Opcion no valida. Selecciona 1, 2, 3 o 4.");
            }
        }
    }
}
