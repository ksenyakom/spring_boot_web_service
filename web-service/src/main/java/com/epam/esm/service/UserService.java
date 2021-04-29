package com.epam.esm.service;

import com.epam.esm.model.User;

import java.util.List;

public interface UserService {
    User findById(Integer id) throws ServiceException;

    List<User> findAll() throws ServiceException;
}
