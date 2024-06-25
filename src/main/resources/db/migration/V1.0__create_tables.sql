CREATE TABLE authors (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL);

CREATE TABLE books (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    isbn VARCHAR(20) UNIQUE,
    author_id INT NOT NULL,
    CONSTRAINT fk_author_id FOREIGN KEY (author_id) REFERENCES authors(id));
