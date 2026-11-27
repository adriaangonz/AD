/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.taller_jdbc;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 * @author adrian
 */
public class Reparacion {

    private int id;
    private String matricula;
    private String descripcion;
    private LocalDate fecha_entrada;
    private LocalDate fecha_salida;
    private BigDecimal coste;
    private Integer id_cliente;

    public Reparacion() {
    }

    public Reparacion(String matricula, String descripcion, LocalDate fecha_entrada, LocalDate fecha_salida, BigDecimal coste, Integer id_cliente) {
        setMatricula(matricula);
        setDescripcion(descripcion);
        setFecha_entrada(fecha_entrada);
        setFecha_salida(fecha_salida);
        setCoste(coste);
        setId_cliente(id_cliente);
    }

    public Reparacion(int id, String matricula, String descripcion, LocalDate fecha_entrada, LocalDate fecha_salida, BigDecimal coste, Integer id_cliente) {
        this(matricula, descripcion, fecha_entrada, fecha_salida, coste, id_cliente);
        this.id = id;
    }

    
    public void setMatricula(String matricula) {
        if (matricula == null || matricula.isBlank()) {
            throw new IllegalArgumentException("Matricula no puede estar vacia");
        }
        String matriculaM = matricula.toUpperCase();
        if (!matriculaM.matches("[0-9]{4}[A-Z]{3}")) {
            throw new IllegalArgumentException("No cumple el formato (0000BBB)");
        }
        this.matricula = matriculaM;
    }

    public void setDescripcion(String descripcion) {
        if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("Descripcion no puede estar vacia");
        }
        if (descripcion.length() > 200) {
            throw new IllegalArgumentException("nO PUEDE superar los 200 caracteres");
        }
        this.descripcion = descripcion;
    }

    public void setFecha_entrada(LocalDate fecha_entrada) {
        if (fecha_entrada == null) {
            throw new IllegalArgumentException("fecha no puede estar vacia");
        }
        this.fecha_entrada = fecha_entrada;
    }

    public void setFecha_salida(LocalDate fecha_salida) {
        if (fecha_salida == null) {
            this.fecha_salida = null;
            return;
        }
        this.fecha_salida = fecha_salida;
    }

    public void setCoste(BigDecimal coste) {
        if (coste == null) {
            this.coste = null;
            return;
        }
        this.coste = coste;
    }

    public void setId_cliente(Integer id_cliente) {
        if (id_cliente == null) {
            this.id_cliente = null;
            return;
        }
        this.id_cliente = id_cliente;
    }

}
