package com.epam.esm.dao.impl;

import com.epam.esm.config.H2JpaConfig;
import com.epam.esm.config.TestConfig;
import com.epam.esm.dao.DaoException;
import com.epam.esm.dao.TagDao;
import com.epam.esm.model.Tag;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {H2JpaConfig.class, TestConfig.class})
@Transactional
@Sql({"/data.sql"})
class TagDaoImplTest {

    @Autowired
    private TagDao tagDao;

    @Test
    void create() throws DaoException {
        Tag tag = new Tag();
        tag.setName("education");
        tagDao.create(tag);
        Tag actual = tagDao.read(tag.getId());

        assertEquals(tag, actual);
    }

    @Test
    void readException() {
        int notExistingId = 100;
        assertThrows(DaoException.class, () -> tagDao.read(notExistingId));
    }

    @Test
    void createException() {
        Tag emptyTag = new Tag();

        assertNotNull(emptyTag.getId());
        assertThrows(DaoException.class, () -> tagDao.create(emptyTag));
    }

    @Test
    void deleteException() throws DaoException {
        int id = 6;
        tagDao.delete(id);
        assertThrows(DaoException.class, () -> tagDao.read(id));
    }

    @Test
    void readAll() throws DaoException {
        List<Tag> tags = tagDao.readAll(1, 6);
        assertAll("Should read all lines",
                () -> {
                    assertNotNull(tags);
                    assertEquals(6, tags.size());
                });
    }

    @Test
    void readByName() throws DaoException {
        String tagName = "care";
        List<Tag> tags = tagDao.readByPartName(tagName);
        assertEquals(2, tags.size());
    }

    @Test
    void checkIfExist() throws DaoException {
        assertTrue(tagDao.checkIfExist("beauty"));
        assertFalse(tagDao.checkIfExist("not exist"));
    }
}