/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.superheroes_jdbc;

/**
 *
 * @author adrian
 */
public class Pelicula {
    private int id;
    private String title;
    private Integer duration;
    private Integer year;
    private String producer;

    public Pelicula(int id, String title, Integer duration, Integer year, String producer) {
        this.id = id;
        this.title = title;
        this.duration = duration;
        this.year = year;
        this.producer = producer;
    }

    public Pelicula(String title, Integer duration, Integer year, String producer) {
        this.title = title;
        this.duration = duration;
        this.year = year;
        this.producer = producer;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Integer getDuration() {
        return duration;
    }

    public Integer getYear() {
        return year;
    }

    public String getProducer() {
        return producer;
    }

    @Override
    public String toString() {
        return "Pelicula{" + "id=" + id + ", title=" + title + ", duration=" + duration + ", year=" + year + ", producer=" + producer + '}';
    }
    
    
}
