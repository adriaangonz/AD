package com.daw.prog.biblioteca_mysql;

import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author subie
 */
public class BibliotecaApp {

    public static void main(String[] args) throws SQLException, IOException {

        MetodosClase m1 = new MetodosClase();

        m1.insertarDuplicado();
        
        //m1.prestar("S004", "El Quijote");

//m1.ejecutarScript(); //lo cometno apra no estar todos el rato ejecutando-> podría eerminar cometiendo error 
//m1.prestar("S002", "Platero y yo"); //ya lo he prestado no vuelvo a prestarlo al musmo usuario - soci
       // System.out.println("De Neibla me quedadn: " + m1.librosDisponibles("Niebla"));

        //System.out.println("Préstamos activos S001: " + m1.prestamosActivos("S001"));
    }
}
