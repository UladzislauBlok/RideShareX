--liquibase formatted sql

--changeset ublok:1
CREATE TABLE IF NOT EXISTS country
(
    code VARCHAR(2) PRIMARY KEY ,
    name VARCHAR(64) UNIQUE NOT NULL
);

--changeset ublok:2
CREATE TABLE IF NOT EXISTS city (
    code VARCHAR(3) PRIMARY KEY ,
    name VARCHAR(64) UNIQUE NOT NULL ,
    country VARCHAR(2) REFERENCES country(code) NOT NULL
);

--changeset ublok:3
CREATE TABLE IF NOT EXISTS trip (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    departure_city_code VARCHAR(3) REFERENCES city(code) NOT NULL ,
    arrival_city_code VARCHAR(3) REFERENCES city(code) NOT NULL ,
    departure_time TIMESTAMP NOT NULL ,
    arrival_time TIMESTAMP NOT NULL ,
    fare FLOAT NOT NULL ,
    status VARCHAR(32) NOT NULL ,
    max_passenger_capacity INT NOT NULL
);

--changeset ublok:4
CREATE TABLE IF NOT EXISTS trip_user (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    trip_id uuid REFERENCES trip(id) NOT NULL ,
    user_id uuid NOT NULL ,
    is_owner BOOLEAN NOT NULL
);

--changeset ublok:5
CREATE TABLE IF NOT EXISTS join_trip_attempt (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    trip_id uuid REFERENCES trip(id) NOT NULL ,
    status VARCHAR(32) NOT NULL
)