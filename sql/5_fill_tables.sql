USE `gift_db`;

insert into `user`
(id, name, surname, date_of_birth, email, is_active)
VALUES
(1, 'Tais', 'Afinskaya', '2000-03-30', 'tais@mail.ru', true),
(2, 'Alexander', 'the Great', '2001-03-30', 'makedonsky@mail.ru', true),
(3, 'Ptolemy', 'Soter', '2000-03-30', 'ptolemy@yandex.ru', true),
(4, 'Ivan', 'Efremov', '2001-03-30','ivan@gmail.com', true);

insert into `tag`
(`id`, `name`)
values
(1, 'beauty'),
(2, 'hand care'),
(3, 'hair care'),
(4, 'sport'),
(5, 'relax');

insert into `gift_certificate`
(`id`, `name`, `description`, `price`, `duration`, `create_date`,`is_active`)
values
(1, 'Haircut' , 'Any haircut with styling', 100, 30, '2021-03-30T18:31:42', true),
(2, 'Manicure', 'Manicure with long-lasting cover', 20, 60, '2021-03-20T18:31:42',true),
(3, 'Swimming pool', 'Visiting swimming pool with sauna', 15, 10, '2021-03-15T18:31:42',true);

insert into `certificate_tag`
(`certificate_id`, `tag_id`)
values
(1, 1),
(1, 3),
(2, 1),
(2, 2),
(3, 4),
(3, 5);