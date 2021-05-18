package com.epam.esm.dao.impl;

import com.epam.esm.dao.DaoException;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotNull;
import java.util.List;

@Repository
public class OrderDaoImpl implements OrderDao {
    private static Logger logger = LogManager.getLogger(OrderDaoImpl.class);


    @PersistenceContext
    private EntityManager em;

    @Override
    public @NotNull void create(@NotNull Order order) throws DaoException {
        try {
            em.persist(order);
            logger.debug("New order created with id={}", order.getId());
        } catch (PersistenceException e) {
            throw new DaoException(String.format("Can not create new Order. User id = %s ", order.getUser() == null ? null : order.getUser().getId()), "61", e);
        } catch (ConstraintViolationException e) {
            throw new DaoException("Can not create new Order, constraint violations", "42", e);
        }
    }

    @Override
    public @NotNull Order read(@NotNull Integer id) throws DaoException {
        try {
            Order order = em.find(Order.class, id);
            if (order == null) {
                throw new DaoException(String.format("Order with id = %s not found.", id), "404");
            }

            return order;
        } catch (IllegalArgumentException e) {
            throw new DaoException(String.format("Can not read Order (id = %s).", id), "61", e);
        }
    }

    @Override
    public void update(@NotNull Order order) throws DaoException {
        try {
            Order existingOrder = read(order.getId());
            order.setCreateDate(existingOrder.getCreateDate());
            em.merge(order);
            logger.debug("Order was updated with id={}", order.getId());
        } catch (PersistenceException e) {
            throw new DaoException(String.format("Can not update Order (id = %s)", order.getId()), "63", e);
        } catch (ConstraintViolationException e) {
            throw new DaoException("Can not create new Order, constraint violations", "42", e);
        }
    }

    @Override
    public void delete(@NotNull Integer id) throws DaoException {
        try {
            Order order = read(id);
            em.remove(order);
            logger.debug("Order was deleted(isActive=false) with id={}", id);
        } catch (PersistenceException e) {
            throw new DaoException(String.format("Can not delete Order (id = %s)", id), "64", e);
        }
    }

    @Override
    public @NotNull List<Order> readAllActive(int page, int size) throws DaoException {
        String jpql = "SELECT o FROM user_order o WHERE o.isActive = true";
        Query query = em.createQuery(jpql);
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);
        return query.getResultList();
    }

    @Override
    public int countAllActive() throws DaoException {
        try {
            Query query = em.createQuery("SELECT count(o) FROM user_order o WHERE o.isActive = true");
            long count = (long) query.getSingleResult();
            return (int) count;
        } catch (PersistenceException e) {
            throw new DaoException("Can't count active orders.", "37");
        }
    }

    @Override
    public List<Order> readByUser(@NonNull User user, int page, int size) throws DaoException {
        try {
            String jpql = "SELECT o FROM user_order o WHERE o.user = :user";
            Query query = em.createQuery(jpql);
            query.setParameter("user", user);
            query.setFirstResult((page - 1) * size);
            query.setMaxResults(size);

            return query.getResultList();
        } catch (PersistenceException e) {
            throw new DaoException(String.format("Can not read Orders for user id = %s", user.getId()), "66", e);
        }
    }

    @Override
    public int countActiveByUser(@NonNull User user) throws DaoException {
        try {
            String jpql = "SELECT count(o) FROM user_order o WHERE o.isActive = true";
            Query query = em.createQuery(jpql);
            long count = (long) query.getSingleResult();
            return (int) count;
        } catch (PersistenceException e) {
            throw new DaoException(String.format("Can not count Orders for user id = %s", user.getId()), "66", e);
        }
    }
}
