package com.epam.esm.dao;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.Tag;
import com.epam.esm.model.User;
import org.springframework.jdbc.core.RowMapper;

import javax.validation.constraints.NotNull;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;

public interface OrderDao {
    RowMapper<Order> ROW_MAPPER = (ResultSet resultSet, int rowNum) -> {
        Order order = new Order();

        order.setId(resultSet.getInt("id"));
        order.setUser(new User(resultSet.getInt("user_id")));
        order.setCertificate(new GiftCertificate(resultSet.getInt("certificate_id")));
        Timestamp createDate = resultSet.getTimestamp("create_date");
        order.setCreateDate(createDate == null ? null : createDate.toLocalDateTime());
        order.setPrice(resultSet.getBigDecimal("price"));
        order.setActive(resultSet.getBoolean("is_active"));
        return order;
    };

    @NotNull
    Integer create(@NotNull Order entity) throws DaoException;

    @NotNull
    Order read(@NotNull Integer id) throws DaoException;

    @NotNull
    void read(@NotNull Order order) throws DaoException;

    @NotNull
    List<Order> readAllActive() throws DaoException;

    void update(@NotNull Order entity) throws DaoException;

    void delete(@NotNull Integer id) throws DaoException;



}
