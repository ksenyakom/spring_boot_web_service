package com.epam.esm.dao.impl;

import com.epam.esm.dao.DaoException;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.impl.TagDaoImpl;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TagDaoImplTest {
    private static EmbeddedDatabase embeddedDatabase;

    private static TagDao tagDao;

    @BeforeAll
    public static void setUp() {
        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .addDefaultScripts()
                .setType(EmbeddedDatabaseType.H2)
                .build();

        JdbcTemplate jdbcTemplate = new JdbcTemplate(embeddedDatabase);

        tagDao = new TagDaoImpl(jdbcTemplate);
    }

    @AfterAll
    public static void tearDown() {
        embeddedDatabase.shutdown();
    }

    @Test
    void create() throws DaoException {
        Tag tag = new Tag();
        tag.setName("education");
        tag.setId(tagDao.create(tag));
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
        assertThrows(DaoException.class, () -> tagDao.create(emptyTag));
    }

    @Test
    void deleteException() throws DaoException {
        int id = 6;
        tagDao.delete(id);
        assertThrows(DaoException.class, () -> tagDao.read(id));
    }

    @Test
    void readAll() {
        assertAll("Should read all lines",
                () -> {
                    assertNotNull(tagDao.readAll());
                    assertEquals(6, tagDao.readAll().size());
                });
    }

    @Test
    void readCertificateByTag() throws DaoException {
        int id = 1;
        List<GiftCertificate> certificates = tagDao.readCertificateByTag(id);
        assertEquals(2, certificates.size());
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