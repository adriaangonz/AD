/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.daw.prog.archivos_serializables;

import java.io.Serializable;

/**
 *
 * @author 
 */
public class City implements Serializable{
    private static final long serialVersionUID = 1L;

    private int id;
    private String nombre;
    private String codigoPais;
    private String provincia;
    private int poblacion;

    public City(int id, String nombre, String codigoPais, String provincia, int poblacion) {
        this.id = id;
        this.nombre = nombre.toUpperCase();
        this.codigoPais = codigoPais.toUpperCase();
        this.provincia = provincia.toUpperCase();
        this.poblacion = poblacion;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCodigoPais() {
        return codigoPais;
    }

    public String getProvincia() {
        return provincia;
    }

    public int getPoblacion() {
        return poblacion;
    }

    @Override
    public String toString() {
        return nombre + " (" + codigoPais + ") - " + provincia + " - " + poblacion + " habitantes";
    }
}
