package com.epam.esm.service.impl;

import com.epam.esm.dao.DaoException;
import com.epam.esm.dao.UserDao;
import com.epam.esm.model.Tag;
import com.epam.esm.model.User;
import com.epam.esm.service.ServiceException;
import com.epam.esm.service.TagService;
import com.epam.esm.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDao userDao;

    private UserService userService;

    @BeforeEach
    void serUp() {
        userService = new UserServiceImpl(userDao);
    }

    @Test
    void findById() throws DaoException {
        int id = 1;
        User user = new User(id, "Tais", "Afinskaya", 17, "tais@mail.ru", true);
        given(userDao.read(id)).willReturn(user);
        User actual = userService.findById(id);
        assertEquals(actual, user);

    }

    @Test
    void findByIdException() throws DaoException {
        given(userDao.read(anyInt())).willThrow(DaoException.class);
        Integer id = anyInt();

        assertThrows(ServiceException.class, () -> userService.findById(id));
    }

    @Test
    void findAll() throws DaoException {
        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());
        given(userDao.readAll(anyInt(), anyInt())).willReturn(users);
        List<User> actual = userService.findAll(anyInt(), anyInt());

        assertEquals(2, actual.size());
    }

    @Test
    void findAllException() throws DaoException {
        given(userDao.readAll(anyInt(), anyInt())).willThrow(DaoException.class);

        assertThrows(ServiceException.class, () -> userService.findAll(anyInt(), anyInt()));
    }
}