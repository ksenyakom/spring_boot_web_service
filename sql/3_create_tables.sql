USE `gift_db`;

CREATE TABLE `user`
(
    `id`            INT          NOT NULL AUTO_INCREMENT,
    `name`          VARCHAR(255) NOT NULL,
    `surname`       VARCHAR(255) NOT NULL,
    `date_of_birth` DATE,
    `email`         VARCHAR(255) NOT NULL,
    `is_active`     BOOL DEFAULT true,

    CONSTRAINT PK_user PRIMARY KEY (`id`),
    CONSTRAINT UC_user UNIQUE (email)
);

CREATE TABLE `gift_certificate`
(
    `id`               INTEGER        NOT NULL AUTO_INCREMENT,
    `name`             VARCHAR(255)   NOT NULL,
    `description`      VARCHAR(255)   NOT NULL,
    `price`            DECIMAL(10, 2) NOT NULL,
    `duration`         SMALLINT       NOT NULL,
    `create_date`      TIMESTAMP      NOT NULL,
    `last_update_date` TIMESTAMP,
    `is_active`        BOOL DEFAULT true,
    `operation`        VARCHAR(255),
    `timestamp`        TIMESTAMP,
    CONSTRAINT PK_gift_certificate PRIMARY KEY (`id`)
);

CREATE TABLE `tag`
(
    `id`        INTEGER      NOT NULL AUTO_INCREMENT,
    `name`      VARCHAR(255) NOT NULL,
    `operation` VARCHAR(255),
    `timestamp` TIMESTAMP,
    CONSTRAINT PK_tag PRIMARY KEY (`id`),
    CONSTRAINT UC_tag UNIQUE (name)
);

CREATE TABLE `certificate_tag`
(
    `certificate_id` INTEGER,
    `tag_id`         INTEGER,
    CONSTRAINT PK_certificate_tag PRIMARY KEY (certificate_id, tag_id),
    CONSTRAINT FK_certificate_id FOREIGN KEY (`certificate_id`) REFERENCES gift_certificate (`id`)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT FK_tag_id FOREIGN KEY (`tag_id`) REFERENCES tag (`id`)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT UC_certificate_tag UNIQUE (certificate_id, tag_id)
);

CREATE TABLE `user_order`
(
    `id`             INT            NOT NULL AUTO_INCREMENT,
    `user_id`        INT,
    `price`          DECIMAL(10, 2) NOT NULL,
    `create_date`    TIMESTAMP      NOT NULL,
    `is_active`      BOOL DEFAULT true,
    `operation`      VARCHAR(255),
    `timestamp`      TIMESTAMP,
    CONSTRAINT PK_order PRIMARY KEY (`id`),
    CONSTRAINT FK_order_u_id FOREIGN KEY (`user_id`) REFERENCES user (`id`)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);


CREATE TABLE `order_certificate`
(
    `id`             INT NOT NULL AUTO_INCREMENT,
    `certificate_id` INTEGER,
    `order_id`       INTEGER,
    CONSTRAINT PK_order_certificate PRIMARY KEY (`id`),
    CONSTRAINT FK_certificate_id FOREIGN KEY (`certificate_id`) REFERENCES gift_certificate (`id`)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT FK_order_id FOREIGN KEY (`order_id`) REFERENCES user_order (`id`)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);



