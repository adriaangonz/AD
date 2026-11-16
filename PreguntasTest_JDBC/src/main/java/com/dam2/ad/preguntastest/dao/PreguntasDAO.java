package com.dam2.ad.preguntastest.dao;

import com.dam2.ad.preguntastest.conexion.ConexionBD;
import com.dam2.ad.preguntastest.modelo.Pregunta;
import com.dam2.ad.preguntastest.modelo.Respuesta;
import com.dam2.ad.preguntastest.modelo.Usuario;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Data Access Object (DAO) para la gestion de la base de datos 'preguntastest'.
 * 
 * Cumple exhaustivamente con todos los requerimientos de la Hoja 1-02:
 * 1. Consultas con PreparedStatement parametrizadas y JOINs.
 * 2. Comprobacion de credenciales con hash MD5 en la propia consulta MySQL.
 * 3. Manejo de transacciones ACID (setAutoCommit(false), commit(), rollback()).
 * 4. Recuperacion de claves generadas mediante getGeneratedKeys().
 */
public class PreguntasDAO {

    // =========================================================================
    // EJERCICIO 1: CONSULTA DE PREGUNTAS POR TIPO CON RESPUESTAS
    // =========================================================================

    /**
     * Recupera todas las preguntas no canceladas de un tipo determinado junto con
     * sus respuestas asociadas, ordenadas por el numero de pregunta y numero de respuesta.
     * 
     * COMENTARIO COMPARATIVO REQUERIDO POR EL ENUNCIADO:
     * -------------------------------------------------------------------------
     * Se ha optado por resolver esta operacion mediante una UNICA CONSULTA CON JOIN
     * (LEFT JOIN entre preguntas, categorias y respuestas):
     * 
     * - Ventajas del enfoque con una unica consulta con JOIN:
     *   1. Eficiencia de red (Network Round-trips): Se realiza exactamente una sola ida y
     *      vuelta al servidor de base de datos en lugar de hacer 1 + N consultas (patron N+1).
     *      Si hubiese 50 preguntas, el metodo de consultas separadas requeriria 51 peticiones SQL,
     *      mientras que con JOIN se ejecuta en una sola.
     *   2. Atomicidad y consistencia en lectura: Todos los datos se leen bajo una misma fotografia
     *      consistente del estado de la base de datos en ese instante.
     *   3. Desventaja y solucion: El JOIN duplica los datos de la cabecera de la pregunta
     *      (enunciado, nivel, etc.) en cada fila de respuesta. En Java lo agrupamos de forma limpia
     *      y ordenada en memoria usando un Map indexado por numPreg (LinkedHashMap para conservar
     *      el orden original del ORDER BY).
     * 
     * @param tipo Tipo de pregunta (1 a 5)
     * @return Lista de preguntas con sus respuestas cargadas
     * @throws SQLException si ocurre un error en la base de datos
     */
    public List<Pregunta> obtenerPreguntasPorTipo(int tipo) throws SQLException {
        String sql = """
            SELECT p.num_preg, p.enunciado, p.categoria, c.nombre_cat, p.nivel, p.tipo,
                   p.ruta_foto AS p_foto, p.ruta_audio, p.veces_formulada, p.veces_acertada, p.fecha_canc,
                   r.num_resp, r.texto_resp, r.correcta, r.ruta_foto AS r_foto, r.veces_respondida
            FROM preguntas p
            LEFT JOIN categorias c ON p.categoria = c.cod_cat
            LEFT JOIN respuestas r ON p.num_preg = r.num_preg
            WHERE p.tipo = ? AND p.fecha_canc IS NULL
            ORDER BY p.num_preg ASC, r.num_resp ASC
            """;

        Map<Integer, Pregunta> mapaPreguntas = new LinkedHashMap<>();

        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, tipo);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int numPreg = rs.getInt("num_preg");

