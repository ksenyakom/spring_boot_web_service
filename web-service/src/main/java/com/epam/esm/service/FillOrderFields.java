package com.epam.esm.service;

import com.epam.esm.dao.DaoException;
import com.epam.esm.model.Order;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class FillOrderFields {
    public Order fill(Order order, Set<String> fieldsToFind){
        Order orderToReturn = new Order(order.getId());
        if (fieldsToFind.contains("price")) {
            orderToReturn.setPrice(order.getPrice());
        }
        if (fieldsToFind.contains("createDate")) {
            orderToReturn.setCreateDate(order.getCreateDate());
        }
        if (fieldsToFind.contains("certificate")) {
            orderToReturn.setCertificate(order.getCertificate());

        }
        if (fieldsToFind.contains("user")) {
            orderToReturn.setUser(order.getUser());
        }

        return orderToReturn;
    }
}
