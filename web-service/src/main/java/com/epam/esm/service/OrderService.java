package com.epam.esm.service;

import com.epam.esm.model.Order;
import com.epam.esm.model.User;

import java.util.List;
import java.util.Set;

public interface OrderService {
    Order findById(Integer id) throws ServiceException;

    List<Order> findAll() throws ServiceException;

    void save(Order entity) throws ServiceException;

    void delete(Integer id) throws ServiceException;

    List<Order> findByUser(User user) throws ServiceException;

    Order findById(int id, Set<String> fieldsToFind) throws ServiceException;
}
