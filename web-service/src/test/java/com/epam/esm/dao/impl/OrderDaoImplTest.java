package com.epam.esm.dao.impl;

import com.epam.esm.dao.DaoException;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import org.junit.jupiter.api.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderDaoImplTest {
    private static EmbeddedDatabase embeddedDatabase;

    private static OrderDao orderDao;

    @BeforeAll
    public static void setUp() {
        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .addDefaultScripts()
                .setType(EmbeddedDatabaseType.H2)
                .build();

        JdbcTemplate jdbcTemplate = new JdbcTemplate(embeddedDatabase);

        orderDao = new OrderDaoImpl();
    }

    @AfterAll
    public static void tearDown() {
        embeddedDatabase.shutdown();
    }

    @Test
    void create() throws DaoException {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        Order order = new Order(new User(1), new GiftCertificate(1), now, new BigDecimal(100));

        orderDao.create(order);
        Order actual = orderDao.read(order.getId());

        assertAll("GiftCertificates should be equal",
                () -> {
                    assertNotNull(actual);
                    assertEquals(order.getUser().getId(), actual.getUser().getId());
                    assertEquals(order.getCertificate().getId(), actual.getCertificate().getId());
                    assertEquals(0, order.getPrice().compareTo(actual.getPrice()));
                    assertNotNull(actual.getCreateDate());
                    assertTrue(actual.isActive());
                });
        orderDao.delete(order.getId());
    }

    @Test
    void readException() {
        int notExistingId = 100;

        assertThrows(DaoException.class, () -> orderDao.read(notExistingId));
    }

    @Test
    void createException() {
        Order emptyOrder = new Order();

        assertThrows(DaoException.class, () -> orderDao.create(emptyOrder));
    }
    @org.junit.jupiter.api.Order(1)
    @Test
    void testRead() throws DaoException {
        int id = 2;
        Order order =  orderDao.read(id);
        assertAll(() -> {
            assertNotNull(order);
            assertEquals(id, order.getId());
            assertEquals(2, order.getUser().getId());
            assertEquals(2, order.getCertificate().getId());
            assertEquals(0, order.getPrice().compareTo(BigDecimal.TEN));
            assertNotNull(order.getCreateDate());
            assertTrue(order.isActive());
        });
    }

    @Test
    void update() throws DaoException {
        int id = 1;
        Order order = orderDao.read(id);
        assert order != null;
        order.setUser(new User(2));
        order.setCertificate(new GiftCertificate(2));
        order.setPrice(BigDecimal.ONE);
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        order.setCreateDate(now);
        orderDao.update(order);

        Order actual = orderDao.read(id);

        assertAll("Orders should be equal",
                () -> {
                    assertNotNull(actual);
                    assertEquals(actual, order);
                });
    }

    @Test
    void updateException() {
        int id = 1;
        Order order = new Order(id);
        assertThrows(DaoException.class, () -> orderDao.update(order));
    }

    @org.junit.jupiter.api.Order(2)
    @Test
    void delete() throws DaoException {
        int id = 2;
        orderDao.delete(id);
        Order actual = orderDao.read(id);

        assertAll(() -> {
            assertNotNull(actual);
            assertFalse(actual.isActive());
        });
    }

    @org.junit.jupiter.api.Order(1)
    @Test
    void readAllActive() throws DaoException {
        List<Order> orders = orderDao.readAllActive(1,5);
        assertAll("Should read all lines",
                () -> {
                    assertNotNull(orders);
                    assertEquals(3, orders.size());
                });
    }
}