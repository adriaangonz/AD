package com.dam2.ad.biblioteca.sqlite;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Utilidad para restaurar la base de datos 'biblioteca.db' a partir de
 * 'biblioteca_original.db' para permitir la repeticion controlada de las pruebas.
 */
public class RestaurarBD {

    private static final Path ORIGINAL = Paths.get("biblioteca_original.db");
    private static final Path DESTINO = Paths.get("biblioteca.db");

    public static boolean restaurar() {
        if (!Files.exists(ORIGINAL)) {
            System.err.println("[AVISO] No se encuentra el archivo 'biblioteca_original.db' en la raiz del proyecto.");
            return false;
        }

        try {
            Files.copy(ORIGINAL, DESTINO, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("[OK] Base de datos 'biblioteca.db' restaurada con exito al estado original.");
            return true;
        } catch (IOException e) {
            System.err.println("[ERROR] No se pudo restaurar 'biblioteca.db': " + e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        restaurar();
    }
}
