package com.epam.esm.dao;

import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface OrderDao {

    @NotNull
    void create(@NotNull Order entity) throws DaoException;

    @NotNull
    Order read(@NotNull Integer id) throws DaoException;

    List<Order> readByUser(User user, int page, int size) throws DaoException;

    @NotNull
    List<Order> readAllActive(int page, int size) throws DaoException;

    void update(@NotNull Order entity) throws DaoException;

    void delete(@NotNull Integer id) throws DaoException;

    int countAllActive() throws DaoException;

    int countActiveByUser(@NonNull User user) throws DaoException;
}
