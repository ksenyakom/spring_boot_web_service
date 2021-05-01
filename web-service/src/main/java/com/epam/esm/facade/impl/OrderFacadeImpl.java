package com.epam.esm.facade.impl;

import com.epam.esm.dto.JsonResult;
import com.epam.esm.facade.OrderFacade;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import com.epam.esm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class OrderFacadeImpl implements OrderFacade {
    private final OrderService orderService;

    @Autowired
    public OrderFacadeImpl(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public JsonResult<Order> getOrder(int id) {
        Order order = orderService.findById(id);
        return new JsonResult.Builder<Order>()
                .withSuccess(true)
                .withResult(Collections.singletonList(order))
                .build();
    }

    @Override
    public JsonResult<Order> save(Order order) {
        orderService.save(order);
        order = orderService.findById(order.getId());
        return new JsonResult.Builder<Order>()
                .withSuccess(true)
                .withResult(Collections.singletonList(order))
                .build();
    }

    @Override
    public JsonResult<Order> delete(int id) {
        orderService.delete(id);
        return new JsonResult.Builder<Order>()
                .withSuccess(true)
                .build();
    }

    @Override
    public JsonResult<Order> getAllOrders() {
        List<Order> orders = orderService.findAll();
        String message = String.format("Found results: %s.", orders.size());
        return new JsonResult.Builder<Order>()
                .withSuccess(true)
                .withResult(orders)
                .withMessage(message)
                .build();
    }
}
