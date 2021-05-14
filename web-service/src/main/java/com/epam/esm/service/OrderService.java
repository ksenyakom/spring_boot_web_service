package com.epam.esm.service;

import com.epam.esm.model.Order;
import com.epam.esm.model.User;

import java.util.List;
import java.util.Set;

public interface OrderService {

    void save(Order entity) throws ServiceException;

    Order findById(Integer id) throws ServiceException;

    List<Order> findAll(int page, int size) throws ServiceException;

    List<Order> findByUser(User user, int page, int size) throws ServiceException;

    Order findById(int id, Set<String> fieldsToFind) throws ServiceException;

    void delete(Integer id) throws ServiceException;

    int countAll();

    int countByUser(User user);
}
