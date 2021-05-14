package com.epam.esm.service.search.impl;

import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.search.SearchOrderService;

import java.util.List;

public class SearchOrderByUser implements SearchOrderService {
    private final User user;
    private final int page;
    private final int size;
    private int totalFound;

    public SearchOrderByUser(User user, int page, int size) {
        this.user = user;
        this.page = page;
        this.size = size;
    }

    @Override
    public int getTotalFound() {
        return totalFound;
    }

    @Override
    public List<Order> search(OrderService service) {
        totalFound = service.countByUser(user);
        return service.findByUser(user, page, size);
    }
}
