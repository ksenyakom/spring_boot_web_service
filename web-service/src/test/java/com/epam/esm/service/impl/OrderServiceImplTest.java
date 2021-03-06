package com.epam.esm.service.impl;

import com.epam.esm.dao.*;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import com.epam.esm.service.FillOrderFields;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderDao orderDao;
    @Mock
    private UserDao userDao;
    @Mock
    private GiftCertificateDao giftCertificateDao;
    @Mock
    private TagDao tagDao;
    @Mock
    private FillOrderFields fillOrderFields;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderServiceImpl(orderDao, userDao, fillOrderFields, giftCertificateDao);
    }

    @Test
    void findById() throws DaoException {
        int id = 3;
        given(orderDao.read(id)).willReturn(new Order(id));
        Order order = orderService.findById(id);

        assertEquals(id, order.getId());
    }

    @Test
    void findByIdException() throws DaoException {
        given(orderDao.read(anyInt())).willThrow(DaoException.class);
        Integer id = anyInt();

        assertThrows(ServiceException.class, () -> orderService.findById(id));
    }

    @Test
    void findAll() throws DaoException {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order());
        orders.add(new Order());
        given(orderDao.readAllActive(anyInt(), anyInt())).willReturn(orders);
        List<Order> actual = orderService.findAll(anyInt(), anyInt());

        assertEquals(2, actual.size());
    }

    @Test
    void findAllException() throws DaoException {
        given(orderDao.readAllActive(anyInt(), anyInt())).willThrow(DaoException.class);

        assertThrows(ServiceException.class, () -> orderService.findAll(anyInt(), anyInt()));
    }

}