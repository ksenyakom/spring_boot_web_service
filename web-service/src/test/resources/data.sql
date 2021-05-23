INSERT INTO `user`
    (id, name, surname, date_of_birth, email, is_active)
VALUES (1, 'Tais', 'Afinskaya', '2000-03-30', 'tais@mail.ru', true),
       (2, 'Alexander', 'the Great', '2001-03-30', 'makedonsky@mail.ru', true),
       (3, 'Ptolemy', 'Soter', '2005-03-30', 'ptolemy@yandex.ru', true),
       (4, 'Ivan', 'Efremov', '1975-03-30', 'ivan@gmail.com', true);


INSERT INTO `tag`
    (`id`, `name`)
VALUES (1, 'beauty'),
       (2, 'hand care'),
       (3, 'hair care'),
       (4, 'sport'),
       (5, 'relax'),
       (6, 'to delete');


INSERT INTO `gift_certificate`
(`id`, `name`, `description`, `price`, `duration`, `create_date`, `is_active`)
VALUES (1, 'Haircut', 'Any haircut with styling', 100, 30, '2021-03-30T18:31:42', true),
       (2, 'Manicure', 'Manicure with long-lasting cover', 20, 60, '2021-03-20T18:31:42', true),
       (3, 'Swimming pool', 'Visiting swimming pool with sauna', 15, 10, '2021-03-15T18:31:42', true);

INSERT INTO `certificate_tag`
    (`certificate_id`, `tag_id`)
VALUES (1, 1),
       (1, 3),
       (2, 1),
       (2, 2),
       (3, 4),
       (3, 5);

INSERT INTO user_order
    (id, user_id, certificate_id, price, create_date, is_active)
VALUES (1, 1, 1, 5, '2021-03-30T18:31:42.111',true),
       (2, 2, 2, 10, '2021-04-15T21:21:49.222',true),
       (3, 3, 3, 15, '2021-04-25T11:02:11.331',true)