/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ej2;

import java.time.LocalTime;

/**
 *
 * @author adrian
 */
public class Cancion {
    private int numCancion;
    private String grupo;
    private LocalTime duracion;
    private String titulo;
    private int total_votos;

    public Cancion(int numCancion, String grupo, LocalTime duracion, String titulo, int total_votos) {
        this.numCancion = numCancion;
        this.grupo = grupo;
        this.duracion = duracion;
        this.titulo = titulo;
        this.total_votos = total_votos;
    }

    public Cancion(int numCancion, LocalTime duracion, String titulo) {
        this.numCancion = numCancion;
        this.duracion = duracion;
        this.titulo = titulo;
    }

    public Cancion(String grupo, String titulo, int total_votos) {
        this.grupo = grupo;
        this.titulo = titulo;
        this.total_votos = total_votos;
    }
    
    

    public int getNumCancion() {
        return numCancion;
    }

    public void setNumCancion(int numCancion) {
        this.numCancion = numCancion;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public LocalTime getDuracion() {
        return duracion;
    }

    public void setDuracion(LocalTime duracion) {
        this.duracion = duracion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getTotal_votos() {
        return total_votos;
    }

    public void setTotal_votos(int total_votos) {
        this.total_votos = total_votos;
    }
    
    
}
