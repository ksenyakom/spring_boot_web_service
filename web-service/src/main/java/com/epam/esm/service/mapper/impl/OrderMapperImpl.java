package com.epam.esm.service.mapper.impl;

import com.epam.esm.dao.DaoException;
import com.epam.esm.model.Order;
import com.epam.esm.service.mapper.OrderMapper;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public Order copyDefinedFields(Order copyFrom, Set<String> fieldToCopy) {
        Order orderToReturn = new Order(copyFrom.getId());
        if (fieldToCopy.contains("price")) {
            orderToReturn.setPrice(copyFrom.getPrice());
        }
        if (fieldToCopy.contains("createDate")) {
            orderToReturn.setCreateDate(copyFrom.getCreateDate());
        }
        if (fieldToCopy.contains("certificates")) {
            orderToReturn.setCertificates(copyFrom.getCertificates());

        }
        if (fieldToCopy.contains("user")) {
            orderToReturn.setUser(copyFrom.getUser());
        }

        orderToReturn.add(copyFrom.getLinks());

        return orderToReturn;
    }

}
