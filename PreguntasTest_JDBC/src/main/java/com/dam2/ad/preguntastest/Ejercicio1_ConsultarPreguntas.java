package com.dam2.ad.preguntastest;

import com.dam2.ad.preguntastest.dao.PreguntasDAO;
import com.dam2.ad.preguntastest.modelo.Pregunta;
import com.dam2.ad.preguntastest.modelo.Respuesta;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * Resolucion del EJERCICIO 1 de la Hoja 1-02:
 * 
 * "Realiza un programa que pida al usuario un tipo de pregunta (de 1 a 5) y muestre
 * todas las preguntas no canceladas de ese tipo, ordenadas por numero. Para cada pregunta
 * se muestra su numero, el nombre de su categoria, su nivel y su enunciado y, a continuacion,
 * el numero y el texto de cada una de sus respuestas, ordenadas por num_resp. No se indica
 * cual es la correcta.
 * Tras cada pregunta habra que pulsar ENTER para pasar a la siguiente. Si el tipo no es valido
 * se vuelve a pedir; si no hay preguntas de ese tipo, se informa de ello. Al final se indica
 * cuantas preguntas se han mostrado."
 */
public class Ejercicio1_ConsultarPreguntas {

    private final PreguntasDAO preguntasDAO;
    private final Scanner scanner;

    public Ejercicio1_ConsultarPreguntas(PreguntasDAO preguntasDAO, Scanner scanner) {
        this.preguntasDAO = preguntasDAO;
        this.scanner = scanner;
    }

    public void ejecutar() {
        System.out.println("\n========================================================");
        System.out.println("   EJERCICIO 1: CONSULTA DE PREGUNTAS POR TIPO          ");
        System.out.println("========================================================");

        int tipo = pedirTipoValido();

        try {
            System.out.println("\nConsultando preguntas no canceladas de tipo " + tipo + "...");
            List<Pregunta> preguntas = preguntasDAO.obtenerPreguntasPorTipo(tipo);

            if (preguntas.isEmpty()) {
                System.out.printf("No se han encontrado preguntas de tipo %d en la base de datos.%n", tipo);
                return;
            }

            int contador = 0;
            for (Pregunta p : preguntas) {
                contador++;
                System.out.println();
                // Formato exacto segun el ejemplo del enunciado:
                // Pregunta 5 [Geografía · nivel 2]
                // La siguiente imagen corresponde al estrecho de ...
                System.out.println(p);

                // Mostrar cada respuesta: 1. Bering, 2. Gibraltar, etc.
                for (Respuesta r : p.getRespuestas()) {
                    System.out.println("   " + r.getNumResp() + ". " + r.getTextoResp());
                }

                System.out.print("(Pulsa ENTER para continuar) ");
                scanner.nextLine();
            }

            System.out.printf("%n=== Se han mostrado %d preguntas de tipo %d ===%n", contador, tipo);

        } catch (SQLException e) {
            System.err.printf("[ERROR SQL %d - %s]: %s%n", e.getErrorCode(), e.getSQLState(), e.getMessage());
        }
    }

    private int pedirTipoValido() {
        while (true) {
            System.out.print("Tipo de pregunta (1-5): ");
            String input = scanner.nextLine().trim();
            try {
                int tipo = Integer.parseInt(input);
                if (tipo >= 1 && tipo <= 5) {
                    return tipo;
                }
                System.out.println("[AVISO] El tipo de pregunta debe estar comprendido entre 1 y 5.");
            } catch (NumberFormatException e) {
                System.out.println("[AVISO] Debe introducir un valor numerico entero.");
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        PreguntasDAO dao = new PreguntasDAO();
        new Ejercicio1_ConsultarPreguntas(dao, sc).ejecutar();
    }
}
