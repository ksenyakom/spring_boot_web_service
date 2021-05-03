package com.epam.esm.dao.impl;

import com.epam.esm.dao.DaoException;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderDaoImpl implements OrderDao {
    private static Logger logger = LogManager.getLogger(OrderDaoImpl.class);

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public OrderDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String CREATE = "INSERT INTO user_order (user_id, certificate_id, price, create_date) values(?,?,?,?)";
    private static final String READ = "SELECT * FROM user_order WHERE id = ?";
    private static final String UPDATE = "UPDATE user_order SET user_id = ?, certificate_id = ?,price = ?, create_date = ? WHERE id = ?";
    private static final String DELETE = "UPDATE user_order SET is_active = false WHERE id = ? AND is_active = true";
    private static final String READ_ALL_ACTIVE = "SELECT * FROM user_order  WHERE is_active = true";
    private static final String READ_BY_USER = "SELECT id, certificate_id, price, create_date FROM user_order  WHERE is_active = true AND user_id = ?";

    @Override
    public @NotNull Integer create(@NotNull Order order) throws DaoException {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection
                        .prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);
                if (order.getUser() == null || order.getUser().getId() == null) {
                    ps.setNull(1, Types.INTEGER);
                } else {
                    ps.setInt(1, order.getUser().getId());
                }
                if (order.getCertificate() == null || order.getCertificate().getId() == null) {
                    ps.setNull(2, Types.INTEGER);
                } else {
                    ps.setInt(2, order.getCertificate().getId());
                }
                ps.setBigDecimal(3, order.getPrice());
                LocalDateTime createDate = order.getCreateDate();
                ps.setString(4, createDate == null ? null : createDate.toString());
                return ps;
            }, keyHolder);
            Number number = keyHolder.getKey();
            Integer orderId = number == null ? null : number.intValue();

            if (orderId == null) {
                throw new DaoException("There is no autoincremented index after trying to add record into table `user_order`", "32");
            }
            logger.debug("New order created with id={}", orderId);

            return orderId;
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Can not create new Order. User id = %s ", order.getUser() == null ? null : order.getUser().getId()), "61", e);
        }
    }

    @Override
    public @NotNull Order read(@NotNull Integer id) throws DaoException {
        try {
            List<Order> orders = jdbcTemplate.query(READ, ROW_MAPPER, id);
            if (orders.isEmpty() || orders.get(0) == null) {
                throw new DaoException(String.format("Order with id = %s not found.", id), "404");
            }

            return orders.get(0);
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Can not read Order (id = %s).", id), "61", e);
        }
    }

    @Override
    public @NotNull void read(@NotNull Order order) throws DaoException {
        try {
            List<Order> orders = jdbcTemplate.query(READ, (resultSet, i) -> {
                order.setUser(new User(resultSet.getInt("user_id")));
                order.setCertificate(new GiftCertificate(resultSet.getInt("certificate_id")));
                Timestamp createDate = resultSet.getTimestamp("create_date");
                order.setCreateDate(createDate == null ? null : createDate.toLocalDateTime());
                order.setPrice(resultSet.getBigDecimal("price"));
                return order;
            }, order.getId());
            if (orders.isEmpty()) {
                throw new DaoException(String.format("Order with id = %s not found.", order.getId()), "404");
            }

        } catch (DataAccessException e) {
            throw new DaoException(String.format("Can not read Order (id = %s)", order.getId()), "61", e);
        }
    }

    @Override
    public void update(@NotNull Order order) throws DaoException {
        try {
            int rowsEffected = jdbcTemplate.update(UPDATE,
                    order.getUser() == null ? null : order.getUser().getId(),
                    order.getCertificate() == null ? null : order.getCertificate().getId(),
                    order.getPrice(), order.getCreateDate(), order.getId());
            if (rowsEffected == 0) {
                throw new DaoException(String.format("Order with id = %s not found.", order.getId()), "404");
            }

            logger.debug("Order was updated with id={}", order.getId());
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Can not update Order (id = %s)", order.getId()), "63", e);
        }
    }

    @Override
    public void delete(@NotNull Integer id) throws DaoException {
        try {
            int rowsEffected = jdbcTemplate.update(DELETE, id);
            if (rowsEffected == 0) {
                throw new DaoException(String.format("Order with id = %s not found or not active.", id), "404");
            }
            logger.debug("Order was deleted(isActive=false) with id={}", id);
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Can not delete Order (id = %s)", id), "64", e);
        }
    }

    @Override
    public @NotNull List<Order> readAllActive() throws DaoException {
        Map<Integer, User> userMap = new HashMap<>();
        Map<Integer, GiftCertificate> certificateMap= new HashMap<>();

        try {
            List<Order> orders = jdbcTemplate.query(READ_ALL_ACTIVE, (resultSet, i) -> {
                Order order = new Order();
                order.setId(resultSet.getInt("id"));

                int id = resultSet.getInt("user_id");
                User user = userMap.merge(id, new User(id), (oldValue, newValue) -> oldValue);
                order.setUser(user);

                id = resultSet.getInt("certificate_id");
                GiftCertificate certificate = certificateMap.merge(id, new GiftCertificate(id), (oldValue, newValue) -> oldValue);
                order.setCertificate(certificate);

                Timestamp createDate = resultSet.getTimestamp("create_date");
                order.setCreateDate(createDate == null ? null : createDate.toLocalDateTime());

                order.setPrice(resultSet.getBigDecimal("price"));
                return order;
            });
            if (orders.isEmpty()) {
                throw new DaoException("No Orders was found in database", "404");
            }

            return orders;
        } catch (DataAccessException e) {
            throw new DaoException("Can not read all Orders", "65", e);
        }
    }

    @Override
    public List<Order> readByUser(@NonNull User user) throws DaoException {
        Map<Integer, GiftCertificate> certificateMap= new HashMap<>();

        try {
            List<Order> orders = jdbcTemplate.query(READ_BY_USER, (resultSet, i) -> {
                Order order = new Order();
                order.setUser(user);
                order.setId(resultSet.getInt("id"));

                int id = resultSet.getInt("certificate_id");
                GiftCertificate certificate = certificateMap.merge(id, new GiftCertificate(id), (oldValue, newValue) -> oldValue);
                order.setCertificate(certificate);

                Timestamp createDate = resultSet.getTimestamp("create_date");
                order.setCreateDate(createDate == null ? null : createDate.toLocalDateTime());

                order.setPrice(resultSet.getBigDecimal("price"));
                return order;
            }, user.getId());
            if (orders.isEmpty()) {
                throw new DaoException(String.format("No Orders was found in database for user id = %s", user.getId()), "404");
            }

            return orders;
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Can not read Orders for user id = %s", user.getId()), "66", e);
        }
    }
}
