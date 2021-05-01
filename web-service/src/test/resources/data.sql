INSERT INTO `user`
    (id, name, surname, age, email, is_active)
VALUES (1, 'Tais', 'Afinskaya', 17, 'tais@mail.ru', true),
       (2, 'Alexander', 'the Great', 25, 'makedonsky@mail.ru', true),
       (3, 'Ptolemy', 'Soter', 23, 'ptolemy@yandex.ru', true),
       (4, 'Ivan', 'Efremov', 64, 'ivan@gmail.com', true);


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
    (`id`, `certificate_id`, `tag_id`)
VALUES (1, 1, 1),
       (2, 1, 3),
       (3, 2, 1),
       (4, 2, 2),
       (5, 3, 4),
       (6, 3, 5);

INSERT INTO user_order
    (user_id, certificate_id, price, create_date)
VALUES (1, 1, 5, '2021-03-30T18:31:42.111'),
       (2, 2, 10, '2021-04-15T21:21:49.222'),
       (3, 3, 15, '2021-04-25T11:02:11.331')