                    // Si es la primera vez que vemos esta pregunta, creamos la instancia
                    Pregunta p = mapaPreguntas.get(numPreg);
                    if (p == null) {
                        Date fc = rs.getDate("fecha_canc");
                        p = new Pregunta(
                                numPreg,
                                rs.getString("enunciado"),
                                rs.getString("categoria"),
                                rs.getString("nombre_cat"),
                                rs.getInt("nivel"),
                                rs.getInt("tipo"),
                                rs.getString("p_foto"),
                                rs.getString("ruta_audio"),
                                rs.getInt("veces_formulada"),
                                rs.getInt("veces_acertada"),
                                fc != null ? fc.toLocalDate() : null
                        );
                        mapaPreguntas.put(numPreg, p);
                    }

                    // Si la fila tiene respuesta asociada (num_resp no es null)
                    int numResp = rs.getInt("num_resp");
                    if (!rs.wasNull()) {
                        Respuesta r = new Respuesta(
                                numPreg,
                                numResp,
                                rs.getString("texto_resp"),
                                rs.getInt("correcta") == 1,
                                rs.getString("r_foto"),
                                rs.getInt("veces_respondida")
                        );
                        p.agregarRespuesta(r);
                    }
                }
            }
        }

        return new ArrayList<>(mapaPreguntas.values());
    }

    // =========================================================================
    // EJERCICIO 2: AUTENTICACION Y REALIZACION DE TEST ALEATORIO CON TRANSACCIONES
    // =========================================================================

    /**
     * Autentica a un usuario comprobando el hash MD5 de la contrasena directamente en MySQL.
     * Tal como indica el enunciado: "La contrasena esta guardada con MD5: puedes calcular el
     * resumen en la propia consulta con la funcion MD5(?). No se deben recuperar todos los
     * usuarios para buscar el correcto en Java."
     *
     * @param usuario Nombre de usuario en la tabla
     * @param password Contrasena en texto plano
     * @return Objeto Usuario si las credenciales coinciden, o null si son erroneas
     * @throws SQLException si ocurre un error de consulta
     */
    public Usuario autenticar(String usuario, String password) throws SQLException {
        String sql = """
            SELECT id_usuario, usuario, password, nombre, apellidos, fechanac,
                   num_accesos, test_realizados, puntuacion_media
            FROM usuarios
            WHERE usuario = ? AND password = MD5(?)
            """;

        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Date fn = rs.getDate("fechanac");
                    return new Usuario(
                            rs.getInt("id_usuario"),
                            rs.getString("usuario"),
                            rs.getString("password"),
                            rs.getString("nombre"),
                            rs.getString("apellidos"),
                            fn != null ? fn.toLocalDate() : null,
                            rs.getInt("num_accesos"),
                            rs.getInt("test_realizados"),
                            rs.getFloat("puntuacion_media")
                    );
                }
            }
        }
        return null;
    }

    /**
     * Incrementa el contador de num_accesos del usuario.
     */
    public void registrarAcceso(int idUsuario) throws SQLException {
        String sql = "UPDATE usuarios SET num_accesos = num_accesos + 1 WHERE id_usuario = ?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.executeUpdate();
        }
    }

    /**
     * Selecciona N preguntas aleatorias de tipo 2 no canceladas (ORDER BY RAND() LIMIT ?).
     * Carga de inmediato sus respuestas asociadas ordenadas por num_resp.
     */
    public List<Pregunta> obtenerPreguntasAleatoriasTipo2(int cantidad) throws SQLException {
        String sqlPreguntas = """
            SELECT p.num_preg, p.enunciado, p.categoria, c.nombre_cat, p.nivel, p.tipo,
                   p.ruta_foto, p.ruta_audio, p.veces_formulada, p.veces_acertada, p.fecha_canc
            FROM preguntas p
            LEFT JOIN categorias c ON p.categoria = c.cod_cat
            WHERE p.tipo = 2 AND p.fecha_canc IS NULL
            ORDER BY RAND()
            LIMIT ?
            """;

        List<Pregunta> preguntas = new ArrayList<>();

        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sqlPreguntas)) {

            ps.setInt(1, cantidad);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Date fc = rs.getDate("fecha_canc");
                    Pregunta p = new Pregunta(
                            rs.getInt("num_preg"),
                            rs.getString("enunciado"),
                            rs.getString("categoria"),
                            rs.getString("nombre_cat"),
                            rs.getInt("nivel"),
                            rs.getInt("tipo"),
                            rs.getString("ruta_foto"),
                            rs.getString("ruta_audio"),
                            rs.getInt("veces_formulada"),
                            rs.getInt("veces_acertada"),
                            fc != null ? fc.toLocalDate() : null
                    );
                    preguntas.add(p);
                }
            }
        }

        // Cargar respuestas de cada una de las 5 preguntas seleccionadas
        for (Pregunta p : preguntas) {
            p.setRespuestas(obtenerRespuestasPregunta(p.getNumPreg()));
        }

        return preguntas;
    }

    /**
     * Obtiene las respuestas asociadas a una pregunta dada, ordenadas por num_resp.
     */
    public List<Respuesta> obtenerRespuestasPregunta(int numPreg) throws SQLException {
        String sql = """
            SELECT num_preg, num_resp, texto_resp, correcta, ruta_foto, veces_respondida
            FROM respuestas
            WHERE num_preg = ?
            ORDER BY num_resp ASC
            """;
        List<Respuesta> lista = new ArrayList<>();
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, numPreg);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Respuesta(
                            rs.getInt("num_preg"),
                            rs.getInt("num_resp"),
                            rs.getString("texto_resp"),
                            rs.getInt("correcta") == 1,
                            rs.getString("ruta_foto"),
                            rs.getInt("veces_respondida")
                    ));
                }
            }
        }
        return lista;
    }

    /**
     * Actualiza los contadores de una pregunta respondida DENTRO DE UNA TRANSACCION ACID:
     * 1. Incrementa veces_formulada de la pregunta.
     * 2. Si se acerto, incrementa veces_acertada de la pregunta.
     * 3. Incrementa veces_respondida de la respuesta elegida por el usuario.
     * 
     * Usa setAutoCommit(false), commit() y en caso de error rollback().
     */
    public void registrarRespuestaTransaccional(int numPreg, boolean acertada, int numResp) throws SQLException {
        String sqlPregunta = """
            UPDATE preguntas
            SET veces_formulada = veces_formulada + 1,
                veces_acertada = veces_acertada + ?
            WHERE num_preg = ?
            """;

        String sqlRespuesta = """
            UPDATE respuestas
            SET veces_respondida = veces_respondida + 1
            WHERE num_preg = ? AND num_resp = ?
            """;

        try (Connection con = ConexionBD.getConexion()) {
            con.setAutoCommit(false); // Iniciar transaccion explicita
            try (PreparedStatement psP = con.prepareStatement(sqlPregunta);
                 PreparedStatement psR = con.prepareStatement(sqlRespuesta)) {

                // 1. Actualizar tabla preguntas
                psP.setInt(1, acertada ? 1 : 0);
                psP.setInt(2, numPreg);
                psP.executeUpdate();

                // 2. Actualizar tabla respuestas
                psR.setInt(1, numPreg);
                psR.setInt(2, numResp);
                psR.executeUpdate();

                con.commit(); // Confirmar todas las modificaciones
            } catch (SQLException e) {
                con.rollback(); // Deshacer cambios en caso de fallo
                throw e;
            } finally {
                con.setAutoCommit(true); // Restaurar autocommit
            }
        }
    }

    /**
     * Incrementa en 1 el contador de test_realizados del usuario.
     */
    public void registrarFinTest(int idUsuario) throws SQLException {
        String sql = "UPDATE usuarios SET test_realizados = test_realizados + 1 WHERE id_usuario = ?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.executeUpdate();
        }
    }

    // =========================================================================
    // AMPLIACION (OPCIONAL): REGISTRO COMPLETO DEL TEST Y SUS PREGUNTAS
    // =========================================================================

    /**
     * Inserta un nuevo registro en 'test_usuarios' al iniciar el test
     * y obtiene la clave primaria autogenerada (num_test) con getGeneratedKeys().
     */
    public int iniciarTestUsuario(String usuario) throws SQLException {
        String sql = """
            INSERT INTO test_usuarios (usuario, fecha, hora_inicio, general, numero_preguntas, categoria, puntos)
            VALUES (?, ?, ?, 1, 5, NULL, 0)
            """;

        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, usuario);
            ps.setDate(2, Date.valueOf(LocalDate.now()));
            ps.setTime(3, Time.valueOf(LocalTime.now()));

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    /**
     * Registra una pregunta formulada en el test y actualiza los contadores en la misma transaccion.
     */
    public void registrarPreguntaFormuladaTransaccional(int numTest, int posicion, int numPreg,
                                                        boolean acertada, int numResp) throws SQLException {
        String sqlPregForm = """
            INSERT INTO preguntas_formuladas (numero_test, num_preg_en_test, pregunta, acertada, puntos)
            VALUES (?, ?, ?, ?, ?)
            """;

        String sqlPregunta = """
            UPDATE preguntas
            SET veces_formulada = veces_formulada + 1,
                veces_acertada = veces_acertada + ?
            WHERE num_preg = ?
            """;

        String sqlRespuesta = """
            UPDATE respuestas
            SET veces_respondida = veces_respondida + 1
            WHERE num_preg = ? AND num_resp = ?
            """;

        try (Connection con = ConexionBD.getConexion()) {
            con.setAutoCommit(false);
            try (PreparedStatement psPF = con.prepareStatement(sqlPregForm);
                 PreparedStatement psP = con.prepareStatement(sqlPregunta);
                 PreparedStatement psR = con.prepareStatement(sqlRespuesta)) {

                // 1. Insertar en preguntas_formuladas
                psPF.setInt(1, numTest);
                psPF.setInt(2, posicion);
                psPF.setInt(3, numPreg);
                psPF.setInt(4, acertada ? 1 : 0);
                psPF.setFloat(5, acertada ? 1.0f : 0.0f);
                psPF.executeUpdate();

                // 2. Incrementar veces_formulada y veces_acertada
                psP.setInt(1, acertada ? 1 : 0);
                psP.setInt(2, numPreg);
                psP.executeUpdate();

                // 3. Incrementar veces_respondida
                psR.setInt(1, numPreg);
                psR.setInt(2, numResp);
                psR.executeUpdate();

                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    /**
     * Finaliza el test: guarda la hora de fin y puntos en 'test_usuarios',
     * e incrementa test_realizados y recalcula puntuacion_media del usuario.
     */
    public void finalizarTestUsuario(int numTest, int idUsuario, String usuario, int puntos) throws SQLException {
        String sqlTest = "UPDATE test_usuarios SET hora_fin = ?, puntos = ? WHERE num_test = ?";
        String sqlUsuario = """
            UPDATE usuarios
            SET test_realizados = test_realizados + 1,
                puntuacion_media = (SELECT IFNULL(AVG(puntos), 0) FROM test_usuarios WHERE usuario = ?)
            WHERE id_usuario = ?
            """;

        try (Connection con = ConexionBD.getConexion()) {
            con.setAutoCommit(false);
            try (PreparedStatement psT = con.prepareStatement(sqlTest);
                 PreparedStatement psU = con.prepareStatement(sqlUsuario)) {

                psT.setTime(1, Time.valueOf(LocalTime.now()));
                psT.setFloat(2, (float) puntos);
                psT.setInt(3, numTest);
                psT.executeUpdate();

                psU.setString(1, usuario);
                psU.setInt(2, idUsuario);
                psU.executeUpdate();

                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }
}
