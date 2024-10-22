CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE users
(
    id       UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    username VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

INSERT INTO users (username, password)
VALUES ('admin', crypt('password', gen_salt('bf')));
