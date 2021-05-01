package com.epam.esm.service;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;

import java.util.List;

public interface OrderService {
    Order findById(Integer id) throws ServiceException;

    List<Order> findAll() throws ServiceException;

    void save(Order entity) throws ServiceException;

    void delete(Integer id) throws ServiceException;


}
