package com.epam.esm.dao;

import com.epam.esm.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.util.List;

public interface UserDao {

    User read(Integer id) throws DaoException;

    List<User> readAll(int page, int size) throws DaoException;

    User readBestBuyer() throws DaoException;

    int countAllActive() throws DaoException;

}
