package com.epam.esm.dao.impl;

import com.epam.esm.config.H2JpaConfig;
import com.epam.esm.config.TestConfig;
import com.epam.esm.dao.DaoException;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {H2JpaConfig.class, TestConfig.class})
@Transactional
@Sql({"/data.sql"})
class OrderDaoImplTest {
    @Autowired
    private OrderDao orderDao;

    @Test
    void create() throws DaoException {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        Order order = new Order(new User(1), Collections.singletonList(new GiftCertificate(1)), now, new BigDecimal(100));
        order.setActive(true);

        orderDao.create(order);
        Order actual = orderDao.read(order.getId());

        assertAll("GiftCertificates should be equal",
                () -> {
                    assertNotNull(actual);
                    assertEquals(order.getUser().getId(), actual.getUser().getId());
                    assertEquals(order.getCertificates().size(), actual.getCertificates().size());
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
    void testRead() throws DaoException {
        int id = 2;
        Order order = orderDao.read(id);
        assertAll(() -> {
            assertNotNull(order);
            assertEquals(id, order.getId());
            assertEquals(2, order.getUser().getId());
            assertEquals(1, order.getCertificates().size());
            assertEquals(0, order.getPrice().compareTo(BigDecimal.TEN));
            assertNotNull(order.getCreateDate());
            assertTrue(order.isActive());
        });
    }

    @Test
    void deleteException() throws DaoException {
        int id = 2;
        orderDao.delete(id);
        assertThrows(DaoException.class, () -> orderDao.read(id));
    }

    @Test
    void readAllActive() throws DaoException {
        List<Order> orders = orderDao.readAllActive(1, 5);
        assertAll("Should read all lines",
                () -> {
                    assertNotNull(orders);
                    assertEquals(3, orders.size());
                });
    }
}