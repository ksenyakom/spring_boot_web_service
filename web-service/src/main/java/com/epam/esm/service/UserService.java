package com.epam.esm.service;

import com.epam.esm.model.User;

import java.util.List;

public interface UserService {
    User findById(Integer id) throws ServiceException;

    User findByEmail(String email) throws ServiceException;

    List<User> findAll(int page, int size) throws ServiceException;

    int countAll();

    void save(User user);

}
