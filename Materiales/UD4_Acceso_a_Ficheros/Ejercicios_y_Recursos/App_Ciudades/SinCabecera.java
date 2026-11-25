/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.daw.prog.archivos_serializables;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 *
 * @author
 */
/**
 * Flujo para AÑADIR objetos a un fichero que ya contiene objetos.
 * No vuelve a escribir la cabecera, porque el fichero ya la tiene.
 * Es el visto en teoría
 */
public class SinCabecera extends ObjectOutputStream {

    public SinCabecera(OutputStream out) throws IOException {
        super(out);
    }

    @Override
    protected void writeStreamHeader() throws IOException {
        // No se puede dejar vacío: reset() indica al lector que vacíe su tabla
        // de objetos ya leídos. Sin esta línea, la lectura puede devolver datos
        // equivocados sin lanzar ningún error.
        reset();
    }
}
