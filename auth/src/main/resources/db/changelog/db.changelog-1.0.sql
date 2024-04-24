--liquibase formatted sql

--changeset ublok:1
CREATE TABLE IF NOT EXISTS users (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(128) NOT NULL UNIQUE ,
    password VARCHAR(256) NOT NULL ,
    is_email_confirmed BOOLEAN NOT NULL
);

--changeset ublok:2
CREATE TABLE IF NOT EXISTS authority (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    authority VARCHAR(64) NOT NULL UNIQUE
);


--changeset ublok:3
CREATE TABLE IF NOT EXISTS user_authority (
    user_id uuid REFERENCES users(id),
    authority_id uuid REFERENCES authority(id),
    PRIMARY KEY (user_id, authority_id)
);

--changeset ublok:4
CREATE TABLE IF NOT EXISTS deactivated_token
(
    id uuid PRIMARY KEY ,
    c_keep_until TIMESTAMP NOT NULL CHECK ( c_keep_until > now() )
);