/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.taller_jdbc;

import java.math.BigDecimal;

/**
 *
 * @author adrian
 */
public class Cliente {

    private int id;
    private String dni;
    private String nombre;
    private String telefono;
    private BigDecimal saldo_bono;

    public Cliente() {
    }

    public Cliente(String dni, String nombre, String telefono, BigDecimal saldo_bono) {
        setDni(dni);
        setNombre(nombre);
        setTelefono(telefono);
        setSaldo_bono(saldo_bono);
    }

    public Cliente(int id, String dni, String nombre, String telefono, BigDecimal saldo_bono) {
        this(dni, nombre, telefono, saldo_bono);
        this.id = id;
    }

    public void setDni(String dni) {
        if (dni == null || dni.isBlank()) {
            throw new IllegalArgumentException("DNI no puede estar vacio");
        }
        String dniM = dni.toUpperCase();
        if (!dniM.matches("[0-9]{8}[A-Z]")) {
            throw new IllegalArgumentException("No cumple el formato");
        }
        this.dni = dniM;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("NOmbre no puede estar vacio");
        }
        if (nombre.length() > 100) {
            throw new IllegalArgumentException("Caracteres maximos pertmitidos para nombre: 100");
        }
        this.nombre = nombre;

    }

    public void setTelefono(String telefono) {
        if (telefono == null || telefono.isBlank()) {
            this.telefono = null;
            return;
        }
        if (telefono.length() > 20) {
            throw new IllegalArgumentException("Caracteres maximos permitidos para telefono: 20");
        }

        this.telefono = telefono.trim();
    }

    public void setSaldo_bono(BigDecimal saldo_bono) {
        if (saldo_bono == null) {
            throw new IllegalArgumentException("El saldo no puede estar vacio");
        }
        this.saldo_bono = saldo_bono;
    }


    public String getDni() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public BigDecimal getSaldo_bono() {
        return saldo_bono;
    }

}
