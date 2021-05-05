package com.epam.esm.dao;

import com.epam.esm.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.util.List;

public interface UserDao {

    RowMapper<User> ROW_MAPPER = (ResultSet resultSet, int rowNum) -> {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setName(resultSet.getString("name"));
        user.setSurname(resultSet.getString("surname"));
        user.setEmail(resultSet.getString("email"));
        user.setAge(resultSet.getInt("age"));
        return user;
    };

    User read(Integer id) throws DaoException;

    void read(User user) throws DaoException;

    List<User> readAll() throws DaoException;

    User readBestBuyer() throws DaoException;
}
