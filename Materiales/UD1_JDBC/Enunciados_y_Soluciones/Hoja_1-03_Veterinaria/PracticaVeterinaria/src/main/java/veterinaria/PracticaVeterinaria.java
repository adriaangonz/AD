package veterinaria;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Programa principal de la práctica.
 *
 * Parte 0: ejecútalo tal cual para comprobar que la conexión funciona.
 * Después, añade aquí las pruebas de cada apartado del enunciado.
 */
public class PracticaVeterinaria {

    public static void main(String[] args) throws SQLException {

        // ----- Parte 0. Comprobación de la conexión -----
        String sql = "SELECT DATABASE(), CURRENT_USER(), VERSION(), (SELECT COUNT(*) FROM citas)";
        try (Connection con = ConexionBD.getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            rs.next();
            System.out.println("Conexión correcta");
            System.out.println("  Base de datos: " + rs.getString(1));
            System.out.println("  Usuario:       " + rs.getString(2));
            System.out.println("  Servidor:      " + rs.getString(3));
            System.out.println("  Citas:         " + rs.getInt(4));
        }

        ClinicaDAO dao = new ClinicaDAO();

    
    }
}
