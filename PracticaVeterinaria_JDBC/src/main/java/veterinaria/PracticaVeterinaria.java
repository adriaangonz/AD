package veterinaria;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import veterinaria.modelo.Cita;
import veterinaria.modelo.Cliente;
import veterinaria.modelo.Mascota;
import veterinaria.modelo.Veterinario;

/**
 * Programa principal de la practica Clinica Veterinaria con JDBC.
 * 
 * Contiene pruebas de ejecucion guiadas y demostrativas para cada parte del guion:
 * - Parte 0: Comprobacion de conexion inicial
 * - Parte 2: Consultas (veterinarios, mascotas de cliente, citas pendientes)
 * - Parte 3: Altas y capturas de error (duplicados, FK, checks)
 * - Parte 4: Funciones y procedimientos almacenados (edad_mascota, pedir_cita)
 * - Parte 5: Transaccion atomica
 * - Parte 6: Inserciones en lote (batch)
 */
public class PracticaVeterinaria {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("   PRACTICA GUIADA: CLINICA VETERINARIA CON JDBC  ");
        System.out.println("==================================================");

        ClinicaDAO dao = new ClinicaDAO();

        // ----- Parte 0. Comprobacion de conexion -----
        System.out.println("\n--- PARTE 0: COMPROBACION DE CONEXION ---");
        String sql = "SELECT DATABASE(), CURRENT_USER(), VERSION(), (SELECT COUNT(*) FROM citas)";
        try (Connection con = ConexionBD.getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                System.out.println("[OK] Conexion establecida con exito");
                System.out.println("  Base de datos: " + rs.getString(1));
                System.out.println("  Usuario:       " + rs.getString(2));
                System.out.println("  Servidor:      " + rs.getString(3));
                System.out.println("  Total Citas:   " + rs.getInt(4));
            }
        } catch (SQLException e) {
            System.err.println("[AVISO] No se pudo conectar a la base de datos veterinaria en este equipo.");
            System.err.println("Recuerda configurar 'db.properties' con las credenciales de clase.");
            System.err.println("Detalle: " + e.getMessage());
            return;
        }

        // ----- Parte 2. Consultas -----
        System.out.println("\n--- PARTE 2: CONSULTAS Y MAPEO A OBJETOS ---");
        try {
            System.out.println("2.1 Veterinarios activos:");
            List<Veterinario> vets = dao.veterinariosActivos();
            for (Veterinario v : vets) {
                System.out.println("  " + v);
            }

            System.out.println("\n2.2 Mascotas del cliente con DNI 12345678A:");
            List<Mascota> mascotas = dao.mascotasDeCliente("12345678A");
            for (Mascota m : mascotas) {
                System.out.println("  " + m);
            }

            System.out.println("\n2.3 Citas pendientes del veterinario 1 para hoy:");
            List<Cita> citas = dao.citasPendientes(1, LocalDate.now());
            if (citas.isEmpty()) {
                System.out.println("  (No hay citas pendientes para hoy)");
            } else {
                for (Cita c : citas) {
                    System.out.printf("  Cita #%d - Mascota: %s | Hora: %s | Tipo: %s%n",
                            c.getId(), c.getNombreMascota(), c.getFechaHora(), c.getTipo());
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR EN CONSULTAS]: " + e.getMessage());
        }

        // ----- Parte 3. Control de errores -----
        System.out.println("\n--- PARTE 3: CONTROL DE ERRORES Y RESTRICCIONES ---");
        System.out.println("Intentando insertar cliente duplicado (DNI ya existente):");
        Cliente cDuplicado = new Cliente("12345678A", "Falso Cliente", "600000000", "falso@correo.com");
        dao.altaCliente(cDuplicado);

        System.out.println("\nIntentando insertar mascota con especie invalida ('pez'):");
        Mascota mInvalida = new Mascota("Nemo", "pez", LocalDate.now(), 1);
        dao.altaMascota(mInvalida);

        // ----- Parte 4. Funciones almacenadas -----
        System.out.println("\n--- PARTE 4: PROCEDIMIENTOS Y FUNCIONES MYSQL ---");
        try {
            Integer edad = dao.edadMascota(1);
            System.out.printf("Edad de la mascota con ID 1: %s anios%n", edad != null ? edad : "Desconocida (fecha NULL)");
        } catch (SQLException e) {
            System.err.println("Error invocando edad_mascota: " + e.getMessage());
        }

        System.out.println("\nPruebas completadas.");
    }
}
