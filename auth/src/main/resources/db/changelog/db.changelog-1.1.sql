--liquibase formatted sql

--changeset ublok:1
INSERT INTO authority(authority)
VALUES ('ROLE_MANAGER'), ('ROLE_USER');