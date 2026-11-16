package veterinaria;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import veterinaria.modelo.Cita;
import veterinaria.modelo.Cliente;
import veterinaria.modelo.Mascota;
import veterinaria.modelo.Veterinario;

/**
 * Capa de acceso a datos (DAO) para la base de datos 'veterinaria'.
 * 
 * Implementacion integral de todas las partes de la practica:
 * - Parte 2: Consultas parametrizadas mapeando ResultSet a POJOs.
 * - Parte 3: Altas y modificaciones con captura de codigos de error (1062, 1452, 3819, 45000).
 * - Parte 4: Invocacion de funciones almacenadas ({? = call ...}) y procedimientos ({call ...}) con OUT e INOUT.
 * - Parte 5: Transacciones ACID complejas con rollback() y getGeneratedKeys().
 * - Parte 6: Inserciones en lote (Batch) optimizadas.
 */
public class ClinicaDAO {

    // =====================================================================
    // PARTE 2. Consultas
    // =====================================================================

    /**
     * 2.1 Veterinarios activos, ordenados por nombre.
     */
    public List<Veterinario> veterinariosActivos() throws SQLException {
        String sql = "SELECT id_veterinario, nombre, especialidad, activo FROM veterinarios WHERE activo = 1 ORDER BY nombre";
        List<Veterinario> lista = new ArrayList<>();

        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Veterinario(
                        rs.getInt("id_veterinario"),
                        rs.getString("nombre"),
                        rs.getString("especialidad"),
                        rs.getBoolean("activo")
                ));
            }
        }
        return lista;
    }

    /**
     * 2.2 Mascotas del cliente con ese DNI, ordenadas por nombre.
     * Cuidado didactico: si fecha_nacimiento es NULL, rs.getDate() devuelve null.
     * Debemos evitar lanzar NullPointerException verificando antes de toLocalDate().
     */
    public List<Mascota> mascotasDeCliente(String dni) throws SQLException {
        String sql = """
            SELECT m.id_mascota, m.nombre, m.especie, m.fecha_nacimiento, m.id_cliente
            FROM mascotas m
            JOIN clientes c ON c.id_cliente = m.id_cliente
            WHERE c.dni = ?
            ORDER BY m.nombre
            """;
        List<Mascota> lista = new ArrayList<>();

        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, dni);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Date nac = rs.getDate("fecha_nacimiento");
                    lista.add(new Mascota(
                            rs.getInt("id_mascota"),
                            rs.getString("nombre"),
                            rs.getString("especie"),
                            nac == null ? null : nac.toLocalDate(),
                            rs.getInt("id_cliente")
                    ));
                }
            }
        }
        return lista;
    }

    /**
     * 2.3 Citas pendientes de un veterinario en un dia, ordenadas por hora.
     * Se compara solo la parte de fecha del DATETIME con la funcion DATE().
     */
    public List<Cita> citasPendientes(int idVeterinario, LocalDate dia) throws SQLException {
        String sql = """
            SELECT ci.id_cita, ci.id_mascota, m.nombre AS nombre_mascota,
                   ci.id_veterinario, ci.fecha_hora, ci.tipo, ci.motivo, ci.precio, ci.estado
            FROM citas ci
            JOIN mascotas m ON m.id_mascota = ci.id_mascota
            WHERE ci.id_veterinario = ? AND DATE(ci.fecha_hora) = ? AND ci.estado = 'PENDIENTE'
            ORDER BY ci.fecha_hora
            """;
        List<Cita> lista = new ArrayList<>();

        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idVeterinario);
            ps.setDate(2, Date.valueOf(dia));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Timestamp fh = rs.getTimestamp("fecha_hora");
                    lista.add(new Cita(
                            rs.getInt("id_cita"),
                            rs.getInt("id_mascota"),
                            rs.getString("nombre_mascota"),
                            rs.getInt("id_veterinario"),
                            fh != null ? fh.toLocalDateTime() : null,
                            rs.getString("tipo"),
                            rs.getString("motivo"),
                            rs.getBigDecimal("precio"),
                            rs.getString("estado")
                    ));
                }
            }
        }
        return lista;
    }

    // =====================================================================
    // PARTE 3. Altas, modificaciones y control de errores
    // =====================================================================

    /**
     * 3.1 Alta de cliente controlando duplicidad de DNI o Email (error 1062 / SQLState 23000).
     */
    public boolean altaCliente(Cliente c) {
        String sql = "INSERT INTO clientes (dni, nombre, telefono, email) VALUES (?, ?, ?, ?)";

        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, c.getDni());
            ps.setString(2, c.getNombre());
            ps.setString(3, c.getTelefono());
            ps.setString(4, c.getEmail());

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    c.setId(rs.getInt(1));
                }
            }
            return true;

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                System.out.println("  [ERROR 1062] Ya existe un cliente registrado con ese DNI o Email.");
            } else {
                System.out.printf("  [ERROR SQL %d - %s]: %s%n", e.getErrorCode(), e.getSQLState(), e.getMessage());
            }
            return false;
        }
    }

    /**
     * 3.2 Alta de mascota controlando FK inexistente (1452) o CHECK especie (3819).
     */
    public boolean altaMascota(Mascota m) {
        String sql = "INSERT INTO mascotas (nombre, especie, fecha_nacimiento, id_cliente) VALUES (?, ?, ?, ?)";

        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, m.getNombre());
            ps.setString(2, m.getEspecie());
            if (m.getFechaNacimiento() == null) {
                ps.setNull(3, Types.DATE);
            } else {
                ps.setDate(3, Date.valueOf(m.getFechaNacimiento()));
            }
            ps.setInt(4, m.getIdCliente());

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    m.setId(rs.getInt(1));
                }
            }
            return true;

        } catch (SQLException e) {
            if (e.getErrorCode() == 1452) {
                System.out.println("  [ERROR 1452] El cliente asociado no existe (violacion de clave ajena).");
            } else if (e.getErrorCode() == 3819) {
                System.out.println("  [ERROR 3819] Especie no valida (debe ser: perro, gato, conejo, ave u otro).");
            } else {
                System.out.printf("  [ERROR SQL %d - %s]: %s%n", e.getErrorCode(), e.getSQLState(), e.getMessage());
            }
            return false;
        }
    }

    /**
     * 3.3 Modificar telefono de cliente buscando por DNI.
     * executeUpdate() == 1 confirma que el cliente existia.
     */
    public boolean modificarTelefonoCliente(String dni, String nuevoTelefono) throws SQLException {
        String sql = "UPDATE clientes SET telefono = ? WHERE dni = ?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoTelefono);
            ps.setString(2, dni);
            return ps.executeUpdate() > 0;
        }
    }

    // =====================================================================
    // PARTE 4. Procedimientos y funciones almacenadas desde Java
    // =====================================================================

    /**
     * 4.1 Invoca la funcion MySQL edad_mascota(id).
     * Devuelve Integer porque si fecha_nacimiento es NULL devuelve null (wasNull()).
     */
    public Integer edadMascota(int idMascota) throws SQLException {
        String sql = "{? = call edad_mascota(?)}";
        try (Connection con = ConexionBD.getConexion();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.registerOutParameter(1, Types.INTEGER); // Valor de retorno
            cs.setInt(2, idMascota);                   // Parametro de entrada
            cs.execute();

            int edad = cs.getInt(1);
            return cs.wasNull() ? null : edad;
        }
    }

    /**
     * 4.2 Invoca el procedimiento MySQL pedir_cita(...) con 5 parametros IN y 1 parametro OUT (id_cita).
     */
    public int pedirCita(int idMascota, int idVeterinario, LocalDateTime fechaHora,
                         String tipo, String motivo) throws SQLException {
        try (Connection con = ConexionBD.getConexion()) {
            return pedirCitaEnConexion(con, idMascota, idVeterinario, fechaHora, tipo, motivo);
        }
    }

    /**
     * Version auxiliar de pedirCita que reutiliza una conexion abierta (necesaria para transacciones).
     */
    public int pedirCitaEnConexion(Connection con, int idMascota, int idVeterinario,
                                   LocalDateTime fechaHora, String tipo, String motivo) throws SQLException {
        String sql = "{call pedir_cita(?, ?, ?, ?, ?, ?)}";
        try (CallableStatement cs = con.prepareCall(sql)) {
            cs.setInt(1, idMascota);
            cs.setInt(2, idVeterinario);
            cs.setTimestamp(3, Timestamp.valueOf(fechaHora));
            cs.setString(4, tipo);
            cs.setString(5, motivo);
            cs.registerOutParameter(6, Types.INTEGER); // OUT id_cita
            cs.execute();
            return cs.getInt(6);
        }
    }

    // =====================================================================
    // PARTE 5. Transacciones ACID
    // =====================================================================

    /**
     * 5.1 Alta atomica de Cliente + Mascota + Cita.
     * Si cualquiera de los 3 pasos falla, se ejecuta con.rollback() y no queda nada guardado.
     */
    public int altaClienteConCita(Cliente c, Mascota m, int idVet,
                                  LocalDateTime fechaHora, String tipo, String motivo) throws SQLException {
        String sqlCliente = "INSERT INTO clientes (dni, nombre, telefono, email) VALUES (?, ?, ?, ?)";
        String sqlMascota = "INSERT INTO mascotas (nombre, especie, fecha_nacimiento, id_cliente) VALUES (?, ?, ?, ?)";

        try (Connection con = ConexionBD.getConexion()) {
            con.setAutoCommit(false); // Iniciar transaccion

            try {
                // 1. Insertar Cliente
                try (PreparedStatement psC = con.prepareStatement(sqlCliente, Statement.RETURN_GENERATED_KEYS)) {
                    psC.setString(1, c.getDni());
                    psC.setString(2, c.getNombre());
                    psC.setString(3, c.getTelefono());
                    psC.setString(4, c.getEmail());
                    psC.executeUpdate();

                    try (ResultSet rsC = psC.getGeneratedKeys()) {
                        rsC.next();
                        c.setId(rsC.getInt(1));
                    }
                }

                // 2. Insertar Mascota asociada
                m.setIdCliente(c.getId());
                try (PreparedStatement psM = con.prepareStatement(sqlMascota, Statement.RETURN_GENERATED_KEYS)) {
                    psM.setString(1, m.getNombre());
                    psM.setString(2, m.getEspecie());
                    if (m.getFechaNacimiento() == null) {
                        psM.setNull(3, Types.DATE);
                    } else {
                        psM.setDate(3, Date.valueOf(m.getFechaNacimiento()));
                    }
                    psM.setInt(4, m.getIdCliente());
                    psM.executeUpdate();

                    try (ResultSet rsM = psM.getGeneratedKeys()) {
                        rsM.next();
                        m.setId(rsM.getInt(1));
                    }
                }

                // 3. Pedir cita usando la misma conexion
                int idCita = pedirCitaEnConexion(con, m.getId(), idVet, fechaHora, tipo, motivo);

                con.commit(); // Todo correcto: confirmamos transaccion
                System.out.println("  [OK TRANSACCION] Cliente, mascota y cita creados satisfactoriamente.");
                return idCita;

            } catch (SQLException e) {
                con.rollback(); // Fallo en algun paso: se deshace todo
                System.out.printf("  [ROLLBACK] Transaccion cancelada por error: %s%n", e.getMessage());
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    // =====================================================================
    // PARTE 6. Lotes (Batch)
    // =====================================================================

    /**
     * 6.1 Insercion masiva en lote de mascotas usando addBatch() y executeBatch().
     */
    public boolean insertarMascotasEnLote(List<Mascota> mascotas) throws SQLException {
        String sql = "INSERT INTO mascotas (nombre, especie, fecha_nacimiento, id_cliente) VALUES (?, ?, ?, ?)";

        try (Connection con = ConexionBD.getConexion()) {
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                for (Mascota m : mascotas) {
                    ps.setString(1, m.getNombre());
                    ps.setString(2, m.getEspecie());
                    if (m.getFechaNacimiento() == null) {
                        ps.setNull(3, Types.DATE);
                    } else {
                        ps.setDate(3, Date.valueOf(m.getFechaNacimiento()));
                    }
                    ps.setInt(4, m.getIdCliente());
                    ps.addBatch();
                }

                int[] filasAfectadas = ps.executeBatch();
                con.commit();
                System.out.printf("  [OK BATCH] Insercion en lote completada: %d sentencias ejecutadas.%n", filasAfectadas.length);
                return true;

            } catch (SQLException e) {
                con.rollback();
                System.out.println("  [ROLLBACK BATCH] Lote cancelado: " + e.getMessage());
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }
}
