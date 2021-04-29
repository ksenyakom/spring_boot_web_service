CREATE DATABASE `gift_db`;

CREATE USER 'gift_user'@'localhost'
IDENTIFIED BY 'gift_password';

GRANT SELECT,INSERT,UPDATE,DELETE
ON `gift_db`.*
TO 'gift_user'@'localhost';

