package com.epam.esm.facade.impl;

import com.epam.esm.controller.OrderController;
import com.epam.esm.dto.JsonResult;
import com.epam.esm.dto.PageMetadata;
import com.epam.esm.facade.OrderFacade;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.search.SearchOrderService;
import com.epam.esm.service.search.impl.SearchOrderByUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
        addHateoasLinks(order);

        return new JsonResult.Builder<Order>()
                .withSuccess(true)
                .withResult(Collections.singletonList(order))
                .build();
    }

    @Override
    public JsonResult<Order> save(Order order) {
        orderService.save(order);
        order = orderService.findById(order.getId());
        addHateoasLinks(order);

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
    public JsonResult<Order> getAllOrders(int page, int perPage, boolean includeMetadata) {
        List<Order> orders = orderService.findAll(page, perPage);
        int totalFound = orderService.countAll();
        PageMetadata pageMetadata = fillPageMetadata(includeMetadata, page, perPage, totalFound);
        orders.forEach(order -> addHateoasLinks(order));

        return new JsonResult.Builder<Order>()
                .withSuccess(true)
                .withResult(orders)
                .withMetadata(pageMetadata)
                .build();
    }

    @Override
    public JsonResult<Order> search(int userId, int page, int perPage, boolean includeMetadata) {
        SearchOrderService searchOrderService = new SearchOrderByUser(new User(userId), page, perPage);
        List<Order> orders = searchOrderService.search(orderService);
        int totalFound = searchOrderService.getTotalFound();
        PageMetadata pageMetadata = fillPageMetadata(includeMetadata, page, perPage, totalFound);
        orders.forEach(order -> addHateoasLinks(order));

        return new JsonResult.Builder<Order>()
                .withSuccess(true)
                .withResult(orders)
                .withMetadata(pageMetadata)
                .build();
    }

    private void addHateoasLinks(Order order) {
        order.add(linkTo(methodOn(OrderController.class).getCertificate(order.getId())).withSelfRel());
    }

    private PageMetadata fillPageMetadata(boolean includeMetadata, int page, int perPage, int totalFound) {
        if (includeMetadata) {
            PageMetadata pageMetadata = new PageMetadata.Builder()
                    .withPage(page)
                    .withPerPage(perPage)
                    .withPageCount(totalFound / perPage + (totalFound % perPage == 0 ? 0 : 1))
                    .withTotalCount(totalFound)
                    .build();
            int pageCount = pageMetadata.getPageCount();
            pageMetadata.add(linkTo(methodOn(OrderController.class).getAll(page, perPage, includeMetadata)).withSelfRel());
            pageMetadata.add(linkTo(methodOn(OrderController.class).getAll(1, perPage, includeMetadata)).withRel("first"));
            pageMetadata.add(linkTo(methodOn(OrderController.class).getAll(page < 2 ? 1 : page - 1, perPage, includeMetadata)).withRel("previous"));
            pageMetadata.add(linkTo(methodOn(OrderController.class).getAll(page >= pageCount ? pageCount : page + 1, perPage, includeMetadata)).withRel("next"));
            pageMetadata.add(linkTo(methodOn(OrderController.class).getAll(pageCount, perPage, includeMetadata)).withRel("last"));


            return pageMetadata;
        }
        return null;
    }
}
