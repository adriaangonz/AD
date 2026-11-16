package com.dam2.ad.preguntastest;

import com.dam2.ad.preguntastest.dao.PreguntasDAO;
import com.dam2.ad.preguntastest.modelo.Pregunta;
import com.dam2.ad.preguntastest.modelo.Respuesta;
import com.dam2.ad.preguntastest.modelo.Usuario;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * Resolucion del EJERCICIO 2 (+ AMPLIACION OPCIONAL) de la Hoja 1-02:
 * 
 * Requisitos:
 * - Autenticacion mediante MD5(?) en MySQL con 3 intentos maximos.
 * - Registro de num_accesos (+1).
 * - 5 preguntas aleatorias tipo 2 no canceladas (sin repetir).
 * - Validacion de respuesta del usuario.
 * - Transaccion estricta (setAutoCommit(false), commit(), rollback()) para actualizar
 *   veces_formulada, veces_acertada y veces_respondida.
 * - Resumen final ("X de 5") y actualizacion de test_realizados (+1).
 * - Ampliacion integrada: insercion en test_usuarios (con getGeneratedKeys()),
 *   preguntas_formuladas, y calculo de puntuacion media.
 */
public class Ejercicio2_SimulacroTest {

    private final PreguntasDAO preguntasDAO;
    private final Scanner scanner;

    public Ejercicio2_SimulacroTest(PreguntasDAO preguntasDAO, Scanner scanner) {
        this.preguntasDAO = preguntasDAO;
        this.scanner = scanner;
    }

    public void ejecutar(boolean conAmpliacion) {
        System.out.println("\n========================================================");
        System.out.printf("   EJERCICIO 2: SIMULACRO DE TEST %s%n", conAmpliacion ? "(CON AMPLIACION)" : "");
        System.out.println("========================================================");

        // Fase 1: Autenticacion (3 intentos)
        Usuario usuario = autenticarUsuario();
        if (usuario == null) {
            System.out.println("\n[FIN] Se han agotado los 3 intentos permitidos. Programa terminado.");
            return;
        }

        try {
            // Bienvenida y contabilizacion de acceso
            System.out.printf("%n[OK] Sesion iniciada correctamente con: %s%n", usuario.getNombreCompleto());
            preguntasDAO.registrarAcceso(usuario.getIdUsuario());
            System.out.println("Se ha registrado un acceso mas para el usuario.");

            // Fase 2: Seleccionar 5 preguntas aleatorias de tipo 2 no canceladas
            System.out.println("\nSeleccionando 5 preguntas aleatorias de tipo 2 (respuesta unica)...");
            List<Pregunta> test = preguntasDAO.obtenerPreguntasAleatoriasTipo2(5);

            if (test.size() < 5) {
                System.out.printf("[AVISO] La base de datos solo contiene %d preguntas de tipo 2 disponibles.%n", test.size());
            }

            int numTestGenerado = -1;
            if (conAmpliacion) {
                numTestGenerado = preguntasDAO.iniciarTestUsuario(usuario.getUsuario());
                System.out.printf("[AMPLIACION] Test registrado en 'test_usuarios' con num_test autogenerado: %d%n", numTestGenerado);
            }

            int aciertos = 0;
            int posicion = 1;

            // Fase 3 y 4: Formular preguntas y actualizar contadores transaccionalmente
            for (Pregunta p : test) {
                System.out.println("\n------------------------------------------------------------");
                System.out.printf("Pregunta %d de %d:%n", posicion, test.size());
                System.out.println(p);

                List<Respuesta> respuestas = p.getRespuestas();
                for (Respuesta r : respuestas) {
                    System.out.printf("   %d. %s%n", r.getNumResp(), r.getTextoResp());
                }

                int respuestaElegida = pedirRespuestaValida(respuestas);

                // Determinar si es correcta y cual era la correcta
                boolean esCorrecta = false;
                Respuesta correcta = null;
                for (Respuesta r : respuestas) {
                    if (r.isCorrecta()) {
                        correcta = r;
                    }
                    if (r.getNumResp() == respuestaElegida && r.isCorrecta()) {
                        esCorrecta = true;
                    }
                }

                if (esCorrecta) {
                    aciertos++;
                    System.out.println(">>> ¡CORRECTO! Enhorabuena.");
                } else {
                    System.out.println(">>> ¡INCORRECTO!");
                    if (correcta != null) {
                        System.out.printf("    La respuesta correcta era: %d. %s%n", correcta.getNumResp(), correcta.getTextoResp());
                    }
                }

                // Actualizacion transaccional (ACID) en la base de datos
                if (conAmpliacion && numTestGenerado > 0) {
                    preguntasDAO.registrarPreguntaFormuladaTransaccional(
                            numTestGenerado, posicion, p.getNumPreg(), esCorrecta, respuestaElegida);
                } else {
                    preguntasDAO.registrarRespuestaTransaccional(p.getNumPreg(), esCorrecta, respuestaElegida);
                }
                posicion++;
            }

            // Fase 5: Resumen final y actualizacion de test_realizados
            System.out.println("\n========================================================");
            System.out.println("                 RESULTADO DEL TEST                     ");
            System.out.println("========================================================");
            System.out.printf("Puntuacion obtenida: %d de %d aciertos.%n", aciertos, test.size());

            if (conAmpliacion && numTestGenerado > 0) {
                preguntasDAO.finalizarTestUsuario(numTestGenerado, usuario.getIdUsuario(), usuario.getUsuario(), aciertos);
                System.out.println("[AMPLIACION] Se ha actualizado la hora de fin, los puntos y recalculado la puntuacion_media del usuario.");
            } else {
                preguntasDAO.registrarFinTest(usuario.getIdUsuario());
                System.out.println("Se ha contabilizado que el usuario ha realizado un test mas (test_realizados).");
            }

        } catch (SQLException e) {
            System.err.printf("[ERROR SQL %d - %s]: %s%n", e.getErrorCode(), e.getSQLState(), e.getMessage());
        }
    }

    private Usuario autenticarUsuario() {
        int intentos = 0;
        final int MAX_INTENTOS = 3;

        while (intentos < MAX_INTENTOS) {
            intentos++;
            System.out.printf("%n[Intento %d de %d] Identificate en el sistema:%n", intentos, MAX_INTENTOS);
            System.out.print("Usuario: ");
            String login = scanner.nextLine().trim();
            System.out.print("Contrasena: ");
            String pass = scanner.nextLine().trim();

            try {
                Usuario u = preguntasDAO.autenticar(login, pass);
                if (u != null) {
                    return u;
                }
                System.out.println("[AVISO] Credenciales incorrectas.");
            } catch (SQLException e) {
                System.err.printf("[ERROR SQL %d - %s]: %s%n", e.getErrorCode(), e.getSQLState(), e.getMessage());
                return null;
            }
        }
        return null;
    }

    private int pedirRespuestaValida(List<Respuesta> respuestas) {
        while (true) {
            System.out.print("Introduce el numero de la respuesta: ");
            String input = scanner.nextLine().trim();
            try {
                int elegido = Integer.parseInt(input);
                for (Respuesta r : respuestas) {
                    if (r.getNumResp() == elegido) {
                        return elegido;
                    }
                }
                System.out.println("[AVISO] El numero introducido no corresponde a ninguna de las respuestas mostradas.");
            } catch (NumberFormatException e) {
                System.out.println("[AVISO] Por favor, introduce un numero valido.");
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        PreguntasDAO dao = new PreguntasDAO();
        new Ejercicio2_SimulacroTest(dao, sc).ejecutar(true);
    }
}
