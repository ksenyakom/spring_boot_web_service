package com.epam.esm.service.impl;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.dao.*;
import com.epam.esm.model.*;
import com.epam.esm.service.FillOrderFields;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderDao orderDao;
    private final UserDao userDao;
    private final GiftCertificateDao giftCertificateDao;

    private final FillOrderFields fillOrderFields;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao, UserDao userDao, FillOrderFields fillOrderFields, GiftCertificateDao giftCertificateDao) {
        this.orderDao = orderDao;
        this.userDao = userDao;
        this.fillOrderFields = fillOrderFields;
        this.giftCertificateDao = giftCertificateDao;
    }

    @Override
    public Order findById(Integer id) throws ServiceException {
        try {
            return orderDao.read(id);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode(), e.getCause());
        }
    }

    @Override
    public Order findById(int id, Set<String> fieldsToFind) {
        try {
            Order order = orderDao.read(id);
            Order orderToReturn = fillOrderFields.fill(order, fieldsToFind);
            return orderToReturn;
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode(), e.getCause());
        }
    }

    @Override
    public List<Order> findAll(int page, int size) throws ServiceException {
        try {
            return orderDao.readAllActive(page, size);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode(), e.getCause());
        }
    }

    @Override
    public int countAll() {
        try {
            return orderDao.countAllActive();
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode(), e.getCause());
        }
    }

    @Override
    public int countByUser(User user) {
        try {
            return orderDao.countActiveByUser(user);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode(), e.getCause());
        }
    }

    @Override
    public List<Order> findByUser(User user, int page, int size) throws ServiceException {
        try {
            userDao.read(user.getId());
            return orderDao.readByUser(user, page, size);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode(), e.getCause());
        }
    }

    @Transactional
    @Override
    public void save(Order order) throws ServiceException {
        try {
            userDao.read(order.getUser().getId());
            giftCertificateDao.read(order.getCertificate().getId());
            if (order.getId() == null) {
                order.setCreateDate(LocalDateTime.now(ZoneOffset.UTC));
                order.setActive(true);
                orderDao.create(order);
            } else {
                orderDao.update(order);
            }
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode(), e.getCause());
        }
    }

    @Transactional
    @Override
    public void delete(Integer id) throws ServiceException {
        try {
            orderDao.delete(id);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode(), e.getCause());
        }
    }

}
