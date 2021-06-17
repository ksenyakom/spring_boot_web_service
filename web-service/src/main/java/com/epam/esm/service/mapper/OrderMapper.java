package com.epam.esm.service.mapper;

import com.epam.esm.model.Order;

import java.util.Set;

public interface OrderMapper {
    Order copyDefinedFields(Order copyFrom, Set<String> fieldToCopy);
}
