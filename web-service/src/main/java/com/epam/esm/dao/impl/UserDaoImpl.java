package com.epam.esm.dao.impl;

import com.epam.esm.dao.DaoException;
import com.epam.esm.dao.UserDao;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
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

import javax.persistence.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class UserDaoImpl implements UserDao {
    private static Logger logger = LogManager.getLogger(UserDaoImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Autowired
    public UserDaoImpl() {
    }

    @Override
    @Nullable
    public User read(@NonNull Integer id) throws DaoException {
        try {
            User user = em.find(User.class, id);
            if (user == null) {
                throw new DaoException(String.format("User with id = %s not found.", id), "404");
            }
            return user;
        } catch (IllegalArgumentException e) {
            throw new DaoException(String.format("Can not read User (id = %s)", id), "51", e);
        }
    }

    @Override
    public List<User> readAll(int page, int size) throws DaoException {
        try {
            String jpql = "SELECT u FROM User u WHERE u.isActive = true";
            Query query = em.createQuery(jpql);
            query.setFirstResult((page - 1) * size);
            query.setMaxResults(size);
            return query.getResultList();
        } catch (PersistenceException e) {
            throw new DaoException("Can not read all Users", "52", e);
        }
    }


    private static final String READ_MAX_SUM_USER = "SELECT user_id AS id, sum(price) AS sum_amount FROM user_order GROUP BY  user_id ORDER BY sum_amount DESC LIMIT 1";

    @Override
    @Nullable
    public User readBestBuyer() throws DaoException {
        try {
            Query query = em.createNativeQuery(READ_MAX_SUM_USER);
            Object object = query.getSingleResult();
            Object[] objects = (Object[]) object;
            int id = (int)objects[0];
            return new User(id);
        } catch (NoResultException e) {
            return null;
        } catch (PersistenceException e1) {
            throw new DaoException("Can not read user with max sum", "39", e1);
        }
    }

    @Override
    public int countAllActive() throws DaoException {
        try {
            Query query = em.createQuery("SELECT count(u) FROM User u WHERE u.isActive = true");
            long count = (long) query.getSingleResult();
            return (int) count;
        } catch (PersistenceException e) {
            throw new DaoException("Can't count active users.", "36");
        }
    }
}
