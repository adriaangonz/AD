/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_batch_callable;

/**
 *
 * @author adrian
 */
public class Libro {

    private int num_libro;
    private String titulo;
    private String autor;
    private int ejemplares;
    private int disponibles;

    public Libro(int num_libro, String titulo, String autor, int ejemplares, int disponibles) {

    }

    public Libro(String titulo, String autor, int ejemplares, int disponibles) {
        setTitulo(titulo);
        this.autor = autor;
        this.ejemplares = ejemplares;
        this.disponibles = disponibles;
    }

    public Libro() {
    }

    public void setTitulo(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("EL titulo no puede estar vacio");
        }
        if (titulo.length() > 80) {
            throw new IllegalArgumentException("eL TITULO no puede superar 80 caracteres");
        }
        this.titulo = titulo;
    }

    public void setAutor(String autor) {
        if (autor == null || autor.isBlank()) {
            throw new IllegalArgumentException("EL autor no puede estar vacio");
        }
        if (autor.trim().length() > 60) {
            throw new IllegalArgumentException("eL autor no puede superar 80 caracteres");
        }
        this.autor = autor.trim();
    }

    public void setEjemplares(int ejemplares) {
        if (ejemplares < 0) {
            throw new IllegalArgumentException("Tiene que ser un numero entero");
        }
        this.ejemplares = ejemplares;
    }

    public void setDisponibles(int disponibles) {
        if (disponibles < 0 || disponibles > ejemplares) {
            throw new IllegalArgumentException("Tiene que ser un numero entero y menor que el de ejemplares");
        }
        this.disponibles = disponibles;
    }

    public int getNum_libro() {
        return num_libro;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public int getEjemplares() {
        return ejemplares;
    }

    public int getDisponibles() {
        return disponibles;
    }

}
