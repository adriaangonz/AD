package veterinaria;


import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import veterinaria.modelo.Cita;
import veterinaria.modelo.Mascota;
import veterinaria.modelo.Veterinario;

/**
 * Acceso a la base de datos veterinaria.
 *
 * Implementa cada método según el apartado del enunciado que se indica.
 * Cada método abre su propia conexión con ConexionBD.getConexion() dentro de
 * un try-with-resources. Puedes añadir los métodos privados que necesites.
 */
public class ClinicaDAO {

    // =====================================================================
    // PARTE 2. Consultas
    // =====================================================================

    /** 2.1 Veterinarios activos, ordenados por nombre. */
    public List<Veterinario> veterinariosActivos() throws SQLException {
        
        return null;
        
    }

    /** 2.2 Mascotas del cliente con ese DNI, ordenadas por nombre. */
    public List<Mascota> mascotasDeCliente(String dni) throws SQLException {
        
        return null;
        
    }

    /** 2.3 Citas pendientes de un veterinario en un día, ordenadas por hora. */
    public List<Cita> citasPendientes(int idVeterinario, LocalDate dia) throws SQLException {
        
        return null;
        
    }

}
