package com.epam.esm.service.search.impl;

import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.search.SearchOrderService;

import java.util.List;

public class SearchOrderByUser implements SearchOrderService {
    private final User user;

    public SearchOrderByUser (User user) {
        this.user = user;
    }

    @Override
    public List<Order> search(OrderService service) {
        return service.findByUser(user);
    }
}
