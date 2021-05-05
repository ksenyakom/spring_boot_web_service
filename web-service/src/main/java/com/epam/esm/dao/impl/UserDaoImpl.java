package com.epam.esm.dao.impl;

import com.epam.esm.dao.DaoException;
import com.epam.esm.dao.UserDao;
import com.epam.esm.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {
    private static Logger logger = LogManager.getLogger(UserDaoImpl.class);

    private final JdbcTemplate jdbcTemplate;

    private static final String READ = "SELECT * FROM user WHERE id = ? ";
    private static final String READ_ALL = "SELECT * FROM user WHERE is_active = true";

    @Autowired
    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Nullable
    public User read(@NonNull Integer id) throws DaoException {
        try {
            List<User> users = jdbcTemplate.query(READ, ROW_MAPPER, id);
            if (users.isEmpty()) {
                throw new DaoException(String.format("User with id = %s not found.", id), "404");
            }
            return users.get(0);
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Can not read User (id = %s)", id), "51", e);
        }
    }

    @Override
    public void read(User user) throws DaoException {
        try {
            List<User> users = jdbcTemplate.query(READ, (resultSet, i) -> {
                user.setName(resultSet.getString("name"));
                user.setSurname(resultSet.getString("surname"));
                user.setEmail(resultSet.getString("email"));
                user.setAge(resultSet.getInt("age"));
                return user;
            }, user.getId());

            if (users.isEmpty()) {
                throw new DaoException(String.format("User with id = %s not found.", user.getId()), "404");
            }

        } catch (DataAccessException e) {
            throw new DaoException(String.format("Can not read User (id = %s)", user.getId()), "51", e);
        }
    }

    @Override
    public List<User> readAll() throws DaoException {
        try {
            List<User> users = jdbcTemplate.query(READ_ALL, ROW_MAPPER);
            if (users.isEmpty()) {
                throw new DaoException("No users found in database", "404");
            }
            return users;
        } catch (DataAccessException e) {
            throw new DaoException("Can not read all Users", "52", e);
        }
    }


    private static final String READ_MAX_SUM_USER = "SELECT user_id, sum(price) AS sum_amount FROM user_order GROUP BY  user_id ORDER BY sum_amount DESC LIMIT 1";

    @Override
    @Nullable
    public User readBestBuyer() throws DaoException {
        try {
            List<User> user = jdbcTemplate.query(READ_MAX_SUM_USER, (resultSet, i) -> {
                User user1 = new User();
                user1.setId(resultSet.getInt("user_id"));
                return user1;
            });
            if (user.isEmpty()) {
                return null;
            }
            return user.get(0);
        } catch (DataAccessException e) {
            throw new DaoException("Can not read user with max sum", "?", e);
        }
    }
}
