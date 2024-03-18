--liquibase formatted sql

--changeset ublok:1
CREATE TABLE IF NOT EXISTS users
(
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(64) NOT NULL ,
    surname VARCHAR(64) NOT NULL ,
    email VARCHAR(128) NOT NULL UNIQUE ,
    password VARCHAR(256) NOT NULL ,
    registration_date DATE NOT NULL ,
    photo VARCHAR(128) ,
    phone_number VARCHAR(32) ,
    animal_preference VARCHAR(32) ,
    conversation_preference VARCHAR(32) ,
    music_preference VARCHAR(32)
);

--changeset ublok:2
CREATE TABLE IF NOT EXISTS documents (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id uuid REFERENCES users(id),
    type VARCHAR(32) NOT NULL,
    number VARCHAR(64) NOT NULL UNIQUE ,
    creation_date DATE NOT NULL
);

--changeset ublok:3
CREATE TABLE IF NOT EXISTS cars (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id uuid REFERENCES users(id),
    brand VARCHAR(32) NOT NULL,
    model VARCHAR(32) NOT NULL,
    year INTEGER,
    color VARCHAR(32),
    registration_number VARCHAR(32)
);

--changeset ublok:4
CREATE TABLE IF NOT EXISTS ratings (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    rated_user_id uuid REFERENCES users(id),
    rating_user_id uuid REFERENCES users(id),
    rating_value INT NOT NULL,
    rated_at TIMESTAMP NOT NULL
);