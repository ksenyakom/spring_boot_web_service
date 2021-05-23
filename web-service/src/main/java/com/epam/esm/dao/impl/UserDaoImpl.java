package com.epam.esm.dao.impl;

import com.epam.esm.dao.DaoException;
import com.epam.esm.dao.UserDao;
import com.epam.esm.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.util.List;

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
