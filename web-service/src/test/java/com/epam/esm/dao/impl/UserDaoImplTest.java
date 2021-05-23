package com.epam.esm.dao.impl;

import com.epam.esm.config.H2JpaConfig;
import com.epam.esm.config.TestConfig;
import com.epam.esm.dao.DaoException;
import com.epam.esm.dao.UserDao;
import com.epam.esm.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {H2JpaConfig.class, TestConfig.class})
@Sql({"/data.sql"})
@Transactional
class UserDaoImplTest {

    @Autowired
    private UserDao userDao;

    @Test
    void read() throws DaoException {
        int id = 2;
        User actual = userDao.read(id);
        assertAll(() -> {
            assertNotNull(actual);
            assertEquals("Alexander", actual.getName());
            assertEquals("the Great", actual.getSurname());
            assertEquals("makedonsky@mail.ru", actual.getEmail());
            assertEquals(LocalDate.parse("2001-03-30"), actual.getDateOfBirth());
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