package com.epam.esm.service.impl;

import com.epam.esm.dao.*;
import com.epam.esm.model.*;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderDao orderDao;

    private final UserDao userDao;

    private final GiftCertificateDao giftCertificateDao;

    private final TagDao tagDao;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao, UserDao userDao, GiftCertificateDao giftCertificateDao, TagDao tagDao) {
        this.orderDao = orderDao;
        this.userDao = userDao;
        this.giftCertificateDao = giftCertificateDao;
        this.tagDao = tagDao;
    }

    @Override
    public Order findById(Integer id) throws ServiceException {
        try {
            Order order = orderDao.read(id);
            userDao.read(order.getUser());
            giftCertificateDao.read(order.getCertificate());
            if (order.getCertificate() != null) {
                readTags(order.getCertificate().getTags());
            }

            return order;
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode(), e.getCause());
        }
    }

    @Override
    public List<Order> findAll() throws ServiceException {
        try {
            List<Order> orders = orderDao.readAllActive();
            readUsers(orders);
            readCertificates(orders);
            readOrderTags(orders);

            return orders;
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode(), e.getCause());
        }
    }

    @Transactional
    @Override
    public void save(Order order) throws ServiceException {
        try {

            if (order.getId() == null) {
                order.setCreateDate(LocalDateTime.now(ZoneOffset.UTC));
                order.setId(orderDao.create(order));
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

    private void readUsers(List<Order> orders) throws DaoException {
        List<User> users = orders.stream()
                .map(Order::getUser)
                .distinct()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        for (User user : users) {
            userDao.read(user);
        }

    }

    private void readCertificates(List<Order> orders) throws DaoException {
        List<GiftCertificate> certificates = orders.stream()
                .map(Order::getCertificate)
                .distinct()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        giftCertificateDao.read(certificates);
    }

    private void readTags(List<Tag> tags) throws DaoException {
        for (Tag tag : tags) {
            tagDao.read(tag);
        }
    }

    private void readOrderTags(List<Order> orders) throws DaoException {
        List<Tag> tags = orders.stream()
                .map(Order::getCertificate)
                .filter(Objects::nonNull)
                .flatMap(certificate -> certificate.getTags().stream() )
                .distinct()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        readTags(tags);
    }
}
