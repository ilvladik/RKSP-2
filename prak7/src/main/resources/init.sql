CREATE TABLE IF NOT EXISTS note
(
    id SERIAL PRIMARY KEY,
    title VARCHAR(255),
    description VARCHAR(255)
);