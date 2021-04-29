package com.epam.esm.dao.impl;

import com.epam.esm.dao.DaoException;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.model.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoImplTest {
    private static EmbeddedDatabase embeddedDatabase;

    private static UserDao userDao;

    @BeforeAll
    public static void setUp() {
        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .addDefaultScripts()
                .setType(EmbeddedDatabaseType.H2)
                .build();

        JdbcTemplate jdbcTemplate = new JdbcTemplate(embeddedDatabase);

        userDao = new UserDaoImpl(jdbcTemplate);
    }

    @AfterAll
    public static void tearDown() {
        embeddedDatabase.shutdown();
    }

    @Test
    void read() throws DaoException {
        int id = 2;
        User actual = userDao.read(id);
        assertAll(() -> {
            assertNotNull(actual);
            assertEquals("Alexander", actual.getName());
            assertEquals("the Great", actual.getSurname());
            assertEquals("makedonsky@mail.ru", actual.getEmail());
            assertEquals(25, actual.getAge());
            assertEquals(id, actual.getId());
            assertTrue(actual.isActive());
        });

    }

    @Test
    void readException() {
        int notExistingId = 100;
        assertThrows(DaoException.class, () -> userDao.read(notExistingId));
    }


    @Test
    void testRead() throws DaoException {
        int id = 2;
        User user = new User(id);
        userDao.read(user);
        assertAll(() -> {
            assertNotNull(user);
            assertEquals("Alexander", user.getName());
            assertEquals("the Great", user.getSurname());
            assertEquals("makedonsky@mail.ru", user.getEmail());
            assertEquals(25, user.getAge());
            assertEquals(id, user.getId());
            assertTrue(user.isActive());
        });
    }

    @Test
    void readAll() {
        assertAll("Should read all lines",
                () -> {
                    assertNotNull(userDao.readAll());
                    assertEquals(4, userDao.readAll().size());
                });
    }
}