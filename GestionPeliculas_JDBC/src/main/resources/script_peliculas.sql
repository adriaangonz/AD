-- Script de creacion de la base de datos bd_ad_01 y tabla movies
-- Unidad 1 - Practica: Gestion de peliculas con JDBC

CREATE DATABASE IF NOT EXISTS bd_ad_01;
USE bd_ad_01;

CREATE TABLE IF NOT EXISTS movies (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(150) NOT NULL,
    director VARCHAR(100),
    release_year INT,
    genre VARCHAR(50)
);

INSERT INTO movies (title, director, release_year, genre) VALUES
('Inception', 'Christopher Nolan', 2010, 'Ciencia Ficción'),
('Pulp Fiction', 'Quentin Tarantino', 1994, 'Thriller'),
('Interstellar', 'Christopher Nolan', 2014, 'Ciencia Ficción'),
('Spirited Away', 'Hayao Miyazaki', 2001, 'Animación'),
('The Dark Knight', 'Christopher Nolan', 2008, 'Acción');
