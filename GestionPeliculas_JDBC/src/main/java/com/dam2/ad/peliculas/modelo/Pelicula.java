package com.dam2.ad.peliculas.modelo;

/**
 * Clase de modelo (POJO / Entidad) que representa una pelicula en la tabla 'movies'.
 * 
 * Contiene los atributos que se corresponden con las columnas de la tabla:
 * - id: INT PRIMARY KEY AUTO_INCREMENT
 * - title: VARCHAR(150)
 * - director: VARCHAR(100)
 * - releaseYear: INT (release_year en la BD)
 * - genre: VARCHAR(50)
 */
public class Pelicula {

    private int id;
    private String title;
    private String director;
    private int releaseYear;
    private String genre;

    // Constructor vacio por defecto
    public Pelicula() {
    }

    // Constructor sin ID (usado para inserciones donde el ID lo autogenera la base de datos)
    public Pelicula(String title, String director, int releaseYear, String genre) {
        this.title = title;
        this.director = director;
        this.releaseYear = releaseYear;
        this.genre = genre;
    }

    // Constructor completo con todos los atributos (usado al mapear desde ResultSet)
    public Pelicula(int id, String title, String director, int releaseYear, String genre) {
        this.id = id;
        this.title = title;
        this.director = director;
        this.releaseYear = releaseYear;
        this.genre = genre;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return String.format("[%d] %s (%d) | Director: %s | Genero: %s",
                id, title, releaseYear, (director != null ? director : "Desconocido"), (genre != null ? genre : "N/A"));
    }
}
