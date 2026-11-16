package com.dam2.ad.preguntastest.modelo;

import java.time.LocalDate;

public class Usuario {
    private int idUsuario;
    private String usuario;
    private String password;
    private String nombre;
    private String apellidos;
    private LocalDate fechaNac;
    private int numAccesos;
    private int testRealizados;
    private float puntuacionMedia;

    public Usuario() {
    }

    public Usuario(int idUsuario, String usuario, String password, String nombre, String apellidos,
                   LocalDate fechaNac, int numAccesos, int testRealizados, float puntuacionMedia) {
        this.idUsuario = idUsuario;
        this.usuario = usuario;
        this.password = password;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fechaNac = fechaNac;
        this.numAccesos = numAccesos;
        this.testRealizados = testRealizados;
        this.puntuacionMedia = puntuacionMedia;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public LocalDate getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(LocalDate fechaNac) {
        this.fechaNac = fechaNac;
    }

    public int getNumAccesos() {
        return numAccesos;
    }

    public void setNumAccesos(int numAccesos) {
        this.numAccesos = numAccesos;
    }

    public int getTestRealizados() {
        return testRealizados;
    }

    public void setTestRealizados(int testRealizados) {
        this.testRealizados = testRealizados;
    }

    public float getPuntuacionMedia() {
        return puntuacionMedia;
    }

    public void setPuntuacionMedia(float puntuacionMedia) {
        this.puntuacionMedia = puntuacionMedia;
    }

    public String getNombreCompleto() {
        return nombre + " " + apellidos;
    }

    @Override
    public String toString() {
        return String.format("%s (%s %s) - Accesos: %d | Tests: %d | Media: %.2f",
                usuario, nombre, apellidos, numAccesos, testRealizados, puntuacionMedia);
    }
}
