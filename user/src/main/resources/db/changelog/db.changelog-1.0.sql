--liquibase formatted sql

--changeset ublok:1
CREATE TABLE IF NOT EXISTS users
(
    id SERIAL PRIMARY KEY ,
    name VARCHAR(64) NOT NULL ,
    surname VARCHAR(64) NOT NULL ,
    email VARCHAR(128) NOT NULL UNIQUE ,
    password VARCHAR(256) NOT NULL ,
    registration_date DATE NOT NULL ,
    photo VARCHAR(128) ,
    animal_preference VARCHAR(32) ,
    conversation_preference VARCHAR(32) ,
    music_preference VARCHAR(32)
);

--changeset ublok:2
CREATE TABLE IF NOT EXISTS documents (
    id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(id),
    type VARCHAR(32) NOT NULL,
    number VARCHAR(64) NOT NULL UNIQUE ,
    creation_date DATE NOT NULL
);

--changeset ublok:3
CREATE TABLE IF NOT EXISTS cars (
    id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(id),
    brand VARCHAR(32) NOT NULL,
    model VARCHAR(32) NOT NULL,
    year INTEGER,
    color VARCHAR(32),
    registration_number VARCHAR(32)
);