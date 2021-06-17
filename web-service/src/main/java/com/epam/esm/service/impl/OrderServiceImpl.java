package com.epam.esm.service.impl;

import com.epam.esm.dao.*;
import com.epam.esm.model.*;
import com.epam.esm.service.mapper.impl.OrderMapperImpl;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderDao orderDao;
    private final UserDao userDao;
    private final GiftCertificateDao giftCertificateDao;

    private final OrderMapperImpl orderMapperImpl;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao, UserDao userDao, OrderMapperImpl orderMapperImpl, GiftCertificateDao giftCertificateDao) {
        this.orderDao = orderDao;
        this.userDao = userDao;
        this.orderMapperImpl = orderMapperImpl;
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
            BigDecimal price = BigDecimal.ZERO;
            for (GiftCertificate certificate : order.getCertificates()) {
                certificate = giftCertificateDao.read(certificate.getId());
                price = price.add(certificate.getPrice());
            }
            if (order.getId() == null) {
                order.setPrice(price);
                order.setCreateDate(LocalDateTime.now(ZoneOffset.UTC));
                order.setActive(true);
                orderDao.create(order);
            } else {
                if (order.getPrice() == null) {
                    order.setPrice(price);
                }
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
