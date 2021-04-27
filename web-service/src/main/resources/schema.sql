CREATE TABLE `gift_certificate`
(
    `id`               INTEGER        NOT NULL AUTO_INCREMENT,
    `name`             VARCHAR(255)   NOT NULL,
    `description`      VARCHAR(255)   NOT NULL,
    `price`            DECIMAL(10, 2) NOT NULL,
    `duration`         SMALLINT       NOT NULL,
    `create_date`      TIMESTAMP      NOT NULL,
    `last_update_date` TIMESTAMP,
    `is_active`        BOOL,
    CONSTRAINT PK_gift_certificate PRIMARY KEY (`id`)
);

CREATE TABLE `tag`
(
    `id`   INTEGER      NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    CONSTRAINT PK_tag PRIMARY KEY (`id`),
    CONSTRAINT UC_tag UNIQUE (name)
);

CREATE TABLE `certificate_tag`
(
    `id`             INTEGER NOT NULL AUTO_INCREMENT,
    `certificate_id` INTEGER,
    `tag_id`         INTEGER,
    CONSTRAINT PK_certificate_tag PRIMARY KEY (`id`),
    CONSTRAINT FK_certificate_id FOREIGN KEY (`certificate_id`) REFERENCES gift_certificate (`id`)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT FK_tag_id FOREIGN KEY (`tag_id`) REFERENCES tag (`id`)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT UC_certificate_tag UNIQUE (certificate_id, tag_id)
);
