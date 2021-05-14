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

import java.util.List;

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

        userDao = new UserDaoImpl();
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
    void readAll() throws DaoException {
        List<User> users = userDao.readAll(1,5);
        assertAll("Should read all lines",
                () -> {
                    assertNotNull(users);
                    assertEquals(4, users.size());
                });
    }
}