CREATE TABLE users
(
    id       BIGSERIAL PRIMARY KEY,
    username VARCHAR(32) NOT NULL UNIQUE,
    email    VARCHAR(64) NOT NULL UNIQUE,
    password VARCHAR NOT NULL
);

CREATE UNIQUE INDEX unique_users_username_idx ON users (username);