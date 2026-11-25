-- =====================================================================
-- Base de datos superheroes (PostgreSQL)
-- Acceso a Datos · JDBC: consultas y actualizaciones
--
-- Antes de ejecutar este script, crea la base de datos (una sola vez),
-- conectado a la base de datos postgres:
--
--     CREATE DATABASE superheroes ENCODING 'UTF8';
--
-- Después, conéctate a superheroes y ejecuta este script entero.
-- Se puede volver a ejecutar siempre que se quiera recuperar los datos
-- de partida: borra las tablas y las vuelve a crear.
-- =====================================================================

-- Primero se borra acts, porque depende de movies y characters
DROP TABLE IF EXISTS acts;
DROP TABLE IF EXISTS movies;
DROP TABLE IF EXISTS characters;

CREATE TABLE movies (
    id       SERIAL PRIMARY KEY,
    title    TEXT NOT NULL,
    duration INTEGER CHECK (duration > 0),        -- minutos
    year     INTEGER CHECK (year BETWEEN 1900 AND 2100),
    producer TEXT
);

CREATE TABLE characters (
    id      SERIAL PRIMARY KEY,
    name    TEXT NOT NULL,
    powers  TEXT,
    company TEXT,
    origin  TEXT,
    isHeroe INTEGER CHECK (isHeroe IN (0, 1))     -- 1: héroe, 0: villano o antihéroe
);

CREATE TABLE acts (
    id               SERIAL PRIMARY KEY,
    character_id     INTEGER NOT NULL,
    movie_id         INTEGER NOT NULL,
    minutes_in_movie INTEGER CHECK (minutes_in_movie >= 0),
    main_character   BOOLEAN NOT NULL,
    actor            TEXT,
    FOREIGN KEY (character_id) REFERENCES characters(id),
    FOREIGN KEY (movie_id) REFERENCES movies(id),
    -- Un personaje solo puede tener una actuación en cada película
    UNIQUE (character_id, movie_id)
);

INSERT INTO movies (title, duration, year, producer) VALUES
    ('The Avengers', 143, 2012, 'Marvel Studios'),                      -- 1
    ('Wonder Woman', 141, 2017, 'Warner Bros'),                         -- 2
    ('Deadpool', 108, 2016, '20th Century Fox'),                        -- 3
    ('Aquaman', 143, 2018, 'Warner Bros'),                              -- 4
    ('Black Panther', 134, 2018, 'Marvel Studios'),                     -- 5
    ('Wonder Woman 1984', 151, 2020, 'Warner Bros'),                    -- 6
    ('Guardians of the Galaxy Vol. 3', 150, 2023, 'Marvel Studios');    -- 7

INSERT INTO characters (name, powers, company, origin, isHeroe) VALUES
    ('Spider-Man', 'Agilidad, trepar paredes, sentido arácnido', 'Marvel', 'Nueva York', 1),              -- 1
    ('Batman', 'Alta inteligencia, habilidades marciales', 'DC', 'Gotham City', 1),                        -- 2
    ('Wonder Woman', 'Fuerza, agilidad, inmortalidad', 'DC', 'Themyscira', 1),                             -- 3
    ('Iron Man', 'Armadura avanzada, vuelo, armas láser', 'Marvel', 'Malibú', 1),                          -- 4
    ('Black Widow', 'Agilidad, combate cuerpo a cuerpo', 'Marvel', 'Rusia', 1),                            -- 5
    ('Superman', 'Fuerza, vuelo, visión de rayos láser', 'DC', 'Krypton', 1),                              -- 6
    ('Captain Marvel', 'Fuerza, vuelo, energía cósmica', 'Marvel', 'Planeta Hala', 1),                     -- 7
    ('Deadpool', 'Regeneración, inmortalidad', 'Marvel', 'Canadá', 0),                                     -- 8
    ('Aquaman', 'Control del agua, comunicación con criaturas marinas', 'DC', 'Atlantis', 1),              -- 9
    ('Harley Quinn', 'Experta en combate cuerpo a cuerpo, inteligencia', 'DC', 'Gotham City', 0),          -- 10
    ('Star-Lord', 'Habilidades para el combate, uso de tecnología avanzada', 'Marvel', 'Missouri (EE. UU.)', 1), -- 11
    ('Black Panther', 'Fuerza y agilidad sobrehumanas, traje de vibranio', 'Marvel', 'Wakanda', 1);        -- 12

-- Los minutos en pantalla son orientativos.
-- minutes_in_movie y actor pueden ser NULL (hay dos actuaciones sin minutos):
-- hay que tenerlo en cuenta al leerlos desde Java (getInt devuelve 0 → wasNull()).
INSERT INTO acts (character_id, movie_id, minutes_in_movie, main_character, actor) VALUES
    (4, 1, 60, true, 'Robert Downey Jr.'),        -- Iron Man en The Avengers
    (5, 1, 45, false, 'Scarlett Johansson'),      -- Black Widow en The Avengers
    (3, 2, 100, true, 'Gal Gadot'),               -- Wonder Woman en Wonder Woman
    (8, 3, 85, true, 'Ryan Reynolds'),            -- Deadpool en Deadpool
    (9, 4, 110, true, 'Jason Momoa'),             -- Aquaman en Aquaman
    (12, 5, 95, true, 'Chadwick Boseman'),        -- Black Panther en Black Panther
    (3, 6, NULL, true, 'Gal Gadot'),              -- Wonder Woman en Wonder Woman 1984
    (11, 7, NULL, true, 'Chris Pratt');           -- Star-Lord en Guardians of the Galaxy Vol. 3
