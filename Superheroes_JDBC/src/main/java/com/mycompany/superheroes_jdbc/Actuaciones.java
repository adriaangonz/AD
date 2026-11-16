/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.superheroes_jdbc;

/**
 *
 * @author adrian
 */
public class Actuaciones {
    private int id;
    private int characterId;
    private int movieId;
    private Integer minutesInMovie;
    private boolean mainCharacter;
    private String actor;

    public Actuaciones(int id, int characterId, int movieId, Integer minutesInMovie, boolean mainCharacter, String actor) {
        this.id = id;
        this.characterId = characterId;
        this.movieId = movieId;
        this.minutesInMovie = minutesInMovie;
        this.mainCharacter = mainCharacter;
        this.actor = actor;
    }

    public int getId() {
        return id;
    }

    public int getCharacterId() {
        return characterId;
    }

    public int getMovieId() {
        return movieId;
    }

    public Integer getMinutesInMovie() {
        return minutesInMovie;
    }

    public boolean isMainCharacter() {
        return mainCharacter;
    }

    public String getActor() {
        return actor;
    }
    
    
}
