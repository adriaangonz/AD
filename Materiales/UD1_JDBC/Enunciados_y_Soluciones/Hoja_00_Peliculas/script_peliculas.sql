


CREATE TABLE movies (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(150) NOT NULL,
    director VARCHAR(100),
    release_year INT,
    genre VARCHAR(50)
);


INSERT INTO movies (title, director, release_year, genre) VALUES
('Inception', 'Christopher Nolan', 2010, 'Sci-Fi'),
('Pulp Fiction', 'Quentin Tarantino', 1994, 'Crime'),
('Interstellar', 'Christopher Nolan', 2014, 'Sci-Fi'),
('Spirited Away', 'Hayao Miyazaki', 2001, 'Animation'),
('The Dark Knight', 'Christopher Nolan', 2008, 'Action');


