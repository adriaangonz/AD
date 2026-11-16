/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.superheroes_jdbc;

/**
 *
 * @author adrian
 */
public class Personaje {
    private int id;
    private String name;
    private String powers;
    private String company;
    private String origin;
    private boolean isHeroe;

    public Personaje(int id, String name, String powers, String company, String origin, boolean isHeroe) {
        this.id = id;
        this.name = name;
        this.powers = powers;
        this.company = company;
        this.origin = origin;
        this.isHeroe = isHeroe;
    }

    public Personaje(String name, String powers, String company, String origin, boolean isHeroe) {
        this.name = name;
        this.powers = powers;
        this.company = company;
        this.origin = origin;
        this.isHeroe = isHeroe;
    }
    

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPowers() {
        return powers;
    }

    public String getCompany() {
        return company;
    }

    public String getOrigin() {
        return origin;
    }

    public boolean getIsHeroe() {
        return isHeroe;
    }

    @Override
    public String toString() {
        return "Personaje{" + "id=" + id + ", name=" + name + ", powers=" + powers + ", company=" + company + ", origin=" + origin + ", isHeroe=" + isHeroe + '}';
    }
    
    
}
