/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ej2;

import java.time.LocalDate;

/**
 *
 * @author adrian
 */
public class Grupo {
    private int codgrupo;
    private String nombre;
    private String localidad;
    private String estilo;
    private boolean esGrupo;
    private int annoGrab;
    private LocalDate fechaEstreno;
    private String compania;

    public Grupo(int codgrupo, String nombre, String localidad, String estilo, boolean esGrupo, int annoGrab, LocalDate fechaEstreno, String compania) {
        this.codgrupo = codgrupo;
        this.nombre = nombre;
        this.localidad = localidad;
        this.estilo = estilo;
        this.esGrupo = esGrupo;
        this.annoGrab = annoGrab;
        this.fechaEstreno = fechaEstreno;
        this.compania = compania;
    }

    public int getCodgrupo() {
        return codgrupo;
    }

    public void setCodgrupo(int codgrupo) {
        this.codgrupo = codgrupo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getEstilo() {
        return estilo;
    }

    public void setEstilo(String estilo) {
        this.estilo = estilo;
    }

    public boolean isEsGrupo() {
        return esGrupo;
    }

    public void setEsGrupo(boolean esGrupo) {
        this.esGrupo = esGrupo;
    }

    public int getAnnoGrab() {
        return annoGrab;
    }

    public void setAnnoGrab(int annoGrab) {
        this.annoGrab = annoGrab;
    }

    public LocalDate getFechaEstreno() {
        return fechaEstreno;
    }

    public void setFechaEstreno(LocalDate fechaEstreno) {
        this.fechaEstreno = fechaEstreno;
    }

    public String getCompania() {
        return compania;
    }

    public void setCompania(String compania) {
        this.compania = compania;
    }
    
    
}
