/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.daw.prog.archivos_serializables;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author
 */

public class GestorCiudades {

    private final Path ficheroObj;

    public GestorCiudades(Path ficheroObj) {
        this.ficheroObj = ficheroObj;
    }

    public boolean esFicheroValido(Path ruta) {
        return Files.isRegularFile(ruta);
    }

    /**
     * Lee del CSV la ciudad de la línea indicada (la línea 1 es la cabecera).
     * Devuelve null si el número no corresponde a una ciudad.
     */
    public City leerCiudadCSV(Path csv, int numeroLinea) throws IOException {
        List<String> lineas = Files.readAllLines(csv);

        if (numeroLinea < 2 || numeroLinea > lineas.size()) {
            return null;
        }

        String[] columnas = lineas.get(numeroLinea - 1).split(",");
        return new City(Integer.parseInt(columnas[0]), columnas[1], columnas[2],
                columnas[3], Integer.parseInt(columnas[4]));
    }

    /**
     * Añade una ciudad al final del fichero de objetos. - Fichero nuevo o
     * vacío: flujo normal, que escribe la cabecera. - Fichero con contenido:
     * flujo sin cabecera, porque ya la tiene.
     */
    public void anadirCiudad(City ciudad) throws IOException {
        if (Files.exists(ficheroObj) && Files.size(ficheroObj) > 0) {
            try (ObjectOutputStream salida = new SinCabecera(
                    Files.newOutputStream(ficheroObj, StandardOpenOption.APPEND))) {
                salida.writeObject(ciudad);
            }
        } else {
            try (ObjectOutputStream salida = new ObjectOutputStream(
                    Files.newOutputStream(ficheroObj))) {
                salida.writeObject(ciudad);
            }
        }
    }

    /**
     * Lee todas las ciudades del fichero. Si el fichero no existe o está vacío,
     * devuelve una lista vacía.
     */
    public List<City> leerTodas() throws IOException, ClassNotFoundException {
        List<City> ciudades = new ArrayList<>();

        if (!Files.exists(ficheroObj) || Files.size(ficheroObj) == 0) {
            return ciudades;
        }
        //leer hasta que falle. readObject() Lee uno; si no queda ninguno, lanza EOFException
        try (ObjectInputStream entrada = new ObjectInputStream(Files.newInputStream(ficheroObj))) {
            while (true) {
                ciudades.add((City) entrada.readObject());
            }
        } catch (EOFException e) {
            // fin normal del fichero: no quedan más objetos
        }

        return ciudades;
    }

    public boolean hayCiudades() throws IOException, ClassNotFoundException {
        return !leerTodas().isEmpty();
    }

    /**
     * Ciudades ordenadas por nombre (orden natural de String) y, si coinciden,
     * desempatan por código de país.
     */
    public List<City> obtenerOrdenadas() throws IOException, ClassNotFoundException {
        List<City> ciudades = leerTodas();

       
        ciudades.sort((City a, City b) -> {
            int resultado = a.getNombre().compareTo(b.getNombre());

            if (resultado != 0) {
                return resultado;               // los nombres ya deciden el orden
            }

            return a.getCodigoPais().compareTo(b.getCodigoPais());   // desempate
        });

        return ciudades;
    }

    /**
     * Ciudades repetidas (mismo nombre y mismo código de país). Cada pareja
     * aparece una sola vez, con el texto "NOMBRE (PAIS): N veces". Al estar la
     * lista ordenada por nombre y país, las ciudades repetidas quedan juntas,
     * así que basta contar los grupos de ciudades iguales.
     */
    public List<String> obtenerRepetidas() throws IOException, ClassNotFoundException {
        List<City> ordenadas = obtenerOrdenadas();
        List<String> repetidas = new ArrayList<>();

        int i = 0;
        while (i < ordenadas.size()) {
            City actual = ordenadas.get(i);

            // contamos cuántas ciudades seguidas son iguales a la actual
            int veces = 1;
            while (i + veces < ordenadas.size() && mismaCiudad(actual, ordenadas.get(i + veces))) {
                veces++;
            }

            if (veces > 1) {
                repetidas.add(actual.getNombre() + " (" + actual.getCodigoPais() + "): " + veces + " veces");
            }

            i = i + veces;      // saltamos todo el grupo
        }

        return repetidas;
    }

    private boolean mismaCiudad(City a, City b) {
        return a.getNombre().equals(b.getNombre())
                && a.getCodigoPais().equals(b.getCodigoPais());
    }

    /**
     * Elimina todas las ciudades con ese nombre y código de país. Un fichero de
     * objetos no se puede modificar en el sitio: se lee todo, se cierra la
     * lectura y se reescribe el fichero completo. Devuelve cuántas ciudades se
     * han eliminado.
     */
    public int eliminar(String nombre, String codigoPais) throws IOException, ClassNotFoundException {
        String nombreBuscado = nombre.toUpperCase();
        String paisBuscado = codigoPais.toUpperCase();

        List<City> todas = leerTodas();
        List<City> restantes = new ArrayList<>();

        for (City ciudad : todas) {
            boolean coincide = ciudad.getNombre().equals(nombreBuscado)
                    && ciudad.getCodigoPais().equals(paisBuscado);
            if (!coincide) {
                restantes.add(ciudad);
            }
        }

        int eliminadas = todas.size() - restantes.size();

        if (eliminadas > 0) {
            try (ObjectOutputStream salida = new ObjectOutputStream(
                    Files.newOutputStream(ficheroObj))) {
                for (City ciudad : restantes) {
                    salida.writeObject(ciudad);
                }
            }
        }

        return eliminadas;
    }

    /**
     * Ciudades con la población máxima. Si hay empate, se devuelven todas.
     */
    public List<City> obtenerMasPobladas() throws IOException, ClassNotFoundException {
        List<City> masPobladas = new ArrayList<>();
        int maximo = 0;

        for (City ciudad : leerTodas()) {
            if (ciudad.getPoblacion() > maximo) {
                maximo = ciudad.getPoblacion();
                masPobladas.clear();
                masPobladas.add(ciudad);
            } else if (ciudad.getPoblacion() == maximo) {
                masPobladas.add(ciudad);
            }
        }

        return masPobladas;
    }

    /**
     * Ciudades con la población máxima. Si hay empate, se devuelven todas.
     * Versión con lambda: primero se busca el máximo con un bucle, y después se
     * filtra la lista con esa lambda.
     */
    public List<City> obtenerMasPobladasLambda() throws IOException, ClassNotFoundException {
     
    }

    /**
     * Exporta todas las ciudades del fichero de objetos a un CSV nuevo, con la
     * misma cabecera que el CSV de origen. Si ya existe, se sobrescribe.
     * Devuelve cuántas ciudades se han exportado.
     */
    public int exportarCSV(Path destino) throws IOException, ClassNotFoundException {
       
    }

    /**
     * también se podría abrir el fichero y escribir línea a línea try
     * (BufferedWriter w = Files.newBufferedWriter(destino)) {
     * w.write("Id,Nombre,CodigoPais,Provincia,Poblacion\n"); for (City c :
     * ciudades) { w.write(c.getId() + "," + c.getNombre() + "," + ...);
     * w.newLine(); } }
     */
}
